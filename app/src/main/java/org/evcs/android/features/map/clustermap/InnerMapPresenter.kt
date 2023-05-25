package org.evcs.android.features.map.clustermap

import com.base.maps.IMapPresenter
import com.base.networking.retrofit.RetrofitServices
import com.google.android.gms.maps.model.LatLng
import org.evcs.android.network.service.presenter.ServicesPresenter

class InnerMapPresenter(viewInstance: InnerMapView, services: RetrofitServices?) :
        ServicesPresenter<InnerMapView>(viewInstance, services), IMapPresenter {

    var mLastLocation: LatLng? = null

    override fun onMapReady() {
        view.onMapReady()
    }

    override fun onMapDestroyed() {}

}