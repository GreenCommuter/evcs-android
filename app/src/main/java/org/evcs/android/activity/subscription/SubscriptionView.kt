package org.evcs.android.activity.subscription

import org.evcs.android.model.SubscriptionStatus
import org.evcs.android.ui.view.shared.IErrorView

interface SubscriptionView : IErrorView {
    fun onSubscriptionPlanRetrieved(currentSubscription: SubscriptionStatus)
}