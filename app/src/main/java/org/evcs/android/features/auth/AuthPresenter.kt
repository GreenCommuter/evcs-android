package org.evcs.android.features.auth

import com.base.networking.retrofit.RetrofitServices
import com.base.networking.retrofit.callback.NetworkCallback
import com.base.networking.utils.NetworkCodes
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.iid.FirebaseInstanceId
import okhttp3.ResponseBody
import org.evcs.android.BaseConfiguration
import org.evcs.android.model.push.DeviceToken
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.AuthUser
import org.evcs.android.model.user.UserRequestFacebook
import org.evcs.android.model.user.UserRequestGoogle
import org.evcs.android.network.service.DeviceTokensService
import org.evcs.android.network.service.UserService
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class AuthPresenter<T>(viewInstance: T, services: RetrofitServices?) :
    TermsPresenter<T>(viewInstance, services) where T : AuthView?, T : ITermsView? {

    protected fun getLoginCallback(): NetworkCallback<AuthUser> {
        return object : NetworkCallback<AuthUser>() {
            override fun onResponseSuccessful(authUser: AuthUser?) {
                UserUtils.saveAuthUser(authUser)
                getToken()
            }

            override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                view?.showErrorPopup(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(throwable: Throwable) {
                runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
            }
        }
    }

    open fun getAccessToken(account: GoogleSignInAccount) {
        getService(UserService::class.java).getAccessToken(UserRequestGoogle.ACCESS_TOKEN_REQUEST_FIELDS.URL,
                UserRequestGoogle.ACCESS_TOKEN_REQUEST_FIELDS.GRANT_TYPE,
                UserRequestGoogle.ACCESS_TOKEN_REQUEST_FIELDS.CLIENT_ID,
                UserRequestGoogle.ACCESS_TOKEN_REQUEST_FIELDS.CLIENT_SECRET,
                "", account.serverAuthCode, account.idToken)
                .enqueue(object : NetworkCallback<UserRequestGoogle.Tokens>() {
                    override fun onResponseSuccessful(tokens: UserRequestGoogle.Tokens) {
                        logInGoogle(account.email, tokens.idToken, tokens.accessToken)
                    }

                    override fun onResponseFailed(responseBody: ResponseBody?, i: Int) {
                        view.showError(ErrorUtils.getError(responseBody!!))
                    }

                    override fun onCallFailure(throwable: Throwable?) {
                        runIfViewCreated(Runnable { view.showError(RequestError.getNetworkError()) })
                    }
                })
    }

    open fun logInGoogle(email: String?, idToken: String?, accessToken: String?) {
        getService(UserService::class.java).logInGoogle(UserRequestGoogle(email, idToken, accessToken))
                .enqueue(getLoginCallback())
    }

    open fun logInFacebook(accessToken: AccessToken) {
        getService(UserService::class.java).logInFacebook(UserRequestFacebook(accessToken.token))
                .enqueue(object : NetworkCallback<AuthUser?>() {
                    override fun onResponseSuccessful(authUser: AuthUser?) {
                        getLoginCallback().onResponseSuccessful(authUser)
                    }

                    override fun onResponseFailed(responseBody: ResponseBody?, i: Int) {
                        if (i == NetworkCodes.ERROR_UNAUTHORIZED) {
                            view.showFacebookError()
                        } else {
                            getLoginCallback().onResponseFailed(responseBody, i)
                        }
                    }

                    override fun onCallFailure(throwable: Throwable?) {
                        getLoginCallback().onCallFailure(throwable)
                    }
                })
        LoginManager.getInstance().logOut()
    }

    fun getToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            sendNotifToken(task.result.token)
        }
    }

    /**
     * We need to send the [DeviceToken] to API. This is because there is a database mapping
     * each user to one or more token. So, if we change the device that a user is using, API has
     * to know where to send the notifications.
     * @param token The device token
     */
    fun sendNotifToken(token: String?) {
        val device = DeviceToken(token, BaseConfiguration.DEVICE_TYPE)
        getService(DeviceTokensService::class.java)
                .addDeviceToken(device)
                .enqueue(object : Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
//                        val user: User = UserUtils.getLoggedUser()
//                        Rollbar.setPersonData(Integer.toString(user.getId()), user.getFirstName(),
//                                user.getEmail())
                        view.onTokenSent()
                    }

                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        runIfViewCreated(Runnable { view.showError(RequestError.getNetworkError()) })
                    }
                })
    }
}