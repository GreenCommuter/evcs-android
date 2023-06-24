package org.evcs.android.features.profile.notifications

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityBaseBinding

class NotificationsActivity : BaseActivity2() {

    override fun init() {
        replaceFragment(R.id.activity_base_content, NotificationsFragment::class.java)
    }

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseBinding.inflate(layoutInflater).root
    }
}

