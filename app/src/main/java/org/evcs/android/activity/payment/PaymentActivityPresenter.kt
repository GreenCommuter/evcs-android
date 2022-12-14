package org.evcs.android.activity.payment

import okhttp3.ResponseBody
import org.evcs.android.model.Location
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.EVCSRetrofitServices
import org.evcs.android.network.service.LocationService
import org.evcs.android.network.service.PaymentMethodsService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class PaymentActivityPresenter(viewInstance: PaymentActivityView, services: EVCSRetrofitServices?) :
    ServicesPresenter<PaymentActivityView>(viewInstance, services) {

    fun getPaymentMethods() {
        getService(PaymentMethodsService::class.java).getPaymentMethods()
            .enqueue(object : AuthCallback<List<PaymentMethod>?>(this) {
                override fun onResponseSuccessful(response: List<PaymentMethod>?) {
                    view.showPaymentMethods(response)
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
