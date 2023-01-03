package org.evcs.android.activity.payment

import org.evcs.android.model.PaymentMethod
import org.evcs.android.ui.view.shared.IErrorView

interface PaymentActivityView : IErrorView {
    fun showPaymentMethods(response: List<PaymentMethod>?)
}
