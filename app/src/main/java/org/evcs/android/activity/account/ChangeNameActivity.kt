package org.evcs.android.activity.account

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.databinding.ActivityChangeNameBinding
import org.evcs.android.ui.view.shared.EVCSToolbar2
import org.evcs.android.util.UserUtils
import org.evcs.android.util.validator.NonEmptyTextInputValidator


class ChangeNameActivity : UpdateUserActivity() {

    private lateinit var mBinding: ActivityChangeNameBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityChangeNameBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun populate() {
        super.populate()
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mBinding.fragmentChangeNameFirst))
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mBinding.fragmentChangeNameLast))
        mBinding.fragmentChangeNameFirst.editText?.setText(UserUtils.getLoggedUser().firstName)
        mBinding.fragmentChangeNameLast.editText?.setText(UserUtils.getLoggedUser().lastName)
    }

    override fun getButton(): View {
        return mBinding.activityChangeNameSave
    }

    override fun getToolbar(): EVCSToolbar2 {
        return mBinding.activityChangeNameToolbar
    }

    override fun getLayout(): View {
        return mBinding.activityAccountLayout
    }

    override fun onSaveClicked() {
        mBinding.activityChangeNameSave.isEnabled = false
        mPresenter.changeName(mBinding.fragmentChangeNameFirst.text.trim(), mBinding.fragmentChangeNameLast.text.trim())
    }

}