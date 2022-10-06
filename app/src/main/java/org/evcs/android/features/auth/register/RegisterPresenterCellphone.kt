package org.evcs.android.features.auth.register

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.PhoneWrapper
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

open class RegisterPresenterCellphone(viewInstance: RegisterViewCellphone, services: RetrofitServices) :
    ServicesPresenter<RegisterViewCellphone?>(viewInstance, services) {

    fun sendNumbertoVerify(number: String) {
        getService(UserService::class.java).sendPhoneToVerify(PhoneWrapper(number))
            .enqueue(object : AuthCallback<Void>(this) {
                override fun onResponseSuccessful(p0: Void?) {
                    view.onCellphoneSent()
                }

                override fun onResponseFailed(responseBody: ResponseBody?, p1: Int) {
                    view.onCellphoneSent()

                    view?.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(p0: Throwable?) {
                    runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
                }
            })
    }

}