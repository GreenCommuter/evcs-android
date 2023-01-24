package org.evcs.android.features.profile.wallet

import com.base.networking.retrofit.RetrofitServices
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.network.callback.AuthCallback
import okhttp3.ResponseBody
import org.evcs.android.network.service.PaymentMethodsService
import org.evcs.android.model.CreditCard
import org.evcs.android.model.PaymentMethod
import org.evcs.android.util.ErrorUtils

class PaymentMethodPresenter<T : IPaymentMethodView>(viewInstance: T, services: RetrofitServices?)
    : ServicesPresenter<T>(viewInstance, services) {
    private val mPaymentMethodUrlCallback: AuthCallback<List<PaymentMethod>>

    init {
        mPaymentMethodUrlCallback = object : AuthCallback<List<PaymentMethod>>(this) {
            override fun onResponseSuccessful(creditCardInformations: List<PaymentMethod>) {
                view.onPaymentMethodsReceived(creditCardInformations.map { cci -> cci.card })
            }

            override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable) {
                runIfViewCreated(Runnable { view.onPaymentMethodsNotReceived() })
            }
        }
    }

    fun getCreditCards() {
        getService(PaymentMethodsService::class.java).paymentMethods.enqueue(mPaymentMethodUrlCallback)
    }

    fun makeDefaultPaymentMethod(item: CreditCard?) {
//        getService(BrainTreeService.class).makeDefaultPaymentMethod(item.getToken())
//                .enqueue(mPaymentMethodUrlCallback);
    }

    fun removePaymentMethod(item: CreditCard?) {
//        getService(BrainTreeService.class).removePaymentMethod(item.getToken())
//                .enqueue(mPaymentMethodUrlCallback);
    }

}