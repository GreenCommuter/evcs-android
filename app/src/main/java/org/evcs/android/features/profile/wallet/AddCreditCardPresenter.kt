package org.evcs.android.features.profile.wallet

import com.base.networking.retrofit.RetrofitServices
import com.stripe.android.model.Address
import com.stripe.android.model.ConfirmSetupIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.model.PaymentMethodCreateParams
import okhttp3.ResponseBody
import org.evcs.android.Configuration
import org.evcs.android.model.ClientSecret
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.network.service.PaymentMethodsService
import org.evcs.android.util.UserUtils
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.model.shared.RequestError
import org.evcs.android.util.ErrorUtils

class AddCreditCardPresenter(viewInstance: AddCreditCardView, services: RetrofitServices?)
    : CreditCardPresenter<AddCreditCardView>(viewInstance, services) {

    private var mClientSecret: String? = null

    fun getClientSecret() {
        getService(PaymentMethodsService::class.java).getClientSecret()
            .enqueue(object : AuthCallback<ClientSecret>(this) {
                override fun onResponseSuccessful(response: ClientSecret) {
                    mClientSecret = response.clientSecret
                }

                override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                    view.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(t: Throwable) {
                    view.showError(RequestError.getNetworkError())
                }
            })
        }

    fun getConfirmParams(name: String, card: PaymentMethodCreateParams.Card, zipcode: String): ConfirmSetupIntentParams {
        val billingDetails = PaymentMethod.BillingDetails.Builder()
            .setName(name)
//            .setEmail(UserUtils.getUserEmail())
            .setAddress(Address.Builder().setPostalCode(zipcode).build())
            .build()

        val paymentMethodParams = PaymentMethodCreateParams
            .create(card, billingDetails, null)

        return ConfirmSetupIntentParams
            .create(paymentMethodParams, mClientSecret!!)
    }
}