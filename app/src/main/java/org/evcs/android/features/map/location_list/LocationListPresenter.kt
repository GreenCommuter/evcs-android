package org.evcs.android.features.map.location_list

import com.base.networking.retrofit.RetrofitServices
import org.evcs.android.network.service.presenter.ServicesPresenter

class LocationListPresenter(viewInstance: LocationListView, services: RetrofitServices) :
    ServicesPresenter<LocationListView>(viewInstance, services) {


}
