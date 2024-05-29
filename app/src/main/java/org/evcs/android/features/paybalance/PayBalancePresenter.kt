package org.evcs.android.features.paybalance

import com.base.networking.retrofit.RetrofitServices
import com.rollbar.android.Rollbar
import okhttp3.ResponseBody
import org.evcs.android.model.Payment
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.PaymentsService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class PayBalancePresenter(viewInstance: PayBalanceView, services: RetrofitServices) :
    ServicesPresenter<PayBalanceView?>(viewInstance, services) {

    fun getPendingPayment() {
        getService(PaymentsService::class.java).rejectedPayments.enqueue(
            object : AuthCallback<ArrayList<Payment>>(this) {
                override fun onResponseSuccessful(response: ArrayList<Payment>) {
                    if (response.size == 1) {
                        view.showPendingPayment(response.first())
                    } else {
                        Rollbar.instance().warning("More than one payment returned: " + response.size.toString())
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

    fun processPayment() {
        view.onPaymentSuccess()
//        getService(PaymentsService::class.java).deleteAccount()
//            .enqueue(object : AuthCallback<Void>(this) {
//                override fun onResponseSuccessful(response: Void?) {
//                    view.onAccountDeleted()
//                }
//
//                override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
//                    view.showError(ErrorUtils.getError(responseBody))
//                }
//
//                override fun onCallFailure(t: Throwable) {
//                    view.showError(RequestError.getNetworkError())
//                }
//            })
    }
}
