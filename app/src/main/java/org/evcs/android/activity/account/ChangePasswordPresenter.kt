package org.evcs.android.activity.account

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.User
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils

class ChangePasswordPresenter(viewInstance: ChangePasswordView, services: RetrofitServices) :
    ServicesPresenter<ChangePasswordView?>(viewInstance, services) {

    fun changePassword(oldPassword: String, newPassword: String) {
        getService(UserService::class.java).changePassword(oldPassword, newPassword, newPassword)
            .enqueue(object : AuthCallback<Void?>(this) {
                override fun onResponseSuccessful(response: Void?) {
                    view?.onPasswordChanged();
                }

                override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                    view?.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(throwable: Throwable) {
                    runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
                }
            })
    }
}