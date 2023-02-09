package org.evcs.android.features.account

import org.evcs.android.ui.view.shared.IErrorView

interface ChangePasswordView : IErrorView {
    fun onPasswordChanged()
}