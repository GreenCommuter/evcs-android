package org.evcs.android.activity.location

import com.base.networking.retrofit.RetrofitServices
import com.google.android.gms.maps.model.LatLng
import okhttp3.ResponseBody
import org.evcs.android.model.Location
import org.evcs.android.model.PaginatedResponse
import org.evcs.android.model.Station
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.LocationService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.PaginationState

class LocationActivityPresenter(viewInstance: LocationActivityView, services: RetrofitServices) :
    ServicesPresenter<LocationActivityView>(viewInstance, services) {

    fun getLocation(id: Int) {
        getService(LocationService::class.java).getLocation(id)
            .enqueue(object : AuthCallback<Location?>(this) {
                override fun onResponseSuccessful(response: Location?) {
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

    fun pack(stations: List<Station>): ArrayList<List<Station>> {
//        val res = ArrayList<Triple<Station, Int, Int>>()
        val map = stations.groupBy { station -> station.chargerType }
        return ArrayList(map.values)
//        res.addAll(map.map { entry ->
//            Triple(entry.value.first(),
//                   entry.value.count { station -> station.availableStatus == "AVAILABLE" },
//                   entry.value.size)
//        })
//        return res
    }

    //TODO: use enum
    fun countAvailable(stations: List<Station>): Int {
        return stations.count { station -> station.availableStatus == "AVAILABLE" }
    }

}
