package org.evcs.android.activity.account

import android.view.LayoutInflater
import android.view.View
import com.base.core.util.NavigationUtils.jumpTo
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityAccountBinding
import org.evcs.android.util.UserUtils

class AccountActivity : BaseActivity2() {

    private lateinit var mBinding: ActivityAccountBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityAccountBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {}

    override fun populate() {
        super.populate()
        mBinding.fragmentAccountToolbar.title = "Edit Account"
        mBinding.fragmentAccountToolbar.navigationIcon = getDrawable(R.drawable.back_arrow)
        mBinding.fragmentAccountEmail.text = UserUtils.getUserEmail()
        //        mBinding.fragmentAccountName.setText(UserUtils.getLoggedUser().name);
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.fragmentAccountNameLayout.setOnClickListener { jumpTo(this, ChangeNameActivity::class.java) }
        mBinding.fragmentAccountChangePassword.setOnClickListener { jumpTo(this, ChangePasswordActivity::class.java) }
        //TODO: add dialog
        mBinding.fragmentAccountSignOut.setOnClickListener { UserUtils.logout(null) }
        mBinding.fragmentAccountDelete.setOnClickListener { jumpTo(this, DeleteAccountActivity::class.java)  }
    }
}