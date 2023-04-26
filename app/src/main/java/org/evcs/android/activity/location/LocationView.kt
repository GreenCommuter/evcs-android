package org.evcs.android.activity.location

import org.evcs.android.model.Location
import org.evcs.android.ui.view.shared.IErrorView

interface LocationView : IErrorView {
    fun showLocation(response: Location?)
}
