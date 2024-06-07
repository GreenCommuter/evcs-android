package org.evcs.android.features.main

import com.base.networking.retrofit.RetrofitServices
import com.base.networking.retrofit.callback.NetworkCallback
import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.AuthUser
import org.evcs.android.model.user.User
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
                } else {
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


}
