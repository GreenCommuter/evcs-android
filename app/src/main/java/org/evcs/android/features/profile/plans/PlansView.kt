package org.evcs.android.features.profile.plans

import org.evcs.android.model.PlanTab
import org.evcs.android.ui.view.shared.IErrorView
import kotlin.collections.ArrayList

interface PlansView : IErrorView {
    fun showPlans(response: ArrayList<PlanTab>)
    fun userHasHiddenPlan(url: String)
}
