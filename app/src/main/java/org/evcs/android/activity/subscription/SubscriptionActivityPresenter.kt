package org.evcs.android.activity.subscription

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.SubscriptionStatusWrapper
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.SubscriptionService
import org.evcs.android.network.service.presenter.ServicesPresenter

class SubscriptionActivityPresenter(viewInstance: SubscriptionActivityView, services: RetrofitServices) :
        ServicesPresenter<SubscriptionActivityView?>(viewInstance, services) {

    fun refreshSubscription() {
            getService(SubscriptionService::class.java).getStatus().enqueue(
                    object : AuthCallback<SubscriptionStatusWrapper>(this) {
                        override fun onResponseSuccessful(response: SubscriptionStatusWrapper) {
                            view.onSubscriptionPlanRetrieved(response.currentSubscription!!)
                        }

                        override fun onResponseFailed(responseBody: ResponseBody, code: Int) {}

                        override fun onCallFailure(t: Throwable) {}
                    })
    }


}