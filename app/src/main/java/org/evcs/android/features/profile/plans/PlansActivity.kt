package org.evcs.android.features.profile.plans

import org.evcs.android.activity.BaseActivity2
import android.view.LayoutInflater
import android.view.View
import org.evcs.android.BaseConfiguration
import org.evcs.android.R
import org.evcs.android.activity.WebViewFragment
import org.evcs.android.databinding.ActivityBaseBinding
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils

/**
 * To show PlansFragment without nav
 */
class PlansActivity : BaseActivity2() {

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseBinding.inflate(layoutInflater).root
    }

    override fun init() {
        if (userHasHiddenPlan()) {
            val url = String.format(BaseConfiguration.WebViews.PLANS_URL, UserUtils.getSessionToken())
            val intent = WebViewFragment.buildIntent(this, "Plans", url, "")
            startActivity(intent)
            finish()
            return
        }

        val isCorporate = intent.getBooleanExtra(Extras.PlanActivity.IS_CORPORATE, true)
        val fragment = PlansFragment.newInstance(isCorporate)
        replaceFragment(R.id.activity_base_content, fragment)
    }

    private fun userHasHiddenPlan(): Boolean {
        val userPlan = UserUtils.getLoggedUser()?.activeSubscription?.plan
        return userPlan != null && !BaseConfiguration.ALLOWED_PLANS.contains(userPlan.name)
    }

}