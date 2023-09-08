package org.evcs.android.features.profile.plans

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.BaseConfiguration
import org.evcs.android.model.Plan
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.PlansService
import org.evcs.android.network.service.presenter.MultipleRequestsManager
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class PlansPresenter(viewInstance: PlansView, services: RetrofitServices)
    : ServicesPresenter<PlansView>(viewInstance, services) {

    private lateinit var mPlans: ArrayList<Plan>
    private lateinit var mPlanExtras: ArrayList<Plan>

    fun getPlans() {
        val m = MultipleRequestsManager(this)
        m.addRequest(getService(PlansService::class.java).plans, object : AuthCallback<ArrayList<Plan>>(this) {
            override fun onResponseSuccessful(response: ArrayList<Plan>) {
//                view.showPlans(response)
                mPlans = response
            }

            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable?) {
                view.showError(RequestError.getNetworkError())
            }

        })
        getPlanExtras(m)
        m.fireRequests {
            mergePlans(mPlans, mPlanExtras)
            //TODO: API should do this
            val plans = mPlans.filter { plan -> BaseConfiguration.ALLOWED_PLANS.contains(plan.name) }
            val runnable = { view.showPlans(plans) }
            runIfViewCreated(runnable)
        }
    }

    //Temporary
    private fun getPlanExtras(m: MultipleRequestsManager) {
        val r = object : RetrofitServices() {
            override fun getApiEndpoint(): String {
                return "https://evcs-assets.s3.us-west-2.amazonaws.com"
            }

        }
        r.init()
        m.addRequest(r.getService(PlansService::class.java).planExtras, object : AuthCallback<ArrayList<Plan>>(this) {
            override fun onResponseSuccessful(response: ArrayList<Plan>) {
                mPlanExtras = response
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
    private fun mergePlans(plans: ArrayList<Plan>, response: ArrayList<Plan>) {
        response.forEach { planExtra ->
            plans.firstOrNull { plan -> plan.name.equals(planExtra.name) }
                    .let { plan ->
                        plan?.banner = planExtra.banner
                        plan?.useCase = planExtra.useCase
                    }
        }
    }

}
