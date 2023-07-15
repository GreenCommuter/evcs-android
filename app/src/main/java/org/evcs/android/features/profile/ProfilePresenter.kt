package org.evcs.android.features.profile

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.Subscription
import org.evcs.android.model.SubscriptionStatusWrapper
import org.evcs.android.model.user.User
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.PaymentMethodsService
import org.evcs.android.network.service.SubscriptionService
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.Extras
import org.evcs.android.util.StorageUtils
import org.evcs.android.util.UserUtils

open class ProfilePresenter(viewInstance: ProfileView?, services: RetrofitServices?)
    : ServicesPresenter<ProfileView>(viewInstance, services) {

    fun refreshUser() {
        getService(UserService::class.java).getCurrentUser().enqueue(
                object : AuthCallback<User>(this) {
                    override fun onResponseSuccessful(response: User) {
                        refreshSubscription(response)
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {}

                    override fun onCallFailure(t: Throwable) {}
                })
    }

    private fun refreshSubscription(user: User) {
        getService(SubscriptionService::class.java).status.enqueue(
                object : AuthCallback<SubscriptionStatusWrapper>(this) {
                    override fun onResponseSuccessful(response: SubscriptionStatusWrapper) {
                        user.activeSubscription = response.currentSubscription
                        view.onUserRefreshed(user)
                        UserUtils.saveUser(user)
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {}

                    override fun onCallFailure(t: Throwable) {}
                })
    }

    fun refreshDefaultPaymentMethod() {
        getService(PaymentMethodsService::class.java).paymentMethods.enqueue(
            object : AuthCallback<List<PaymentMethod>>(this) {
                override fun onResponseSuccessful(response: List<PaymentMethod>) {
                    val pm = response.firstOrNull { pm -> pm.id == UserUtils.getLoggedUser().defaultPm }
                    StorageUtils.storeInSharedPreferences(Extras.ChangePaymentMethod.PAYMENT_METHODS, pm)
                }

                override fun onResponseFailed(responseBody: ResponseBody, code: Int) {}

                override fun onCallFailure(t: Throwable) {}
            })
    }

}
