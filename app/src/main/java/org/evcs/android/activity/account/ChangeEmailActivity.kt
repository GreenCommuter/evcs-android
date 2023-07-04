package org.evcs.android.activity.account

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.databinding.ActivityChangeEmailBinding
import org.evcs.android.ui.view.shared.EVCSToolbar2
import org.evcs.android.util.UserUtils
import org.evcs.android.util.validator.EmailTextInputValidator


class ChangeEmailActivity : UpdateUserActivity() {

    private lateinit var mBinding: ActivityChangeEmailBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityChangeEmailBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun populate() {
        super.populate()
        mValidatorManager.addValidator(EmailTextInputValidator(mBinding.activityChangeEmailInput))
        mBinding.activityChangeEmailInput.editText?.setText(UserUtils.getLoggedUser().email)
    }

    override fun getButton(): View {
        return mBinding.activityChangeNameSave
    }

    override fun getLayout(): View {
        return mBinding.activityAccountLayout
    }

    override fun onSaveClicked() {
        mBinding.activityChangeNameSave.isEnabled = false
        mPresenter.changeEmail(mBinding.activityChangeEmailInput.text.trim())
    }

}