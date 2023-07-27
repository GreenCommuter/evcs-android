package org.evcs.android.features.profile.plans

import android.os.Bundle
import org.evcs.android.R
import org.evcs.android.model.Plan
import org.evcs.android.util.Extras
import org.joda.time.DateTime

class GetPlanFragment : AbstractGetPlanFragment() {

    companion object {
        fun newInstance(plan: Plan): GetPlanFragment {
            val args = Bundle()
            args.putSerializable(Extras.PlanActivity.PLAN, plan)

            val fragment = GetPlanFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getStartingDate(): DateTime? {
        return mPlan.startingDate()
    }

    override fun getActiveUntil(): DateTime? {
        return null
    }

    override fun showCouponCode(): Boolean {
        return false
    }

    override fun getTrialLabel(): String {
        return String.format("%d Day Offer - New Members Only", mPlan.trialDays)
    }

    override fun getButtonText(): String {
        return getString(R.string.get_plan_agree_subscribe)
    }

    override fun showToday(): Boolean {
        return true
    }
}