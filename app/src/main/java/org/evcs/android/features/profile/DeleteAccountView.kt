package org.evcs.android.features.profile

import org.evcs.android.ui.view.shared.IErrorView

interface DeleteAccountView : IErrorView {
    fun onAccountDeleted()
    fun showConfirmDialog()
    fun showPaymentIssue()
}
