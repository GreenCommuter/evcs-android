package org.evcs.android.features.profile

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.Payment
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.Subscription
import org.evcs.android.model.SubscriptionStatusWrapper
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.User
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.PaymentMethodsService
import org.evcs.android.network.service.PaymentsService
import org.evcs.android.network.service.SubscriptionService
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.MultipleRequestsManager
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.Extras
import org.evcs.android.util.StorageUtils
import org.evcs.android.util.UserUtils

open class ProfilePresenter(viewInstance: ProfileView?, services: RetrofitServices?)
    : ServicesPresenter<ProfileView>(viewInstance, services) {

    private lateinit var mIncompleteUser: User
    private var mSubscription: Subscription? = null
    private lateinit var mRejectedPayments: ArrayList<Payment>

    private var mMultipleRequestsManager: MultipleRequestsManager =
            MultipleRequestsManager(this)

    fun populate() {
        refreshUser()
        refreshSubscription()
        getRejectedPayments()
        mMultipleRequestsManager.fireRequests {
            mIncompleteUser.activeSubscription = mSubscription
            view.onUserRefreshed(mIncompleteUser)
            UserUtils.saveUser(mIncompleteUser)
            handleIssues(mSubscription)
        }
    }

    private fun refreshUser() {
        mMultipleRequestsManager.addRequest(getService(UserService::class.java).getCurrentUser(),
                object : AuthCallback<User>(this) {
                    override fun onResponseSuccessful(response: User) {
                        mIncompleteUser = response
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                        view.showError(ErrorUtils.getError(responseBody))
                    }

                    override fun onCallFailure(t: Throwable) {
                        runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
                    }
                })
    }

    private fun refreshSubscription() {
        mMultipleRequestsManager.addRequest(getService(SubscriptionService::class.java).status,
                object : AuthCallback<SubscriptionStatusWrapper>(this) {
                    override fun onResponseSuccessful(response: SubscriptionStatusWrapper) {
                        mSubscription = response.currentSubscription
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                        view?.showError(ErrorUtils.getError(responseBody))
                    }

                    override fun onCallFailure(t: Throwable) {
                        runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
                    }
                })
    }

    private fun handleIssues(subscription: Subscription?) {
        if (mRejectedPayments.isNotEmpty()) {
            view?.showPaymentIssue()
        } else if (subscription != null && subscription.issue) {
            view?.showSubscriptionIssue(subscription)
        } else view.hideIssues()
    }

    private fun getRejectedPayments() {
        mMultipleRequestsManager.addRequest(getService(PaymentsService::class.java).rejectedPayments,
                object : AuthCallback<ArrayList<Payment>>(this) {
                    override fun onResponseSuccessful(response: ArrayList<Payment>) {
                        mRejectedPayments = response
                    }

                    override fun onResponseFailed(responseBody: ResponseBody?, code: Int) {}

                    override fun onCallFailure(t: Throwable?) {}
                }
        )
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
