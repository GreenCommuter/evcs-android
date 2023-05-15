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
        return "Agree & Subscribe"
    }

    override fun showToday(): Boolean {
        return true
    }
}