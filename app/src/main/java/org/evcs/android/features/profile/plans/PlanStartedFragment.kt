package org.evcs.android.features.profile.plans

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentGetPlanBinding
import org.evcs.android.model.Plan
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.ViewUtils.setVisibility
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

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

    override fun getActiveUntil(): DateTime {
        return DateTime()
    }

    override fun showCouponCode(): Boolean {
        return false
    }

    override fun getTrialLabel(): String? {
        return if (true /* is on trial*/) null else String.format("%d Day Offer", mPlan.trialDays)
    }

    override fun getButtonText(): String {
        return "Close"
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