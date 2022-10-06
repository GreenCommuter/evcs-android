package org.evcs.android.features.auth.forgotPassword

import com.base.networking.retrofit.RetrofitServices
import org.evcs.android.network.service.presenter.ServicesPresenter

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
    fun requestPasswordReset(email: String) {}
}