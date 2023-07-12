package org.evcs.android.features.charging

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.Session
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.ChargesService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

open class ChargingTabPresenter<T : ChargingTabView>(viewInstance: T, services: RetrofitServices?) :
    ServicesPresenter<T>(viewInstance, services) {

    fun getCurrentCharge() {
        getService(ChargesService::class.java).current.enqueue(object : AuthCallback<Session>(this) {
            override fun onResponseSuccessful(response: Session?) {
                view?.onChargeRetrieved(response)
            }

            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                view?.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable?) {
                runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
            }

        })
    }

}
