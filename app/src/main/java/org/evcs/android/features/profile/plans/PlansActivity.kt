package org.evcs.android.features.profile.plans

import org.evcs.android.activity.BaseActivity2
import android.view.LayoutInflater
import android.view.View
import org.evcs.android.R
import org.evcs.android.databinding.ActivityBaseBinding

/**
 * To show PlansFragment without nav
 */
class PlansActivity : BaseActivity2() {
    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseBinding.inflate(layoutInflater).root
    }

    override fun init() {
        replaceFragment(R.id.activity_base_content, PlansFragment::class.java)
    }
}