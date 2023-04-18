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


}
