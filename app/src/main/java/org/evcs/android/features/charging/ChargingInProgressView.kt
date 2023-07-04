package org.evcs.android.features.charging

import org.evcs.android.model.Location

interface ChargingInProgressView : ChargingTabView {
    fun sessionStopped()
    fun onLocationRetrieved(location: Location)
}