package org.evcs.android.activity.account

import org.evcs.android.activity.BaseActivity2
import android.view.LayoutInflater
import android.view.View
import org.evcs.android.databinding.FragmentChangePasswordBinding

class ChangePasswordActivity : BaseActivity2() {

    private lateinit var mBinding: FragmentChangePasswordBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = FragmentChangePasswordBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {}
}