package org.evcs.android.features.charging

import com.base.networking.retrofit.RetrofitServices
import com.base.networking.utils.NetworkCodes
import okhttp3.ResponseBody
import org.evcs.android.model.Charge
import org.evcs.android.model.Session
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.ChargesService
import org.evcs.android.network.service.CommandsService
import org.evcs.android.network.service.presenter.PollingManager
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChargingInProgressPresenter(viewInstance: ChargingInProgressView?, services: RetrofitServices?) :
    ServicesPresenter<ChargingInProgressView?>(viewInstance, services) {

    private val LOCATION_KEY = "location"

    fun stopSession(sessionId: String) {
        getService(CommandsService::class.java).stopSession(sessionId).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                val headers = response!!.headers()
                val url = headers[LOCATION_KEY]
                if (url != null) {
                    this@ChargingInProgressPresenter.pollStopSession(url)
                } else {
                    view.showError(ErrorUtils.getError(response.errorBody()))
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                view.showError(RequestError.getNetworkError())
            }
        })
    }

    private fun pollStopSession(url: String) {
        PollingManager(this).poll(getService(CommandsService::class.java).startSession(url), object : PollingManager.PollingCallback {
            override fun onResponseSuccessful(response: Response<*>?) {
            }

            override fun onResponseFailed(responseBody: ResponseBody?, responseCode: Int) {
                view.showError(ErrorUtils.getError(responseBody!!))
            }

            override fun onCallFailure() {
                runIfViewCreated(Runnable { view.showError(RequestError.getNetworkError()) })
            }

            override fun shouldRetry(response: Response<*>): Boolean {
                return response.code() == NetworkCodes.ACCEPTED
            }
        })
    }

    fun getCurrentCharge() {
        getService(ChargesService::class.java).current.enqueue(object : AuthCallback<Session>(this) {
            override fun onResponseSuccessful(response: Session?) {
                view?.onChargeRetrieved(response!!)
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