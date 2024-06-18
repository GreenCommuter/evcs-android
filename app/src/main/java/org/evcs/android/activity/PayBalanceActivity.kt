package org.evcs.android.activity

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.R
import org.evcs.android.databinding.ActivityBaseBinding
import org.evcs.android.features.paybalance.PayBalanceFragment

class PayBalanceActivity : BaseActivity2() {

    override fun init() {
        replaceFragment(R.id.activity_base_content, PayBalanceFragment::class.java)
    }

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseBinding.inflate(layoutInflater).root
    }

}
