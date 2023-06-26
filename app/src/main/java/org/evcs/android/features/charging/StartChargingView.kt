package org.evcs.android.features.charging

import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.view.shared.IErrorView

interface StartChargingView : IErrorView {
    fun onSessionStarted()
    fun showErrorDialog(error: RequestError)
}