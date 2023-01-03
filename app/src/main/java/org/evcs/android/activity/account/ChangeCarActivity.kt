package org.evcs.android.activity.account

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityBaseBinding
import org.evcs.android.features.profile.ChangeCarFragment


class ChangeCarActivity : BaseActivity2() {

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseBinding.inflate(layoutInflater).root;
    }

    override fun init() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_base_content, ChangeCarFragment::class.java, null)
            .commit()
    }

}