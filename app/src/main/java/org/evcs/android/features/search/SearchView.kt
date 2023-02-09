package org.evcs.android.features.search

import com.google.android.gms.maps.model.LatLngBounds
import org.evcs.android.model.Location
import org.evcs.android.ui.view.shared.IErrorView
import org.evcs.android.ui.view.shared.ILoadingView

interface SearchView : IErrorView, ILoadingView {
    fun showLocations(page: List<Location?>?, viewport: LatLngBounds?)
    fun onEmptyResponse();
}
