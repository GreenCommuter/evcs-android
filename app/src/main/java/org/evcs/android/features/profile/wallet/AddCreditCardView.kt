package org.evcs.android.features.profile.wallet

import org.evcs.android.ui.view.shared.IErrorView

interface AddCreditCardView : IErrorView {
    fun onMakeDefaultFinished()
}