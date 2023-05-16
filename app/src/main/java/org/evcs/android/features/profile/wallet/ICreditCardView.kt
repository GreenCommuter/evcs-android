package org.evcs.android.features.profile.wallet

import org.evcs.android.model.PaymentMethod
import org.evcs.android.ui.view.shared.IErrorView

interface ICreditCardView : IErrorView {

    fun onPaymentMethodRemoved(item: PaymentMethod)

    fun onDefaultPaymentMethodSet(item: PaymentMethod)

}
