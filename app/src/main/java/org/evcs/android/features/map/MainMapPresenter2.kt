package org.evcs.android.features.map

import com.base.maps.IMapPresenter
import com.base.networking.retrofit.RetrofitServices
import org.evcs.android.network.service.presenter.ServicesPresenter

class MainMapPresenter2(viewInstance: IMainMapView2, services: RetrofitServices?) :
        ServicesPresenter<IMainMapView2>(viewInstance, services), IMapPresenter {

    override fun onMapReady() {
        view.onMapReady()
    }

    override fun onMapDestroyed() {}

}