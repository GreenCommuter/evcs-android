package org.evcs.android.features.profile.plans

import android.os.Bundle
import org.evcs.android.R
import org.evcs.android.model.Plan
import org.evcs.android.util.Extras
import org.joda.time.DateTime

class SwitchPlanFragment : AbstractGetPlanFragment() {

    companion object {
        fun newInstance(plan: Plan): SwitchPlanFragment {
            val args = Bundle()
            args.putSerializable(Extras.PlanActivity.PLAN, plan)

            val fragment = SwitchPlanFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getActiveUntil(): DateTime {
        //todo: set
        return DateTime()
    }

    override fun showCouponCode(): Boolean {
        return true;
    }

    override fun getTrialLabel(): String? {
        return null
    }

    override fun getButtonText(): String {
        return getString(R.string.get_plan_agree_switch)
    }

    override fun showToday(): Boolean {
        return false
    }

}