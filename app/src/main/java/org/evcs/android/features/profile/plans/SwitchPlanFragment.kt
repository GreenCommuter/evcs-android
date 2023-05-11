package org.evcs.android.features.profile.plans

import android.os.Bundle
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

    override fun getActiveUntil(): DateTime? {
        return null
    }

    override fun showCouponCode(): Boolean {
        return true;
    }

    override fun showFreeTrial(): Boolean {
        return false
    }

    override fun getButtonText(): String {
        return "Agree & Switch Plans"
    }

    override fun showToday(): Boolean {
        return false
    }

}