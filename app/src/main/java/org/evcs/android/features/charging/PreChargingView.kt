package org.evcs.android.features.charging

import org.evcs.android.model.Session
import org.evcs.android.ui.view.shared.IErrorView

interface PreChargingView : IErrorView {
    fun onChargeRetrieved(response: Session?)
}
