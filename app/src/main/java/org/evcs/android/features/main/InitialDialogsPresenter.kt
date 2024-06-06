package org.evcs.android.features.main

import com.base.networking.retrofit.RetrofitServices
import com.base.networking.retrofit.callback.NetworkCallback
import okhttp3.ResponseBody
import org.evcs.android.EVCSApplication
import org.evcs.android.model.user.AuthUser
import org.evcs.android.model.user.User
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
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
        val service = EVCSApplication.getInstance().retrofitServices.getService(UserService::class.java)
        service.refreshToken().enqueue(object : NetworkCallback<AuthUser>() {
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
        val service = EVCSApplication.getInstance().retrofitServices.getService(UserService::class.java)
        service.currentUser.enqueue(object : NetworkCallback<User?>() {
            override fun onResponseSuccessful(response: User?) {
                if (response?.previousSubscription?.pendingCancelConfirmation == true
                    && response.activeSubscription == null) {
                    view?.onPendingCancelation()
                }
            }

            override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
            }

            override fun onCallFailure(throwable: Throwable) {
            }
        })
    }

    fun confirmCancelation() {
        TODO("Not yet implemented")
    }


}
