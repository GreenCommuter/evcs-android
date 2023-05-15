package org.evcs.android.features.profile.plans

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityBaseBinding
import org.evcs.android.model.Plan
import org.evcs.android.model.SubscriptionStatus
import org.evcs.android.util.Extras

class GetPlanActivity : BaseActivity2() {

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseBinding.inflate(layoutInflater).root
    }

    override fun init() {
        val plan = intent.getSerializableExtra(Extras.PlanActivity.PLAN) as Plan
        val fragment = if (intent.getBooleanExtra(Extras.PlanActivity.HAS_PLAN, false))
                            SwitchPlanFragment.newInstance(plan)
                       else GetPlanFragment.newInstance(plan)
        replaceFragment(R.id.activity_base_content, fragment)
    }

    fun onPlanSubscribed(subscription: SubscriptionStatus) {
        val fragment = PlanStartedFragment.newInstance(subscription.plan)
        replaceFragment(R.id.activity_base_content, fragment)
    }

}
