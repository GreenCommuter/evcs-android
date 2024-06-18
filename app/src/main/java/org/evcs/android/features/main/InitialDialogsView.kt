package org.evcs.android.features.main

import org.evcs.android.model.Subscription
import org.evcs.android.ui.view.shared.IErrorView

interface InitialDialogsView : IErrorView {
    fun onPendingCancelation(previousSubscription: Subscription)
    fun onConfirmCancelation()
    fun showAccountSuspendedDialog()
}
