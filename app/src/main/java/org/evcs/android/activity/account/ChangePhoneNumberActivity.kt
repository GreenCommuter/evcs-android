package org.evcs.android.activity.account

import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import org.evcs.android.BaseConfiguration
import org.evcs.android.databinding.ActivityChangePhoneNumberBinding
import org.evcs.android.ui.view.shared.EVCSToolbar2
import org.evcs.android.util.UserUtils
import org.evcs.android.util.validator.PhoneTextInputValidator


class ChangePhoneNumberActivity : UpdateUserActivity() {

    private lateinit var mBinding: ActivityChangePhoneNumberBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityChangePhoneNumberBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun populate() {
        super.populate()
        mValidatorManager.addValidator(PhoneTextInputValidator(mBinding.activityChangePhoneNumber))
        mBinding.activityChangePhoneNumber.editText?.addTextChangedListener(PhoneNumberFormattingTextWatcher(
                BaseConfiguration.DEFAULT_LOCALE.country))
        mBinding.activityChangePhoneNumber.editText?.setText(UserUtils.getLoggedUser().phone)

    }

    override fun getButton(): View {
        return mBinding.activityChangeNameSave
    }

    override fun getToolbar(): EVCSToolbar2 {
        return mBinding.activityChangePhoneToolbar
    }

    override fun getLayout(): View {
        return mBinding.activityAccountLayout
    }

    override fun onSaveClicked() {
        mBinding.activityChangeNameSave.isEnabled = false
        mPresenter.changePhoneNumber(mBinding.activityChangePhoneNumber.text.trim())
    }
}