package org.evcs.android.activity.account

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.databinding.ActivityChangeNameBinding
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
        var firstName = UserUtils.getLoggedUser().firstName
        var lastName = UserUtils.getLoggedUser().lastName
        val splitFirstName = UserUtils.getLoggedUser().firstName?.split(" ")
        //Old users have their full name as firstname
        if (splitFirstName?.size == 2 && lastName == ""){
            firstName = splitFirstName[0]
            lastName = splitFirstName[1]
        }
        mBinding.fragmentChangeNameFirst.editText?.setText(firstName)
        mBinding.fragmentChangeNameLast.editText?.setText(lastName)
    }

    override fun getButton(): View {
        return mBinding.activityChangeNameSave
    }

    override fun getLayout(): View {
        return mBinding.activityAccountLayout
    }

    override fun onSaveClicked() {
        mBinding.activityChangeNameSave.isEnabled = false
        mPresenter.changeName(mBinding.fragmentChangeNameFirst.text.trim(), mBinding.fragmentChangeNameLast.text.trim())
    }

}