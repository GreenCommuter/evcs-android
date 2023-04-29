package org.evcs.android.features.map.location_list

import com.google.android.gms.maps.model.LatLngBounds
import org.evcs.android.model.Location
import org.evcs.android.ui.view.shared.IErrorView

interface LocationListView : IErrorView {
    fun showLocations(page: List<Location?>?, viewport: LatLngBounds?)
    fun onEmptyResponse();
}
