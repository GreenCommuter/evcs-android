package org.evcs.android.activity.account

import org.evcs.android.activity.BaseActivity2
import android.view.LayoutInflater
import android.view.View
import org.evcs.android.databinding.ActivityChangeNameBinding

class ChangeNameActivity : BaseActivity2() {

    private lateinit var mBinding: ActivityChangeNameBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityChangeNameBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {}

    override fun populate() {
        super.populate()
        mBinding.fragmentChangeNameToolbar.title = "Edit Your Name"
    }
}