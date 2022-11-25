package org.evcs.android.features.map

import com.base.maps.IMapPresenter
import com.base.networking.retrofit.RetrofitServices
import com.google.android.gms.maps.model.LatLng
import okhttp3.ResponseBody
import org.evcs.android.model.FilterState
import org.evcs.android.model.Location
import org.evcs.android.model.PaginatedResponse
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.LocationService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class MainMapPresenter(viewInstance: IMainMapView?, services: RetrofitServices?) :
    ServicesPresenter<IMainMapView?>(viewInstance, services), IMapPresenter {

    var mFilterState: FilterState = FilterState()
    var mLastLocation: LatLng? = null

    /**
     * Returns the current logged user from API
     *
     * @return Current logged user
     */
    fun getLoggedUser() {

    }

    override fun onMapReady() {
        view.onMapReady()
    }

    override fun onMapDestroyed() {}

    fun getLocations(latlng: LatLng?, minKw: Int?, connector: String?) {
        view?.showLoading()
        getService(LocationService::class.java).getLocations(1, latlng?.latitude, latlng?.longitude, minKw, connector)
            .enqueue(object : AuthCallback<PaginatedResponse<Location?>?>(this) {
                override fun onResponseSuccessful(response: PaginatedResponse<Location?>?) {
                    view.showLocations(response?.page)
                }

                override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                    view.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(t: Throwable) {
                    view.showError(RequestError.getNetworkError())
                }
            })
    }

    fun getLocations(latlng : LatLng? = mLastLocation) {
        getLocations(latlng, mFilterState.minKw, mFilterState.connectorType?.name?.lowercase());
    }

    fun onFilterResult(result: FilterState) {
        mFilterState = result
        getLocations()
    }

}