package org.evcs.android.features.main

import com.base.networking.retrofit.RetrofitServices
import com.base.networking.retrofit.callback.NetworkCallback
import okhttp3.ResponseBody
import org.evcs.android.model.Payment
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.AuthUser
import org.evcs.android.model.user.User
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.PaymentsService
import org.evcs.android.network.service.SubscriptionService
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils

class InitialDialogsPresenter(viewInstance: InitialDialogsView?, services: RetrofitServices?)
    : ServicesPresenter<InitialDialogsView>(viewInstance, services) {

    fun checkTokenExpiration() {
        val expiration = UserUtils.getSessionTokenExpiration()
        if (expiration == null || expiration.minusDays(1).isBeforeNow) {
            refreshToken()
        }
    }

    fun refreshToken() {
        getService(UserService::class.java).refreshToken().enqueue(object : NetworkCallback<AuthUser>() {
            override fun onResponseSuccessful(response: AuthUser) {
                UserUtils.saveAuthUser(response)
            }

            override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
            }

            override fun onCallFailure(throwable: Throwable) {
            }
        })
    }

    fun checkPendingCancelation() {
        getService(UserService::class.java).currentUser.enqueue(object : NetworkCallback<User?>() {
            override fun onResponseSuccessful(response: User?) {
                if (response?.previousSubscription?.pendingCancelConfirmation == true
                    && response.activeSubscription == null) {
                    view?.onPendingCancelation(response?.activeSubscription!!)
                }
            }

            override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
            }

            override fun onCallFailure(throwable: Throwable) {
            }
        })
    }

    fun confirmCancelation(id: String) {
        getService(SubscriptionService::class.java).confirmCancelation(id).enqueue(object : NetworkCallback<Void?>() {
            override fun onResponseSuccessful(response: Void?) {
                view?.onConfirmCancelation()
            }

            override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable) {
                runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
            }

        })
    }

    fun checkAccountSuspended() {
        if (UserUtils.getLoggedUser() == null) return

        //TODO: don't bring the user twice with check pending
        if (UserUtils.getLoggedUser().activeSubscription != null) {
            getService(UserService::class.java).getCurrentUser().enqueue(getAccountSuspendedCallback<User> {
                user -> user.activeSubscription?.isSuspended == true
            })
        } else {
            getService(PaymentsService::class.java).rejectedPayments.enqueue(getAccountSuspendedCallback<ArrayList<Payment>> {
                payments -> payments.isNotEmpty()
            })
        }
    }

    fun <T> getAccountSuspendedCallback(onSuccess: (T) -> Boolean): AuthCallback<T> {
        return object : AuthCallback<T>(this) {
            override fun onResponseSuccessful(response: T) {
                if (onSuccess.invoke(response)) {
                    view.showAccountSuspendedDialog()
                }
            }

            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable?) {
                runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
            }

        }

    }

}
