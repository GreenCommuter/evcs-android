package org.evcs.android.activity.account

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.base.core.util.NavigationUtils.jumpTo
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityAccountBinding
import org.evcs.android.util.UserUtils

class AccountActivity : BaseActivity2() {

    private lateinit var mChangeNameResult: ActivityResultLauncher<Intent>
    private lateinit var mBinding: ActivityAccountBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityAccountBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mChangeNameResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            mBinding.fragmentAccountName.text = UserUtils.getLoggedUser().name
        }
    }

    override fun populate() {
        super.populate()
        mBinding.fragmentAccountEmail.text = UserUtils.getUserEmail()
        mBinding.fragmentAccountName.text = UserUtils.getLoggedUser().name;
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.fragmentAccountNameLayout.setOnClickListener {
            mChangeNameResult.launch(Intent(this, ChangeNameActivity::class.java))
        }
        mBinding.fragmentAccountChangePassword.setOnClickListener { jumpTo(this, ChangePasswordActivity::class.java) }
        //TODO: add dialog
        mBinding.fragmentAccountSignOut.setOnClickListener { UserUtils.logout(null) }
        mBinding.fragmentAccountDelete.setOnClickListener { jumpTo(this, DeleteAccountActivity::class.java)  }
        mBinding.fragmentAccountToolbar.setNavigationOnClickListener { finish() }
    }
}