package org.evcs.android.activity.search
//
//import com.base.networking.retrofit.RetrofitServices
//import com.google.android.gms.maps.model.LatLng
//import okhttp3.ResponseBody
//import org.evcs.android.model.FilterState
//import org.evcs.android.model.Location
//import org.evcs.android.model.PaginatedResponse
//import org.evcs.android.model.shared.RequestError
//import org.evcs.android.network.callback.AuthCallback
//import org.evcs.android.network.service.LocationService
//import org.evcs.android.network.service.presenter.ServicesPresenter
//import org.evcs.android.util.ErrorUtils
//import org.evcs.android.util.PaginationState
//
//class SearchActivityPresenter(viewInstance: SearchActivityView, services: RetrofitServices) :
//    ServicesPresenter<SearchActivityView>(viewInstance, services) {
//
//    lateinit var mFilterState: FilterState
//    private lateinit var mLatlng: LatLng
//    var mPaginationState : PaginationState = PaginationState()
//
//    fun search(latlng: LatLng) {
//        mLatlng = latlng
//        search()
//    }
//
//    fun search() {
//        getService(LocationService::class.java).getLocations(mPaginationState.page, 20,
//            mLatlng?.latitude, mLatlng?.longitude)
//            .enqueue(object : AuthCallback<PaginatedResponse<Location?>?>(this) {
//                override fun onResponseSuccessful(response: PaginatedResponse<Location?>?) {
//                    mPaginationState.updateTotal(response!!.totalPages)
//                    val locationList: List<Location?>? = response.page
//                    if (locationList!!.size == 0 && mPaginationState.isOnFirstPage) {
//                        view.onEmptyResponse()
//                    } else {
//                        view.showLocations(
//                            locationList, mPaginationState.pagesLeft(), mPaginationState.isOnFirstPage)
//                    }
//                    mPaginationState.advancePage()
//                }
//
//                override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
//                    view.showError(ErrorUtils.getError(responseBody))
//                }
//
//                override fun onCallFailure(t: Throwable) {
//                    view.showError(RequestError.getNetworkError())
//                }
//            })
//    }
//
//}
