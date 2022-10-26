package org.evcs.android.features.map

import org.evcs.android.model.Location
import org.evcs.android.ui.view.shared.IErrorView

interface IMainMapView : IErrorView {
    fun showLocations(response: List<Location?>?)
}