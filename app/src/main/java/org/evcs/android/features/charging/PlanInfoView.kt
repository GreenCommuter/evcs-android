package org.evcs.android.features.charging

import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.Station
import org.evcs.android.model.SubscriptionStatus
import org.evcs.android.ui.view.shared.IErrorView

interface PlanInfoView : IErrorView {
//    fun onStatusRetrieved(response: SubscriptionStatusWrapper?)
//    fun onStationRetrieved(response: Station)
    fun showFree(freeChargingCode: String)
    fun show(station: Station, status: SubscriptionStatus?, hasRejectedPayments: Boolean)
    fun showDefaultPM(paymentMethod: PaymentMethod?)
    fun showStationNotFound()
}
