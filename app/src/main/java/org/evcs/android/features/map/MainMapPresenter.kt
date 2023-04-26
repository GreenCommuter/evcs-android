package org.evcs.android.features.map

import com.base.networking.retrofit.RetrofitServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import okhttp3.ResponseBody
import org.evcs.android.model.FilterState
import org.evcs.android.model.Location
import org.evcs.android.model.PaginatedResponse
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.LocationService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.LocationUtils

class MainMapPresenter(viewInstance: IMainMapView?, services: RetrofitServices?) :
    ServicesPresenter<IMainMapView?>(viewInstance, services) {

    var mFilterState: FilterState = FilterState()
    var mLastLocation: LatLng? = null

    /**
     * Returns the current logged user from API
     *
     * @return Current logged user
     */
    fun getLoggedUser() {

    }

    fun getInitialLocations(latlng: LatLng?, minKw: Int?, connector: String?) {
        view?.showLoading()
        getService(LocationService::class.java).getLocations(1, latlng?.latitude, latlng?.longitude, minKw, connector)
            .enqueue(object : AuthCallback<PaginatedResponse<Location?>?>(this) {
                override fun onResponseSuccessful(response: PaginatedResponse<Location?>?) {
                    view.showInitialLocations(populateDistances(response?.page!!), latlng == null)
                }

                override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                    view.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(t: Throwable) {
                    view.showError(RequestError.getNetworkError())
                }
            })
    }

    fun populateDistances(locations: List<Location?>): List<Location?> {
        locations.forEach { location ->
            location!!.distance =
                if (mLastLocation != null) LocationUtils.distance(location.latLng, mLastLocation) else null
        }
        return locations
    }

    fun getInitialLocations(latlng : LatLng? = null) {
        getInitialLocations(latlng, mFilterState.minKw, mFilterState.connectorType?.name?.lowercase());
    }

    fun onFilterResult(result: FilterState) {
        mFilterState = result
        getInitialLocations()
    }

    fun searchFromQuery(latlng: LatLng, viewport: LatLngBounds?) {
        mLastLocation = latlng
        searchFromQuery(viewport)
    }

    /**
     * @param viewport: bounds for the map to show the locations
     */
    fun searchFromQuery(viewport: LatLngBounds?) {
        getService(LocationService::class.java).getLocations(mLastLocation?.latitude,
                mLastLocation?.longitude, 200, mFilterState.minKw, mFilterState.connectorType?.name?.lowercase())
                .enqueue(object : AuthCallback<PaginatedResponse<Location?>?>(this) {
                    override fun onResponseSuccessful(response: PaginatedResponse<Location?>?) {
                        val locationList: List<Location?>? = response!!.page
                        if (locationList!!.size == 0) {
                            view.onEmptyResponse()
                        } else {
                            view.showLocations(locationList, viewport)
                        }
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