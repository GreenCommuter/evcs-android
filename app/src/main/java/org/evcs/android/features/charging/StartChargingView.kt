package org.evcs.android.features.charging

import org.evcs.android.model.shared.RequestError

interface StartChargingView : PreChargingView {
    fun onSessionStarted()
    fun showErrorDialog(error: RequestError)
}