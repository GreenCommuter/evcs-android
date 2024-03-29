package org.evcs.android.features.profile.plans

import android.os.Bundle
import org.evcs.android.R
import org.evcs.android.model.Plan
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter

@Deprecated("")
class PlanStartedFragment : AbstractGetPlanFragment() {

    companion object {
        fun newInstance(plan: Plan): PlanStartedFragment {
            val args = Bundle()
            args.putSerializable(Extras.PlanActivity.PLAN, plan)

            val fragment = PlanStartedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getTandCText(): CharSequence? {
        return null
    }

    override fun getStartingDate(): DateTime? {
        TODO("Not yet implemented")
    }

    override fun getActiveUntil(): DateTime? {
        return null//UserUtils.getLoggedUser().previousSubscription?.renewalDate
    }

    override fun showCouponCode(): Boolean {
        return false
    }

    override fun getTrialLabel(): String? {
        if (UserUtils.getLoggedUser().activeSubscription?.onTrialPeriod?: false)
            return String.format("%d Day Offer", mPlan.trialDays)
        else
            return null
    }

    override fun getButtonText(): String {
        return getString(R.string.app_close)
    }

    override fun showToday(): Boolean {
        return false
    }

    override fun getToolbarTitle(): String {
        return "Plan Summary"
    }

    override fun showCostLayout(): Boolean {
        return false
    }

    override fun showPlanStarted(): Boolean {
        return true
    }

    override fun getNextBillingDate(): DateTime? {
        return DateTime()
    }

    override fun getMonthlyLabel(dateFormatter: DateTimeFormatter): String {
        return "%1\$sly Rate"
    }

    override fun getBottomNavigationListener() {
        requireActivity().finish()
    }
}