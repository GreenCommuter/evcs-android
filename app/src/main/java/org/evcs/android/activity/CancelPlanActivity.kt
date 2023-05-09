package org.evcs.android.activity

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.R
import org.evcs.android.databinding.ActivityBaseNavhostBinding

class CancelPlanActivity : NavGraphActivity() {

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseNavhostBinding.inflate(layoutInflater).root
    }

    override fun getNavGraphId(): Int {
        return R.navigation.navigation_cancel_plan
    }

    override fun init() {}
}