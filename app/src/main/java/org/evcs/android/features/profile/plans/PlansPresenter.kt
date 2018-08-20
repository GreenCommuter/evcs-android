package org.evcs.android.features.profile.plans

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.Plan
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.PlansService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class PlansPresenter(viewInstance: PlansView, services: RetrofitServices)
    : ServicesPresenter<PlansView>(viewInstance, services) {

    fun getPlans() {
        getService(PlansService::class.java).plans.enqueue(object : AuthCallback<ArrayList<Plan>>(this) {
            override fun onResponseSuccessful(response: ArrayList<Plan>) {
                view.showPlans(response)
            }

            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable?) {
                view.showError(RequestError.getNetworkError())
            }

        })
    }

}
