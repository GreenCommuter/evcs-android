package org.evcs.android.features.charging

import com.base.networking.retrofit.RetrofitServices
import com.rollbar.android.Rollbar
import okhttp3.ResponseBody
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.Station
import org.evcs.android.model.SubscriptionStatusWrapper
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.RateWrapper
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.ChargesService
import org.evcs.android.network.service.PaymentMethodsService
import org.evcs.android.network.service.StationsService
import org.evcs.android.network.service.SubscriptionService
import org.evcs.android.network.service.presenter.MultipleRequestsManager
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils

class PlanInfoPresenter(viewInstance: PlanInfoView?, services: RetrofitServices?) :
        ServicesPresenter<PlanInfoView>(viewInstance, services) {

    private var mErrorCode: Int = 0
    private var mStation: Station? = null
    private lateinit var mError: RequestError
    private var mStatus: SubscriptionStatusWrapper? = null
    var mPaymentMethods: ArrayList<PaymentMethod> = ArrayList()

    private var mMultipleRequestsManager: MultipleRequestsManager =
            MultipleRequestsManager(this)

    fun populate(stationId : String) {
        getSubscriptionStatus()
        getStation(stationId)
        if (mPaymentMethods.isEmpty())
            getPaymentMethods()
        fireRequests()
    }

    private fun getSubscriptionStatus() {
        mMultipleRequestsManager.addRequest(getService(SubscriptionService::class.java).getStatus(),
                object : AuthCallback<SubscriptionStatusWrapper?>(this) {
                    override fun onResponseSuccessful(response: SubscriptionStatusWrapper?) {
                        mStatus = response
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
//                        view.showError(ErrorUtils.getError(responseBody))
                    }

                    override fun onCallFailure(t: Throwable) {
//                        view.showError(RequestError.getNetworkError())
                    }
                })
    }

    private fun getStation(id : String) {
        mMultipleRequestsManager.addRequest(getService(StationsService::class.java).getStationFromQR(id),
                object : AuthCallback<Station>(this) {
                    override fun onResponseSuccessful(response: Station) {
                        mStation = response//!!.page!!.getOrNull(0)
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                        mError = ErrorUtils.getError(responseBody)
                        mErrorCode = code
                    }

                    override fun onCallFailure(t: Throwable) {
                        mError = RequestError.getNetworkError()
                    }
                })
    }

    private fun getPaymentMethods() {
        mMultipleRequestsManager.addRequest(getService(PaymentMethodsService::class.java).getPaymentMethods(),
                object : AuthCallback<List<PaymentMethod>?>(this) {
                    override fun onResponseSuccessful(response: List<PaymentMethod>?) {
                        mPaymentMethods.addAll(response!!)
                         view.showDefaultPM(mPaymentMethods.firstOrNull {
                             pm -> pm.id == UserUtils.getLoggedUser().defaultPm
                         })
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
//                        view.showError(ErrorUtils.getError(responseBody))
                    }

                    override fun onCallFailure(t: Throwable) {
//                        view.showError(RequestError.getNetworkError())
                    }
                })
    }


    private fun getPpkWh() {
        getService(ChargesService::class.java).getPpkWh(getStationId()).enqueue(
                object : AuthCallback<RateWrapper>(this) {
                    override fun onResponseSuccessful(response: RateWrapper) {
                        view.showChargeRate(response.rateValue)
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
//                        view.showError(ErrorUtils.getError(responseBody))
                    }

                    override fun onCallFailure(t: Throwable) {
//                        view.showError(RequestError.getNetworkError())
                    }
                })
    }

    private fun fireRequests() {
        mMultipleRequestsManager.fireRequests {
            try {
                if (mStation == null) {
                    view.showStationNotFound(mErrorCode, mError)
                } else if (mStation!!.pricing!!.detail.showFreeChargingCode) {
                    view.showFree(mStation!!.pricing!!.detail.freeChargingCode!!)
                } else {
                    view.show(mStation!!, mStatus?.currentSubscription)
                    getPpkWh()
                }
            } catch (e: java.lang.NullPointerException) {
                view.showStationNotFound(mErrorCode, RequestError.getUnknownError())
                Rollbar.instance().error("NPE in station " + (mStation?.id).toString())
            }
        }
    }

    fun getStationId(): Int {
        return mStation!!.id
    }

}
