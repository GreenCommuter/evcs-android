package org.evcs.android.activity.account

import org.evcs.android.activity.BaseActivity2
import android.view.LayoutInflater
import android.view.View
import org.evcs.android.databinding.ActivityDeleteAccountBinding

class DeleteAccountActivity : BaseActivity2() {
    private lateinit var mBinding: ActivityDeleteAccountBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityDeleteAccountBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {}

    override fun setListeners() {
        mBinding.fragmentDeleteAccountToolbar.setNavigationOnClickListener { finish() }
    }
}