package org.evcs.android.activity.subscription

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.SubscriptionStatusWrapper
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.User
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.SubscriptionService
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class SubscriptionPresenter(viewInstance: SubscriptionView, services: RetrofitServices) :
        ServicesPresenter<SubscriptionView?>(viewInstance, services) {

    fun refreshSubscription() {
            getService(SubscriptionService::class.java).status.enqueue(
                    object : AuthCallback<SubscriptionStatusWrapper>(this) {
                        override fun onResponseSuccessful(response: SubscriptionStatusWrapper) {
                            if (response.currentSubscription != null)
                                view.onSubscriptionPlanRetrieved(response.currentSubscription!!)
                            else
                                view.showError(RequestError.getUnknownError())
                        }

                        override fun onResponseFailed(responseBody: ResponseBody, code: Int) {}

                        override fun onCallFailure(t: Throwable) {}
                    })
    }

    fun voidCancellation(subscriptionId: String) {
        getService(SubscriptionService::class.java).voidSubscriptionCancelation(subscriptionId).enqueue(
            object : AuthCallback<Void?>(this) {
                override fun onResponseSuccessful(response: Void?) {
                    refreshSubscription()
                }

                override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                    view?.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(t: Throwable) {
                    view?.showError(RequestError.getNetworkError())
                }
            })
    }

}