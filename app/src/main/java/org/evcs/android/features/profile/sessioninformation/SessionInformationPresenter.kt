package org.evcs.android.features.profile.sessioninformation

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.Charge
import org.evcs.android.model.PaginatedResponse
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.ChargesService
import org.evcs.android.network.service.presenter.PollingManager
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import retrofit2.Response

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

    fun getChargeFromSession(sessionId: Int) {
        val call = getService(ChargesService::class.java).getChargeFromSession(sessionId)
        PollingManager(this).poll(call, object : PollingManager.PollingCallback {
            override fun onResponseSuccessful(response: Response<*>) {
                view?.showCharge(handleResponse(response)[0])
            }

            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure() {
                view.showError(RequestError.getNetworkError())
            }

            override fun shouldRetry(response: Response<*>): Boolean {
                return handleResponse(response).isEmpty()
            }
        })
    }

    private fun handleResponse(response: Response<*>): List<Charge> {
        return (response.body() as PaginatedResponse<Charge>).page!!
    }

}
