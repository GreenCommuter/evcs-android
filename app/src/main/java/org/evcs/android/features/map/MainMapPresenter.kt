package org.evcs.android.features.map

import androidx.activity.result.ActivityResult
import com.base.maps.IMapPresenter
import com.base.networking.retrofit.RetrofitServices
import com.google.android.gms.maps.model.LatLng
import okhttp3.ResponseBody
import org.evcs.android.model.ConnectorType
import org.evcs.android.model.Location
import org.evcs.android.model.PaginatedResponse
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.LocationService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.Extras

class MainMapPresenter(viewInstance: IMainMapView?, services: RetrofitServices?) :
    ServicesPresenter<IMainMapView?>(viewInstance, services), IMapPresenter {

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

    fun getLocations(latlng: LatLng?, minKw: Int?, connector: Array<ConnectorType>?) {
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

    fun getLocations() {
        getLocations(mLastLocation, null, null);
    }

    fun onFilterResult(result: ActivityResult) {
        if (result.data == null) return
        view?.showLoading()
        val connectorTypes = result.data!!.getSerializableExtra(Extras.FilterActivity.CONNECTOR_TYPES)
        val minKw = result.data!!.getIntExtra(Extras.FilterActivity.MIN_KW, 0)
        getLocations(mLastLocation, minKw, (connectorTypes as Array<ConnectorType>))
    }

}