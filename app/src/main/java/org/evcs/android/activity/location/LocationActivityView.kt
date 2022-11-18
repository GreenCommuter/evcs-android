package org.evcs.android.activity.location

import org.evcs.android.model.Location
import org.evcs.android.ui.view.shared.IErrorView

interface LocationActivityView : IErrorView {
    fun showLocation(response: Location?)
}
