package org.evcs.android.activity

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityBaseBinding
import org.evcs.android.features.profile.AccountFragment
import org.evcs.android.features.profile.sessioninformation.SessionInformationFragment

class ChargeReceiptActivity : BaseActivity2() {

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseBinding.inflate(layoutInflater).root;
    }

    override fun init() {
        replaceFragment(R.id.activity_base_content,
            SessionInformationFragment.newInstance(intent.getIntExtra("id", 0)))
    }
}