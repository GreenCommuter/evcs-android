package org.evcs.android.features.auth.forgotPassword

import org.evcs.android.ui.view.shared.IErrorView

interface ForgotPasswordView : IErrorView {
    fun onResetRequest()
}