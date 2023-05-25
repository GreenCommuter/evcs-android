package org.evcs.android.features.map.location

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.LocationService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class LocationPresenter(viewInstance: ILocationView, services: RetrofitServices) :
    ServicesPresenter<ILocationView>(viewInstance, services) {

    var mLocation: Location? = null

    fun getLocation(id: Int) {
        getService(LocationService::class.java).getLocation(id)
            .enqueue(object : AuthCallback<Location?>(this) {
                override fun onResponseSuccessful(response: Location?) {
                    mLocation = response
                    view.showLocation(response)
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
