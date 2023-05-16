package org.evcs.android.activity.subscription

import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.base.core.util.NavigationUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.activity.CancelPlanActivity
import org.evcs.android.databinding.ActivitySubscriptionBinding
import org.evcs.android.features.profile.plans.PlansActivity
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.SubscriptionStatus
import org.evcs.android.util.UserUtils
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class SubscriptionActivity : BaseActivity2(), SubscriptionActivityView {

    private lateinit var mLongDateFormatter: DateTimeFormatter
    private lateinit var mPresenter: SubscriptionActivityPresenter
    private lateinit var mLauncher: ActivityResultLauncher<Intent>
    private lateinit var mChangePmLauncher: ActivityResultLauncher<Intent>
    private lateinit var mBinding: ActivitySubscriptionBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivitySubscriptionBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    result -> if (result.resultCode == RESULT_OK) showCancellationDialog()
                }
        mChangePmLauncher = WalletActivity.getDefaultLauncher(this,
                mBinding.activitySubscriptionsPaymentInfo)
        mLongDateFormatter = DateTimeFormat.forPattern("MMM dd, yyyy")
    }

    override fun populate() {
        createPresenter()
        mPresenter.refreshSubscription()
        onSubscriptionPlanRetrieved(UserUtils.getLoggedUser().activeSubscription!!)
    }

    fun createPresenter() {
        mPresenter = SubscriptionActivityPresenter(this, EVCSApplication.getInstance().retrofitServices)
        mPresenter.onViewCreated()
    }

    override fun onSubscriptionPlanRetrieved(response: SubscriptionStatus) {
        mBinding.activitySubscriptionsPlanName.text = response.planName
        mBinding.activitySubscriptionsPlanProgress.setPlan(response)
        val date = mLongDateFormatter.print(response.renewalDate)
        val defaultPm = PaymentMethod.getDefaultFromSharedPrefs()!!
        mBinding.activitySubscriptionsPaymentDetails.setText(String.format(
                getString(R.string.manage_plan_payment_details_format, defaultPm.card.brand.toPrintableString(),
                    defaultPm.card.last4, response.price, response.renewalPeriod, date)))
        mBinding.activitySubscriptionsPaymentInfo.setPaymentMethod(defaultPm)

        val format = DateTimeFormat.forPattern("MM/dd/yyyy")
        mBinding.activitySubscriptionsEnrolled.setText(format.print(response.activeSince))
        if (response.onTrialPeriod) {
            mBinding.activitySubscriptionsFreeTrial.visibility = View.VISIBLE
            mBinding.activitySubscriptionsFreeTrial.setText(
                    getString(R.string.manage_plan_days_remaining, response.activeDaysLeft))
        }
        if (response.status.equals(SubscriptionStatus.Status.CANCELED)) {
            populateCanceled(response)
        }
        mBinding.activitySubscriptionsPlanStatus.text = response.status.toString()
    }

    //TODO: save subscription to be able to show this
    private fun populateCanceled(response: SubscriptionStatus) {
        mBinding.managePlansCanceledLayout.visibility = View.VISIBLE
        mBinding.managePlansActiveLayout.visibility = View.GONE

        mBinding.activitySubscriptionsPaymentDetails.setText(String.format(
                getString(R.string.manage_plan_payment_details_format_canceled),
                mLongDateFormatter.print(response.renewalDate)))
        mBinding.activitySubscriptionsPlanStatus.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.evcs_danger_700))
    }

    override fun setListeners() {
        mBinding.activitySubscriptionsCancel.setOnClickListener {
            mLauncher.launch(Intent(this, CancelPlanActivity::class.java))
        }
        mBinding.activitySubscriptionsViewAllPlans.setOnClickListener { goToPlansActivity() }
        mBinding.activitySubscriptionsViewAllPlans2.setOnClickListener { goToPlansActivity() }
        mBinding.activitySubscriptionsActivate.setOnClickListener {  }
        mBinding.activitySubscriptionsPaymentInfo.setOnChangeClickListener {
            WalletActivity.buildIntent(this, true)
            mChangePmLauncher.launch(intent)
        }
        mBinding.activitySubscriptionsToolbar.setNavigationOnClickListener { finish() }
    }

    fun goToPlansActivity() {
        NavigationUtils.jumpTo(this, PlansActivity::class.java)
    }

    private fun showCancellationDialog() {
        mPresenter.refreshSubscription()
        val title = getString(R.string.cancellation_dialog_title, UserUtils.getLoggedUser().activeSubscription?.planName)

        EVCSDialogFragment.Builder()
                .setTitle(title)
                .setSubtitle(getString(R.string.cancellation_dialog_subtitle))
                .addButton("Close", { dialog -> dialog.dismiss() },
                    R.drawable.layout_corners_rounded_blue_outline, R.color.button_text_color_selector_blue_outline)
            .show(supportFragmentManager)
    }

}
