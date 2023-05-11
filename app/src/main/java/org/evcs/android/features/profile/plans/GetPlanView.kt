package org.evcs.android.features.profile.plans

import org.evcs.android.ui.view.shared.IErrorView

interface GetPlanView : IErrorView {
    fun onSubscriptionSuccess(response: Void)
}
