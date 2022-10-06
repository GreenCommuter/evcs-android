package org.evcs.android.features.auth

import com.base.networking.retrofit.RetrofitServices
import com.base.networking.retrofit.callback.NetworkCallback
import org.evcs.android.model.user.AuthUser
import org.evcs.android.util.UserUtils
import okhttp3.ResponseBody
import org.evcs.android.util.ErrorUtils
import org.evcs.android.model.shared.RequestError
import com.google.firebase.iid.FirebaseInstanceId
import org.evcs.android.BaseConfiguration
import org.evcs.android.model.DeviceToken

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
    }
}