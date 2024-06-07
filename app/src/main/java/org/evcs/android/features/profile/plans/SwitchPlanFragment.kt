package org.evcs.android.features.profile.plans

import android.os.Bundle
import org.evcs.android.R
import org.evcs.android.model.Plan
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils
import org.joda.time.DateTime

class SwitchPlanFragment : AbstractGetPlanFragment() {

    private var isDowngrade: Boolean = false

    companion object {
        fun newInstance(plan: Plan): SwitchPlanFragment {
            val args = Bundle()
            args.putSerializable(Extras.PlanActivity.PLAN, plan)

            val fragment = SwitchPlanFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun init() {
        super.init()
        val sub = UserUtils.getLoggedUser().activeSubscription
        isDowngrade = sub!!.plan.price > mPlan.price
    }

    override fun getActiveUntil(): DateTime {
        return if (isDowngrade) UserUtils.getLoggedUser().activeSubscription!!.renewalDate else DateTime()
    }

    override fun getStartingDate(): DateTime? {
        return getActiveUntil().plusDays(1)
    }

    override fun showCouponCode(): Boolean {
        return false
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