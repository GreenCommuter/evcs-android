package org.evcs.android.features.charging

import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError

interface ChargingInProgressView : PreChargingView {
    fun sessionStopped()
    fun onLocationRetrieved(location: Location)
    fun showChargeError(error: RequestError)
}