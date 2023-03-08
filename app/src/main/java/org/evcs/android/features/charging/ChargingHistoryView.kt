package org.evcs.android.features.charging

import org.evcs.android.model.Charge
import org.evcs.android.ui.view.shared.IErrorView

interface ChargingHistoryView : IErrorView {
    fun showEmpty()
    fun showCharges(chargesList: List<Charge?>, pagesLeft: Boolean, onFirstPage: Boolean)
}
