package org.evcs.android.features.map

import com.base.networking.retrofit.RetrofitServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import okhttp3.ResponseBody
import org.evcs.android.BaseConfiguration
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

    private val MILES_LIMIT: Int = 200
    var mCachedLocations: List<Location>? = null
    var mFilterState: FilterState = FilterState()
    var mNameQuery: String? = null
    var mLastLocation: LatLng? = null

    /**
     * Returns the current logged user from API
     *
     * @return Current logged user
     */
    fun getLoggedUser() {

    }

    /**
     * There are two ways to search here. If there's no query, I'm bringing every location that
     * matches the filters, and using the user's location (if I have it) just for ordering the list
     * If there's a query, I already have all the icons on the map, so I'm restricting the search
     * to a distance radius.
     */
    fun getInitialLocations(latlng: LatLng?, comingSoon: Boolean?, minKw: Int?, connector: Array<String>) {
        getService(LocationService::class.java).getLocations(latlng?.latitude, latlng?.longitude, comingSoon, minKw, connector)
            .enqueue(object : AuthCallback<PaginatedResponse<Location>?>(this) {
                override fun onResponseSuccessful(response: PaginatedResponse<Location>?) {
                    mCachedLocations = response?.page
                    if (mCachedLocations!!.isEmpty() && latlng?.latitude != BaseConfiguration.Map.DEFAULT_LATITUDE)
                        retryInitialLocations(comingSoon, minKw, connector)
                    else view.showInitialLocations(populateDistances(latlng, mCachedLocations!!), latlng != null)
                }

                override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                    view.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(t: Throwable) {
                    view.showError(RequestError.getNetworkError())
                }
            })
    }

    /**
     * It seems that there's a 3000-something mile cap when searching, so this is mostly for me when
     * I test this from Argentina without mocking my location
     */
    private fun retryInitialLocations(comingSoon: Boolean?, minKw: Int?, connector: Array<String>) {
        getInitialLocations(LatLng(BaseConfiguration.Map.DEFAULT_LATITUDE, BaseConfiguration.Map.DEFAULT_LONGITUDE), comingSoon, minKw, connector)
    }

    fun populateDistances(origin: LatLng?, locations: List<Location>): List<Location> {
        locations.forEach { location ->
            location.distance =
                if (origin != null) LocationUtils.distance(location.latLng, origin) else null
        }
        return locations
    }

    fun getInitialLocations(latlng : LatLng? = null) {
        getInitialLocations(latlng, mFilterState.comingSoon, mFilterState.minKw,
                mFilterState.getConnectorTypes());
    }

    fun onFilterResult(result: FilterState) {
        mFilterState = result
        getLocationsWithFilters()
    }

    fun getLocationsWithFilters() {
        getService(LocationService::class.java).getLocations(mNameQuery, mLastLocation?.latitude, mLastLocation?.longitude, MILES_LIMIT,
                mFilterState.comingSoon, mFilterState.minKw, mFilterState.getConnectorTypes())
                .enqueue(object : AuthCallback<PaginatedResponse<Location>?>(this) {
                    override fun onResponseSuccessful(response: PaginatedResponse<Location>?) {
                        if (response?.page?.isEmpty() == true) {
                            view.onEmptyResponse()
                            return
                        }
                        mCachedLocations = response?.page
//                        if (mCachedLocations!!.isEmpty() && latlng?.latitude != BaseConfiguration.Map.DEFAULT_LATITUDE)
//                            retryInitialLocations(comingSoon, minKw, connector)
                        view.showFilterResult(populateDistances(mLastLocation, mCachedLocations!!), mLastLocation != null || !mFilterState.isEmpty())
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                        view.showError(ErrorUtils.getError(responseBody))
                    }

                    override fun onCallFailure(t: Throwable) {
                        view.showError(RequestError.getNetworkError())
                    }
                })
    }

    fun searchByName(query: String) {
        mNameQuery = query
        getLocationsWithFilters()
    }

//    fun searchFromQuery(latlng: LatLng, viewport: LatLngBounds?) {
//        mLastLocation = latlng
//        searchFromQuery(viewport)
//    }

    /**
     * @param viewport: bounds for the map to show the locations
     */
    fun searchFromQuery(latlng: LatLng, viewport: LatLngBounds?) {
        mNameQuery = null
        getService(LocationService::class.java).getLocations(null, latlng.latitude,
                latlng.longitude, MILES_LIMIT, mFilterState.comingSoon, mFilterState.minKw,
                mFilterState.getConnectorTypes())
                .enqueue(getLocationSearchCallback(viewport))
5    }

    fun getLocationSearchCallback(viewport: LatLngBounds? = null): AuthCallback<PaginatedResponse<Location>?> {
        return object : AuthCallback<PaginatedResponse<Location>?>(this) {
            override fun onResponseSuccessful(response: PaginatedResponse<Location>?) {
                val locationList: List<Location>? = response!!.page
                if (locationList!!.size == 0) {
                    view.onEmptyResponse()
                } else {
                    view.showLocations(locationList, viewport ?: getViewport(locationList))
                }
            }

            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                view.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable) {
                view.showError(RequestError.getNetworkError())
            }
        }

    }

    private fun getViewport(mapItems: List<Location>): LatLngBounds {
        val boundsBuilder = LatLngBounds.builder()
        for (mapItem in mapItems) {
            LocationUtils.addDiagonal(boundsBuilder, mapItem.latLng)
        }
        return boundsBuilder.build()
    }

    fun onLocationRemoved() {
        if (mNameQuery != null) {
            getInitialLocations(mLastLocation)
        }
        mNameQuery = null
    }

}