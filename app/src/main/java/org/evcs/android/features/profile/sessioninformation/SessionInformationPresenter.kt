package org.evcs.android.features.profile.sessioninformation

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.Charge
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.ChargesService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class SessionInformationPresenter(viewInstance: ISessionInformationView, services: RetrofitServices) :
    ServicesPresenter<ISessionInformationView>(viewInstance, services) {

    fun getCharge(id: Int) {
        getService(ChargesService::class.java).getCharge(id)
            .enqueue(object : AuthCallback<Charge>(this) {
                override fun onResponseSuccessful(response: Charge) {
                    view.showCharge(response)
                }

                override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                    view.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(t: Throwable) {
                    view.showError(RequestError.getNetworkError())
                }
            })
    }

}
