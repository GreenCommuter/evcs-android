package org.evcs.android.features.subscriptions

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import org.evcs.android.R
import org.evcs.android.databinding.FragmentCancelPlanBinding
import org.evcs.android.model.Subscription
import org.evcs.android.util.UserUtils
import org.joda.time.format.DateTimeFormat

class CancelPlanFragment : AbstractCancelPlanFragment() {

    private lateinit var mBinding: FragmentCancelPlanBinding

    override fun setPlan(subscription: Subscription) {
        mBinding.cancelPlanSubscriptionEnd.text = DateTimeFormat.forPattern(getString(R.string.app_date_format))
                .print(subscription.renewalDate)
//        mBinding.cancelPlanBillingCycle
        val activeDays = subscription.activeDaysLeft
        mBinding.cancelPlanActiveDays.text = getString(R.string.cancel_plan_days_remaining, activeDays)
        val dateTimeFormat = DateTimeFormat.forPattern("MMM d, yyyy")
        val activeUntil = getString(R.string.cancel_plan_active_until, subscription.plan.renewalPeriod.toAdverb(),
            subscription.planName, dateTimeFormat.print(subscription.renewalDate))
        mBinding.cancelPlanActiveUntil.text = activeUntil
    }

    override fun getConfirmationText(): String {
        return getString(R.string.cancel_plan_confirmation)
    }

    override fun onContinueClicked() {
        findNavController().navigate(CancelPlanFragmentDirections.actionCancelPlanFragmentToSurveyFragment())
    }

    override fun getCancelText(): String {
        return getString(R.string.cancel_plan_cancel)
    }

    override fun getChildLayout(parent: FrameLayout): View {
        mBinding = FragmentCancelPlanBinding.inflate(LayoutInflater.from(requireContext()))//, parent, false)
        return mBinding.root
    }

    override fun getBoldText(): String {
        return getString(R.string.cancel_plan_sad)
    }

}
