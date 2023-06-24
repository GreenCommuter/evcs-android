package org.evcs.android.features.profile.plans

import org.evcs.android.activity.BaseActivity2
import android.view.LayoutInflater
import android.view.View
import org.evcs.android.R
import org.evcs.android.databinding.ActivityBaseBinding
import org.evcs.android.util.Extras

/**
 * To show PlansFragment without nav
 */
class PlansActivity : BaseActivity2() {

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseBinding.inflate(layoutInflater).root
    }

    override fun init() {
        val isCorporate = intent.getBooleanExtra(Extras.PlanActivity.IS_CORPORATE, true)
        val fragment = PlansFragment.newInstance(isCorporate)
        replaceFragment(R.id.activity_base_content, fragment)
    }

}