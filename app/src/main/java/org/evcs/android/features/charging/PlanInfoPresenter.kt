package org.evcs.android.features.charging

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.PaginatedResponse
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.Station
import org.evcs.android.model.SubscriptionStatusWrapper
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.PaymentMethodsService
import org.evcs.android.network.service.StationsService
import org.evcs.android.network.service.presenter.MultipleRequestsManager
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class PlanInfoPresenter(viewInstance: PlanInfoView?, services: RetrofitServices?) :
        ServicesPresenter<PlanInfoView>(viewInstance, services) {

    private var mStation: Station? = null
    private var mStatus: SubscriptionStatusWrapper? = null
    var mPaymentMethods: ArrayList<PaymentMethod> = ArrayList()

    private var mMultipleRequestsManager: MultipleRequestsManager =
            MultipleRequestsManager(this)

    fun populate(stationId : Int) {
        getSubscriptionStatus()
        getStation(stationId)
        getPaymentMethods()
        fireRequests()
    }

    private fun getSubscriptionStatus() {
        mMultipleRequestsManager.addRequest(getService(StationsService::class.java).getStatus(),
                object : AuthCallback<SubscriptionStatusWrapper?>(this) {
                    override fun onResponseSuccessful(response: SubscriptionStatusWrapper?) {
                        mStatus = response
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                        view.showError(ErrorUtils.getError(responseBody))
                    }

                    override fun onCallFailure(t: Throwable) {
                        view.showError(RequestError.getNetworkError())
                    }
                })
    }

    private fun getStation(id : Int) {
        mMultipleRequestsManager.addRequest(getService(StationsService::class.java).getStation(id),
                object : AuthCallback<PaginatedResponse<Station>?>(this) {
                    override fun onResponseSuccessful(response: PaginatedResponse<Station>?) {
                        mStation = response!!.page.getOrNull(0)
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                        view.showError(ErrorUtils.getError(responseBody))
                    }

                    override fun onCallFailure(t: Throwable) {
                        view.showError(RequestError.getNetworkError())
                    }
                })
    }

    private fun getPaymentMethods() {
        mMultipleRequestsManager.addRequest(getService(PaymentMethodsService::class.java).getPaymentMethods(),
                object : AuthCallback<List<PaymentMethod>?>(this) {
                    override fun onResponseSuccessful(response: List<PaymentMethod>?) {
                        mPaymentMethods.addAll(response!!)
                        if (!mPaymentMethods.isEmpty())
                            view.showDefaultPM(mPaymentMethods[0])
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                        view.showError(ErrorUtils.getError(responseBody))
                    }

                    override fun onCallFailure(t: Throwable) {
                        view.showError(RequestError.getNetworkError())
                    }
                })
    }

    private fun fireRequests() {
        mMultipleRequestsManager.fireRequests {
            if (mStation == null) {
                view.showError(RequestError("Station not found"))
            } else if (mStation!!.pricing.detail.showFreeChargingCode) {
                view.showFree(mStation!!.pricing.detail.freeChargingCode)
            } else {
                view.show(mStation!!, mStatus!!.currentSubscription)
            }
        }
    }

}
