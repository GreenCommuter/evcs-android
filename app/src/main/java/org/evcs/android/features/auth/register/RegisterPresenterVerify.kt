package org.evcs.android.features.auth.register

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.CodeWrapper
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.UserService
import org.evcs.android.util.ErrorUtils

class RegisterPresenterVerify(viewInstance: RegisterViewVerify, services: RetrofitServices) :
    RegisterPresenterCellphone(viewInstance, services) {

    fun sendCode(code : String) {
        getService(UserService::class.java).sendCode(CodeWrapper(code))
            .enqueue(object : AuthCallback<Void>(this) {
                override fun onResponseSuccessful(p0: Void?) {
                    (view as RegisterViewVerify).onCellphoneVerified()
                }

                override fun onResponseFailed(responseBody: ResponseBody?, p1: Int) {
                    view?.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(p0: Throwable?) {
                    runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
                }
            })
    }

}