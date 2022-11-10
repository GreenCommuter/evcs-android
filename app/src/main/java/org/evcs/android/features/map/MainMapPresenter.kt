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

class MainMapPresenter(viewInstance: IMainMapView?, services: RetrofitServices?) :
    ServicesPresenter<IMainMapView?>(viewInstance, services), IMapPresenter {

    /**
     * Returns the current logged user from API
     *
     * @return Current logged user
     */
    fun getLoggedUser() {

    }

    override fun onMapReady() {
        getLocations()
    }
    override fun onMapDestroyed() {}

    fun getLocations(latlng: LatLng?, minKw: Int?, connector: Array<ConnectorType>?) {
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
        getLocations(null, null, null);
    }

    fun onFilterResult(result: ActivityResult) {
        if (result.data == null) return
        val connectorTypes = result.data!!.getSerializableExtra("Connector Types")
        val minKw = result.data!!.getIntExtra("Min Kw", 0)
        getLocations(null, minKw, (connectorTypes as Array<ConnectorType>))
    }

}