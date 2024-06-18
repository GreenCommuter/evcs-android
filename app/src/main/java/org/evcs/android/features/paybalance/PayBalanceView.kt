package org.evcs.android.features.paybalance

import org.evcs.android.model.Payment
import org.evcs.android.ui.view.shared.IErrorView

interface PayBalanceView : IErrorView {
    fun onPaymentSuccess()
    fun showPendingPayment(first: Payment)
}
