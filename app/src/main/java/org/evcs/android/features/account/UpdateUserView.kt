package org.evcs.android.features.account

import org.evcs.android.ui.view.shared.IErrorView

interface UpdateUserView : IErrorView {
    fun onUserUpdate()
}