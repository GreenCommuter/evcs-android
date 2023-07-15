package org.evcs.android.features.profile.plans

import org.evcs.android.model.Plan
import org.evcs.android.ui.view.shared.IErrorView
import java.util.ArrayList

interface PlansView : IErrorView {
    fun showPlans(response: List<Plan>)
}
