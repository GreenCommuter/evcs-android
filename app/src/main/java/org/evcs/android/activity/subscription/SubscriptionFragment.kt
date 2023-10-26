package org.evcs.android.activity.subscription

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.ColorStateList
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.core.view.isVisible
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.CancelPlanActivity
import org.evcs.android.databinding.ActivitySubscriptionBinding
import org.evcs.android.features.profile.plans.PlansActivity
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.SubscriptionStatus
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.StringUtils
import org.evcs.android.util.UserUtils
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class SubscriptionFragment : ErrorFragment<SubscriptionPresenter>(), SubscriptionView {

    private lateinit var mSubscriptionId: String
    private lateinit var mLongDateFormatter: DateTimeFormatter
    private lateinit var mLauncher: ActivityResultLauncher<Intent>
    private lateinit var mChangePmLauncher: ActivityResultLauncher<Intent>
    private lateinit var mBinding: ActivitySubscriptionBinding

    override fun layout(): Int {
        return R.layout.activity_subscription
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = ActivitySubscriptionBinding.bind(v)
    }

    override fun init() {
        mLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    result -> if (result.resultCode == RESULT_OK) {
                        presenter.refreshSubscription()
                        showCancellationDialog()
                    }
                }
        mChangePmLauncher = WalletActivity.getDefaultLauncher(this) { pm ->
            mBinding.activitySubscriptionsPaymentInfo.setPaymentMethod(pm) }
        mLongDateFormatter = DateTimeFormat.forPattern("MMM dd, yyyy")
    }

    override fun populate() {
        createPresenter()
        presenter.refreshSubscription()
        UserUtils.getLoggedUser().activeSubscription?.let { onSubscriptionPlanRetrieved(it) }
    }

    override fun createPresenter(): SubscriptionPresenter {
        return SubscriptionPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun onSubscriptionPlanRetrieved(response: SubscriptionStatus) {
        hideProgressDialog()
        mSubscriptionId = response.id
        mBinding.managePlansCanceledLayout.isVisible = response.isCanceled
        mBinding.managePlansActiveLayout.isVisible = !response.isCanceled
        mBinding.activitySubscriptionsPlanName.text = response.planName
        mBinding.activitySubscriptionsEnrolled.setText(response.activeSince)
        mBinding.activitySubscriptionsPlanProgress.setPlan(response)

        if (response.isCanceled) {
            populateCanceled(response)
        } else {
            populateActive(response)
        }
    }

    fun populateActive(response: SubscriptionStatus) {
        val date = mLongDateFormatter.print(response.renewalDate)
        val defaultPm = PaymentMethod.getDefaultFromSharedPrefs()
        var paymentDetailsText = getString(R.string.manage_plan_payment_details_next, date)
        if (userPaysForPlan(response)) {
            paymentDetailsText =
                    getString(R.string.manage_plan_payment_details_format, StringUtils.capitalize(defaultPm!!.card.brand.toPrintableString()),
                            defaultPm.card.last4, response.price, response.renewalPeriod) + "\n" + paymentDetailsText
        } else {
            mBinding.activitySubscriptionsPaymentDetails.setLabel("")
            mBinding.activitySubscriptionsPaymentInfo.isVisible = false
            mBinding.activitySubscriptionsPaymentInfoTitle.isVisible = false
        }
        mBinding.activitySubscriptionsPaymentDetails.setText(paymentDetailsText)
        mBinding.activitySubscriptionsPaymentInfo.setPaymentMethod(defaultPm)

        mBinding.activitySubscriptionsPlanStatus.text = response.status.toString()
        mBinding.activitySubscriptionsViewAllPlans.isEnabled = !response.onTrialPeriod

        if (response.onTrialPeriod) {
            mBinding.activitySubscriptionsFreeTrial.visibility = View.VISIBLE
            mBinding.activitySubscriptionsFreeTrial.setText(
                    getString(R.string.manage_plan_days_remaining, response.activeDaysLeft))
            mBinding.activitySubscriptionsPlanStatus.text = "Free Trial"
        }
        setChipColor(R.color.evcs_secondary_700)
    }

    private fun userPaysForPlan(response: SubscriptionStatus): Boolean {
        return response.price > 0f && UserUtils.getLoggedUser().companyId == null
    }

    private fun populateCanceled(response: SubscriptionStatus) {
        mBinding.activitySubscriptionsPaymentDetails.setText(String.format(
                getString(R.string.manage_plan_payment_details_format_canceled),
                mLongDateFormatter.print(response.renewalDate)))
        setChipColor(R.color.evcs_danger_700)
        mBinding.activitySubscriptionsPlanStatus.text = "Canceled"
    }

    private fun setChipColor(@ColorRes color: Int) {
        mBinding.activitySubscriptionsPlanStatus.backgroundTintList =
            ColorStateList.valueOf(resources.getColor(color))
    }

    override fun setListeners() {
        mBinding.activitySubscriptionsCancel.setOnClickListener {
            mLauncher.launch(Intent(requireContext(), CancelPlanActivity::class.java))
        }
        mBinding.activitySubscriptionsViewAllPlans.setOnClickListener { goToPlansActivity() }
        mBinding.activitySubscriptionsViewAllPlans2.setOnClickListener { goToPlansActivity() }
        mBinding.activitySubscriptionsActivate.setOnClickListener {
            showProgressDialog()
            presenter.voidCancellation(mSubscriptionId)
        }
        mBinding.activitySubscriptionsPaymentInfo.setOnChangeClickListener {
            val intent = WalletActivity.buildIntent(requireContext(), true)
            mChangePmLauncher.launch(intent)
        }
    }

    fun goToPlansActivity() {
        val intent = Intent(requireContext(), PlansActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
//        NavigationUtils.jumpTo(this, PlansActivity::class.java)
    }

    private fun showCancellationDialog() {
        val title = getString(R.string.cancellation_dialog_title, UserUtils.getLoggedUser().activeSubscription?.planName)

        EVCSDialogFragment.Builder()
                .setTitle(title)
                .setSubtitle(getString(R.string.cancellation_dialog_subtitle))
                .addButton(getString(R.string.app_close), { dialog -> dialog.dismiss() }, R.style.ButtonK_BlueOutline)
            .show(childFragmentManager)
    }

}
