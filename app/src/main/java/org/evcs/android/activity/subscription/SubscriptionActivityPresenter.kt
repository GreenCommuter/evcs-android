package org.evcs.android.activity.subscription

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.SubscriptionStatusWrapper
import org.evcs.android.model.user.User
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.SubscriptionService
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter

class SubscriptionActivityPresenter(viewInstance: SubscriptionActivityView, services: RetrofitServices) :
        ServicesPresenter<SubscriptionActivityView?>(viewInstance, services) {

    //getStatus only retrieves active subscription
    fun refreshSubscription() {
            getService(UserService::class.java).currentUser.enqueue(
                    object : AuthCallback<User>(this) {
                        override fun onResponseSuccessful(response: User) {
                            if (response.activeSubscription != null)
                                view.onSubscriptionPlanRetrieved(response.activeSubscription!!)
                            else if (response.previousSubscription != null) {
                                view.onSubscriptionPlanRetrieved(response.previousSubscription)
                            }
                        }

                        override fun onResponseFailed(responseBody: ResponseBody, code: Int) {}

                        override fun onCallFailure(t: Throwable) {}
                    })
    }


}