package org.evcs.android.features.profile.plans

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.Plan
import org.evcs.android.model.SubscriptionRequest
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.SubscriptionService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils

class GetPlanPresenter(viewInstance: GetPlanView?, services: RetrofitServices?) : ServicesPresenter<GetPlanView>(viewInstance, services) {

    fun subscribe(plan: Plan, paymentMethod: String) {
        val id = UserUtils.getLoggedUser().activeSubscription?.id
        val request = SubscriptionRequest(plan.id, paymentMethod)
        val call = if (id == null) getService(SubscriptionService::class.java).createSubscription(request)
                   else getService(SubscriptionService::class.java).changePlan(id, request)
        call.enqueue(object : AuthCallback<Void>(this) {
                override fun onResponseSuccessful(response: Void) {
                    view.onSubscriptionSuccess(response)
                }

                override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                    view.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(t: Throwable) {
                    view.showError(RequestError.getNetworkError())
                }
            })
    }

}
