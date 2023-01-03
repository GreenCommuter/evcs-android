package org.evcs.android.features.auth.forgotPassword

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

/**
 * The ForgotPasswordPresenter class wraps API calls to so a user can ask for a password reset.
 *
 * @param viewInstance View to do the authentication callbacks.
 */
class ForgotPasswordPresenter(viewInstance: ForgotPasswordView?, services: RetrofitServices?) :
    ServicesPresenter<ForgotPasswordView?>(viewInstance, services) {
    /**
     * Tries to send a "Rest password instructions" email to the user.
     *
     * @param email User's email to receive reset password instructions
     */
    fun requestPasswordReset(email: String) {
        getService(UserService::class.java).requestPasswordReset(email)
            .enqueue(object : AuthCallback<Void?>(this) {
                override fun onResponseSuccessful(response: Void?) {
                    view?.onResetRequest();
                }

                override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                    view?.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(throwable: Throwable) {
                    runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
                }
            })
    }
}