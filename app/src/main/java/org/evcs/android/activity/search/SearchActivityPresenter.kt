package org.evcs.android.activity.search

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

class SearchActivityPresenter(viewInstance: SearchActivityView, services: RetrofitServices) :
    ServicesPresenter<SearchActivityView>(viewInstance, services) {

    lateinit var mFilterState: FilterState
    private lateinit var mLatlng: LatLng

    fun search(latlng: LatLng, viewport: LatLngBounds?) {
        mLatlng = latlng
        search(viewport)
    }

    /**
     * @param viewport: bounds for the map to show the locations
     */
    fun search(viewport: LatLngBounds?) {
        getService(LocationService::class.java).getLocations(mLatlng?.latitude,
            mLatlng?.longitude, 200, mFilterState.minKw, mFilterState.connectorType?.name?.lowercase())
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
