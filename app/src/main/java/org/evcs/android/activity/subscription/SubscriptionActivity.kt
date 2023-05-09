package org.evcs.android.activity.subscription

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.activity.CancelPlanActivity
import org.evcs.android.databinding.ActivitySubscriptionBinding
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.model.SubscriptionStatus
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils.setVisibility
import org.joda.time.format.DateTimeFormat

class SubscriptionActivity : BaseActivity2() {

    private lateinit var mLauncher: ActivityResultLauncher<Intent>
    private lateinit var mBinding: ActivitySubscriptionBinding

    override fun init() {
        mLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    result -> if (result.resultCode == RESULT_OK) showCancellationDialog()
                }
    }

    override fun populate() {
        onSubscriptionPlanRetrieved(UserUtils.getLoggedUser().activeSubscription!!)
    }

    fun onSubscriptionPlanRetrieved(response: SubscriptionStatus) {
        mBinding.activitySubscriptionsConsumption.text =
            String.format("%d/%s kWh consumed this month", response.kwhUsage, response.printTotalKwh())
        if (response.remainingKwh != null) {
            mBinding.activitySubscriptionsProgress.progress = response.kwhUsage
            mBinding.activitySubscriptionsProgress.max = response.totalKwh
        }
        val format = DateTimeFormat.forPattern("MM/dd/yyyy")
        mBinding.activitySubscriptionsEnrolled.setText(format.print(response.activeSince))
        if (true /*is on free trial*/) {
            mBinding.activitySubscriptionsFreeTrial.visibility = View.VISIBLE
        }
//        mBinding.activitySubscriptionsPaymentDetails
    }

    override fun setListeners() {
        mBinding.activitySubscriptionsCancel.setOnClickListener {
            mLauncher.launch(Intent(this, CancelPlanActivity::class.java))
        }
        mBinding.activitySubscriptionsViewAllPlans.setOnClickListener {  }
        mBinding.activitySubscriptionsChangePaymentMethod.setOnClickListener {  }
        mBinding.activitySubscriptionsToolbar.setNavigationOnClickListener { finish() }
    }

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivitySubscriptionBinding.inflate(layoutInflater)
        return mBinding.root
    }

    private fun showCancellationDialog() {
        EVCSDialogFragment.Builder()
                .setTitle("Your %s subscription has been canceled.")
                .setSubtitle("You will still receive discounted “Pay As You Go” pricing with when this billing cycle ends.\n\n" +
                        "You can always reactivate the plan if you change your mind.")
                .showCancel(true)
    }

}
