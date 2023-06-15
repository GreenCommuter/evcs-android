package org.evcs.android.features.map

import com.google.android.gms.maps.model.LatLngBounds
import org.evcs.android.model.Location
import org.evcs.android.ui.view.shared.IErrorView
import org.evcs.android.ui.view.shared.ILoadingView

interface IMainMapView : IErrorView {
    fun showLocations(response: List<Location>, viewport: LatLngBounds?)
    fun showInitialLocations(response: List<Location>, showInList: Boolean)
    fun onEmptyResponse()
}