package org.evcs.android.features.profile.plans

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.BaseConfiguration
import org.evcs.android.model.Plan
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.PlansService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils

class PlansPresenter(viewInstance: PlansView, services: RetrofitServices)
    : ServicesPresenter<PlansView>(viewInstance, services) {

    fun getPlans() {
        getService(PlansService::class.java).plans.enqueue(object : AuthCallback<ArrayList<Plan>>(this) {
            override fun onResponseSuccessful(response: ArrayList<Plan>) {
                getPlanExtras(response)
//                view.showPlans(response)
            }

            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable?) {
                view.showError(RequestError.getNetworkError())
            }

        })
    }

    //Temporary
    private fun getPlanExtras(plans: ArrayList<Plan>) {
        val r = object : RetrofitServices() {
            override fun getApiEndpoint(): String {
                return "https://evcs-assets.s3.us-west-2.amazonaws.com"
            }
        }
        r.init()
        r.getService(PlansService::class.java).planExtras.enqueue(object : AuthCallback<ArrayList<Plan>>(this) {
            override fun onResponseSuccessful(response: ArrayList<Plan>) {
                val plans2 = intersectPlans(plans, response)
                if (userHasHiddenPlan(plans2)) {
                    val url = String.format(BaseConfiguration.WebViews.PLANS_URL, UserUtils.getSessionToken())
                    view.userHasHiddenPlan(url)
                } else {
                    val runnable = { view.showPlans(plans2) }
                    runIfViewCreated(runnable)
                }
            }

            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable?) {
                view.showError(RequestError.getNetworkError())
            }

        })
    }

    //Temporary
    private fun intersectPlans(plans: ArrayList<Plan>, response: ArrayList<Plan>): ArrayList<Plan> {
        val merged = arrayListOf<Plan>()
        response.forEach { planExtra ->
            val plan = plans.firstOrNull { plan -> plan.name.equals(planExtra.name) }
            if (plan != null) {
                plan.banner = planExtra.banner
                plan.useCase = planExtra.useCase
                merged.add(plan)
            }
        }
        return merged
    }

    private fun userHasHiddenPlan(allowedPlans: ArrayList<Plan>): Boolean {
        val userPlan = UserUtils.getLoggedUser()?.activeSubscription?.plan
        return userPlan != null && !allowedPlans.map { plan -> plan.name }.contains(userPlan.name)
    }

}
