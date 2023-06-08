package org.evcs.android.features.profile.wallet

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.PaymentMethod
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.PaymentMethodsService
import org.evcs.android.util.ErrorUtils

class WalletHeaderPresenter(viewInstance: IWalletHeaderView, services: RetrofitServices?)
    : CreditCardPresenter<IWalletHeaderView>(viewInstance, services) {

    fun getCreditCards() {
        getService(PaymentMethodsService::class.java).paymentMethods.enqueue(object : AuthCallback<List<PaymentMethod>>(this) {
            override fun onResponseSuccessful(creditCardInformations: List<PaymentMethod>) {
                view.onPaymentMethodsReceived(creditCardInformations)
            }

            override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable) {
                runIfViewCreated(Runnable { view.onPaymentMethodsNotReceived() })
            }
        })
    }
}