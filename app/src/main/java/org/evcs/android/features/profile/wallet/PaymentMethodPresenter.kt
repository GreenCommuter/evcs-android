package org.evcs.android.features.profile.wallet

import com.base.networking.retrofit.RetrofitServices
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.network.callback.AuthCallback
import okhttp3.ResponseBody
import org.evcs.android.network.service.PaymentMethodsService
import org.evcs.android.model.PaymentMethod
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils

class PaymentMethodPresenter<T : IPaymentMethodView>(viewInstance: T, services: RetrofitServices?)
    : ServicesPresenter<T>(viewInstance, services) {

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

    fun makeDefaultPaymentMethod(item: PaymentMethod) {
        getService(PaymentMethodsService::class.java).setDefaultPaymentMethod(item.id)
            .enqueue(object : AuthCallback<Void?>(this) {
            override fun onResponseSuccessful(response: Void?) {
                val user = UserUtils.getLoggedUser()
                user.defaultPm = item.id
                UserUtils.saveUser(user)
                view.onDefaultPaymentMethodSet(item)
            }

            override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable) {
                runIfViewCreated(Runnable { view.onPaymentMethodsNotReceived() })
            }
        })
    }

    fun removePaymentMethod(item: PaymentMethod) {
        getService(PaymentMethodsService::class.java).removePaymentMethod(item.id)
            .enqueue(object : AuthCallback<Void?>(this) {
            override fun onResponseSuccessful(response: Void?) {
                view.onPaymentMethodRemoved(item)
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