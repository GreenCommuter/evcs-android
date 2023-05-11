package org.evcs.android.activity.subscription

import org.evcs.android.model.SubscriptionStatus

interface SubscriptionActivityView {
    fun onSubscriptionPlanRetrieved(currentSubscription: SubscriptionStatus)
}