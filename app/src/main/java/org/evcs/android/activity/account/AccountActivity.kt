package org.evcs.android.activity.account

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityBaseBinding

class AccountActivity : BaseActivity2() {

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseBinding.inflate(layoutInflater).root;
    }

    override fun init() {
        replaceFragment(R.id.activity_base_content, AccountFragment::class.java)
    }

    fun goToDeleteError() {
        supportFragmentManager.beginTransaction().add(R.id.activity_base_content, DeleteAccountErrorFragment.newInstance())
            .addToBackStack(null).commit()
    }

}