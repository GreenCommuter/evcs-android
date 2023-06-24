package org.evcs.android.features.profile.sessioninformation

import org.evcs.android.model.Charge
import org.evcs.android.ui.view.shared.IErrorView

interface ISessionInformationView : IErrorView {
    fun showCharge(response: Charge)
}
