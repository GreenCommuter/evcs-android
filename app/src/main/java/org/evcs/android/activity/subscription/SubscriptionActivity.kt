package org.evcs.android.activity.subscription

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivitySubscriptionBinding
import org.evcs.android.model.PaginatedResponse
import org.evcs.android.model.SubscriptionStatus

class SubscriptionActivity : BaseActivity2() {

    private lateinit var mBinding: ActivitySubscriptionBinding

    override fun init() {
    }

    override fun populate() {

    }

    fun onSubscriptionPlanRetrieved(response: SubscriptionStatus) {
        mBinding.activitySubscriptionsConsumption.text =
            String.format("%d/%s kWh consumed this month", response.kwhUsage, response.printTotalKwh())
        if (response.remainingKwh != null) {
            mBinding.activitySubscriptionsProgress.progress = response.kwhUsage
            mBinding.activitySubscriptionsProgress.max = response.totalKwh
        }
//        mBinding.activitySubscriptionsEnrolled
//        mBinding.activitySubscriptionsFreeTrial
//        mBinding.activitySubscriptionsPaymentDetails
    }

    override fun setListeners() {
        mBinding.activitySubscriptionsCancel.setOnClickListener {  }
        mBinding.activitySubscriptionsViewAllPlans.setOnClickListener {  }
        mBinding.activitySubscriptionsChangePaymentMethod.setOnClickListener {  }
        mBinding.activitySubscriptionsToolbar.setNavigationOnClickListener { finish() }
    }

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivitySubscriptionBinding.inflate(layoutInflater)
        return mBinding.root
    }

}
