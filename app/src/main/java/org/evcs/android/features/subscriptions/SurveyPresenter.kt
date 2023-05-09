package org.evcs.android.features.subscriptions

import android.text.Editable
import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.SubscriptionSurveyRequest
import org.evcs.android.model.SurveyItem
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.SubscriptionService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils
import java.util.HashSet

class SurveyPresenter(viewInstance: SurveyView?, services: RetrofitServices?)
    : ServicesPresenter<SurveyView>(viewInstance, services) {

    val subscriptionId = UserUtils.getLoggedUser().activeSubscription!!.id

    fun getSurveyQuestions() {
        getService(SubscriptionService::class.java).questions.enqueue(object : AuthCallback<ArrayList<SurveyItem>>(this) {
            override fun onResponseSuccessful(response: ArrayList<SurveyItem>) {
                view.showQuestions(response)
            }

            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable?) {
                view.showError(RequestError.getNetworkError())
            }

        })
    }

    fun sendSurveyResults(checkedItems: HashSet<String>, otherId: String, text: Editable) {
        val request = SubscriptionSurveyRequest(subscriptionId, checkedItems, otherId, text.toString())
        getService(SubscriptionService::class.java).createSubscriptionSurvey(request).enqueue(object : AuthCallback<Void>(this) {
            override fun onResponseSuccessful(response: Void) {
            }

            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable?) {
                view.showError(RequestError.getNetworkError())
            }

        })
    }

    fun cancelSubscription() {
        getService(SubscriptionService::class.java).cancelSubscription(subscriptionId).enqueue(object : AuthCallback<Void>(this) {
            override fun onResponseSuccessful(response: Void) {
                view.onSubscriptionCanceled()
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
