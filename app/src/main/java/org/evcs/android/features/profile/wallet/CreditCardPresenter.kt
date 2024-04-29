package org.evcs.android.features.profile.wallet

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.PaymentMethod
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.service.PaymentMethodsService
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.Extras
import org.evcs.android.util.StorageUtils
import org.evcs.android.util.UserUtils

open class CreditCardPresenter<T : ICreditCardView?>(viewInstance: T, services: RetrofitServices?)
    : ServicesPresenter<T>(viewInstance, services) {

    fun makeDefaultPaymentMethod(item: PaymentMethod) {
        makeDefaultPaymentMethod(item, null)
    }

    fun makeDefaultPaymentMethod(id: String) {
        makeDefaultPaymentMethod(null, id)
    }

    private fun makeDefaultPaymentMethod(item: PaymentMethod?, id: String?) {
        getService(PaymentMethodsService::class.java).setDefaultPaymentMethod(item?.id ?: id)
                .enqueue(object : AuthCallback<Void?>(this) {
                    override fun onResponseSuccessful(response: Void?) {
                        if (item != null)
                            onDefaultPaymentMethodSet(item)
                        else
                            getPaymentMethod(id!!)
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                        view.onMakeDefaultError(ErrorUtils.getError(responseBody))
                    }

                    override fun onCallFailure(t: Throwable) {
                        runIfViewCreated(Runnable { view.showError(RequestError.getNetworkError()) })
                    }
                })
    }

    private fun onDefaultPaymentMethodSet(item: PaymentMethod) {
        val user = UserUtils.getLoggedUser()
        user.defaultPm = item.id
        UserUtils.saveUser(user)

        StorageUtils.storeInSharedPreferences(Extras.ChangePaymentMethod.PAYMENT_METHODS, item)

        view.onDefaultPaymentMethodSet(item)
    }

    // The problem is that when I add a CC, I receive a stripe.PaymentMethod, which is very similar
    // but not equal to my model.PaymentMethod. So I'll trust the API to do the conversion
    // It would probably be cleaner and quicker to do it here though
    private fun getPaymentMethod(id: String) {
        getService(PaymentMethodsService::class.java).paymentMethods.enqueue(
                object : AuthCallback<List<PaymentMethod>>(this) {
                    override fun onResponseSuccessful(response: List<PaymentMethod>) {
                        val pm = response.firstOrNull { pm -> pm.id == id }
                        onDefaultPaymentMethodSet(pm?:return)
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {}

                    override fun onCallFailure(t: Throwable) {}
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
                        runIfViewCreated(Runnable { view.showError(RequestError.getNetworkError())  })
                    }
                })
    }


}