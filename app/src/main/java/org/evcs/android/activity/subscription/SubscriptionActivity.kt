package org.evcs.android.activity.subscription

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityBaseBinding

class SubscriptionActivity : BaseActivity2() {

    override fun init() {
        replaceFragment(R.id.activity_base_content, SubscriptionFragment::class.java)
    }

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseBinding.inflate(layoutInflater).root
    }

}
