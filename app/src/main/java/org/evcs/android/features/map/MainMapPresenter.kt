package org.evcs.android.features.map

import com.base.maps.IMapPresenter
import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.Location
import org.evcs.android.model.PaginatedResponse
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.LocationService
import org.evcs.android.network.service.presenter.MultipleRequestsManager
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.PaginationState

class MainMapPresenter(viewInstance: IMainMapView?, services: RetrofitServices?) :
    ServicesPresenter<IMainMapView?>(viewInstance, services), IMapPresenter {

    private val mPaginationState = PaginationState()

    private val mMultipleRequestsManager: MultipleRequestsManager

    init {
        mMultipleRequestsManager = MultipleRequestsManager(this)
    }

    /**
     * Returns the current logged user from API
     *
     * @return Current logged user
     */
    fun getLoggedUser() {

    }

    override fun onMapReady() {}
    override fun onMapDestroyed() {}

    fun getLocations() {
            getService(LocationService::class.java).getLocations()
                .enqueue(object : AuthCallback<PaginatedResponse<Location?>?>(this) {
                    override fun onResponseSuccessful(response: PaginatedResponse<Location?>?) {
                        mPaginationState.updateTotal(response!!.totalPages)
                        view.showLocations(response.page)
                        mPaginationState.advancePage()
                        if (mPaginationState.pagesLeft()) {
                            getLocations()
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