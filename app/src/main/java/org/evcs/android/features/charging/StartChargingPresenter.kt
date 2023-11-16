package org.evcs.android.features.charging

import com.base.networking.retrofit.RetrofitServices
import com.base.networking.utils.NetworkCodes
import okhttp3.ResponseBody
import org.evcs.android.model.Station
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.service.CommandsService
import org.evcs.android.network.service.presenter.PollingManager
import org.evcs.android.util.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartChargingPresenter(viewInstance: StartChargingView, services: RetrofitServices?,
        val mStationId: Int, val mPmId: String?, val mCoupons: ArrayList<String>?) :
        PreChargingPresenter<StartChargingView>(viewInstance, services) {

    private lateinit var mStation: Station
    private val LOCATION_KEY = "location"

    fun startSession() {
        getService(CommandsService::class.java).startSession(mStationId, mPmId, mCoupons)
                .enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                val headers = response!!.headers()
                val url = headers[LOCATION_KEY]
                if (url != null) {
                    startSession(url)
                } else {
                    view.showError(ErrorUtils.getError(response.errorBody()))
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                view.showError(RequestError.getNetworkError())
            }
        })
    }

    private fun startSession(url: String) {
        PollingManager(this).poll(getService(CommandsService::class.java).startSession(url), object : PollingManager.PollingCallback {
            override fun onResponseSuccessful(response: Response<*>?) {
                view.onSessionStarted()
            }

            override fun onResponseFailed(responseBody: ResponseBody?, responseCode: Int) {
                runIfViewCreated(Runnable { view.showErrorDialog(ErrorUtils.getError(responseBody!!)) })
            }

            override fun onCallFailure() {
                runIfViewCreated(Runnable { view.showError(RequestError.getNetworkError()) })
            }

            override fun shouldRetry(response: Response<*>): Boolean {
                return response.code() == NetworkCodes.ACCEPTED
            }
        })
    }

}