package org.evcs.android.features.profile.notifications

import com.base.networking.retrofit.RetrofitServices
import org.evcs.android.network.service.presenter.ServicesPresenter

class NotificationsPresenter(viewInstance: NotificationsView, services: RetrofitServices) :
    ServicesPresenter<NotificationsView>(viewInstance, services) {

    fun toggleNotifications(checked: Boolean) {
//        getService(PlansService::class.java).plans.enqueue(object : AuthCallback<ArrayList<Plan>>(this) {
//            override fun onResponseSuccessful(response: ArrayList<Plan>) {
//                view.showPlans(response)
//            }
//
//            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
//                view.showError(ErrorUtils.getError(responseBody))
//            }
//
//            override fun onCallFailure(t: Throwable?) {
//                view.showError(RequestError.getNetworkError())
//            }
//
//        })
    }
}
