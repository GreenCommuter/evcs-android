package org.evcs.android.features.profile

import org.evcs.android.model.user.User
import org.evcs.android.ui.view.shared.IErrorView

interface ProfileView : IErrorView {
    fun onUserRefreshed(response: User)
}
