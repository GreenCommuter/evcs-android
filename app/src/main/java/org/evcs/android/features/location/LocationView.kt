package org.evcs.android.features.location

import org.evcs.android.model.Location
import org.evcs.android.ui.view.shared.IErrorView

interface LocationView : IErrorView {
    fun showLocation(response: Location?)
}
