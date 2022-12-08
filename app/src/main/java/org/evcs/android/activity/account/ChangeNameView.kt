package org.evcs.android.activity.account

import org.evcs.android.ui.view.shared.IErrorView

interface ChangeNameView : IErrorView {
    fun onNameUpdate()
}