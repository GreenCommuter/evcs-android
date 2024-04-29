package org.evcs.android.features.profile.notifications

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.BooleanWrapper
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.MessageService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class NotificationsPresenter(viewInstance: NotificationsView, services: RetrofitServices) :
    ServicesPresenter<NotificationsView>(viewInstance, services) {

    fun getCallback(): AuthCallback<Void?> {
        return object : AuthCallback<Void?>(this) {
            override fun onResponseSuccessful(response: Void?) {
                view.onSuccess()
            }

            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable?) {
                view.showError(RequestError.getNetworkError())
            }
        }
    }

    fun optOut() {
        getService(MessageService::class.java).optOut()
            .enqueue(getCallback())
    }

    fun toggleNotifications(checked: Boolean) {
        getService(MessageService::class.java).enableNotifications(BooleanWrapper(checked.toString()))
            .enqueue(getCallback())
    }
}
