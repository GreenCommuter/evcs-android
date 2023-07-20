package org.evcs.android.features.charging

import org.evcs.android.model.Location

interface ChargingInProgressView : PreChargingView {
    fun sessionStopped()
    fun onLocationRetrieved(location: Location)
}