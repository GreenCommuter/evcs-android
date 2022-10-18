package org.evcs.android.features.auth.register

import android.content.Context
import android.util.Log
import com.base.networking.retrofit.RetrofitServices
import com.google.android.gms.auth.api.phone.SmsRetriever
import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.CodeWrapper
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.MessageReceiver
import org.evcs.android.network.service.UserService
import org.evcs.android.util.ErrorUtils

class RegisterPresenterVerify(viewInstance: RegisterViewVerify, services: RetrofitServices) :
    RegisterPresenterCellphone<RegisterViewVerify>(viewInstance, services), MessageReceiver {

    fun sendCode(code : String) {
        getService(UserService::class.java).sendCode(CodeWrapper(code))
            .enqueue(object : AuthCallback<Void>(this) {
                override fun onResponseSuccessful(p0: Void?) {
                    view.onCellphoneVerified()
                }

                override fun onResponseFailed(responseBody: ResponseBody?, p1: Int) {
                    view?.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(p0: Throwable?) {
                    runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
                }
            })
    }

    fun startSMSListener(context : Context) {
        val client = SmsRetriever.getClient(context)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener {
            Log.e("startSMSListener", "Success")
        }
        task.addOnFailureListener {
            Log.e("startSMSListener", "Failure")
        }
    }

    override fun onReceive(s: String?) {
        //TODO: trimear
        view?.showCode(s)
        sendCode(s!!)
    }
}