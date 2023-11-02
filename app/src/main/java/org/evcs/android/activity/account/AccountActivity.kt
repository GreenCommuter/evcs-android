package org.evcs.android.activity.account

import android.content.Intent
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.base.core.util.NavigationUtils.jumpTo
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityAccountBinding
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.UserUtils

class AccountActivity : BaseActivity2(), DeleteAccountView {

    private lateinit var mChangeUserResult: ActivityResultLauncher<Intent>
    private lateinit var mBinding: ActivityAccountBinding
    private lateinit var mPresenter: DeleteAccountPresenter

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityAccountBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mChangeUserResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            populate()
        }
        mPresenter = DeleteAccountPresenter(this, EVCSApplication.getInstance().retrofitServices)
        mPresenter.onViewCreated()
    }

    override fun populate() {
        super.populate()
        val user = UserUtils.getLoggedUser()
        mBinding.fragmentAccountEmail.setText(user.email)
        mBinding.fragmentAccountPhone.setText(PhoneNumberUtils.formatNumber(user.phone?:"", "US"))
        mBinding.fragmentAccountName.setText(UserUtils.getLoggedUser().name)
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.fragmentAccountName.setOnClickListener {
            mChangeUserResult.launch(Intent(this, ChangeNameActivity::class.java))
        }
        mBinding.fragmentAccountChangePassword.setOnClickListener { jumpTo(this, ChangePasswordActivity::class.java) }
        mBinding.fragmentAccountEmail.setOnClickListener {
            mChangeUserResult.launch(Intent(this, ChangeEmailActivity::class.java))
        }
        mBinding.fragmentAccountPhone.setOnClickListener {
//            mChangeUserResult.launch(Intent(this, ChangePhoneNumberActivity::class.java))
        }
        mBinding.fragmentAccountDelete.setOnClickListener {
            EVCSDialogFragment.Builder()
                    .setTitle("Are you sure?")
                    .setSubtitle("Everything related to your account will be lost. This action can't be undone.")
                    .addButton("Yes, delete my account", { dialog ->
                        dialog.dismiss()
                        mBinding.fragmentAccountDelete.isEnabled = false
                        mPresenter.deleteAccount()
                    }, R.style.ButtonK_Danger)
                    .showCancel("No")
                    .show(supportFragmentManager)
        }
    }

    override fun onAccountDeleted() {
        ToastUtils.show("Your account has been deleted")
        UserUtils.logout(null)
    }

    override fun showError(requestError: RequestError) {
        ErrorFragment.showError(null, requestError)
    }

}