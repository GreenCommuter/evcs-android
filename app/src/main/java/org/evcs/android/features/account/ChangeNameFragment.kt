package org.evcs.android.features.account

import android.view.View
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.ActivityChangeNameBinding
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils
import org.evcs.android.util.validator.NonEmptyTextInputValidator
import org.evcs.android.util.validator.ValidatorManager


class ChangeNameFragment : ErrorFragment<UpdateUserPresenter>(), UpdateUserView {

    private lateinit var mValidatorManager: ValidatorManager
    private lateinit var mBinding: ActivityChangeNameBinding

    override fun layout(): Int {
        return R.layout.activity_change_name
    }

    override fun setUi(v: View) {
        mBinding = ActivityChangeNameBinding.bind(v)
    }

    override fun createPresenter(): UpdateUserPresenter {
        return UpdateUserPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {
        mValidatorManager = ValidatorManager()
    }

    override fun populate() {
        super.populate()
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mBinding.fragmentChangeNameFirst))
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mBinding.fragmentChangeNameLast))
        mBinding.fragmentChangeNameFirst.editText?.setText(UserUtils.getLoggedUser().firstName)
        mBinding.fragmentChangeNameLast.editText?.setText(UserUtils.getLoggedUser().lastName)
        mValidatorManager.setOnAnyTextChangedListener {
            mBinding.activityChangeNameSave.isEnabled = mValidatorManager.areAllFieldsValid()
        }
    }

    override fun setListeners() {
        mBinding.activityChangeNameSave.setOnClickListener {
            mBinding.activityChangeNameSave.isEnabled = false
            presenter.changeName(mBinding.fragmentChangeNameFirst.text.trim(), mBinding.fragmentChangeNameLast.text.trim())
        }

        ViewUtils.setAdjustResize(mBinding.activityAccountLayout)
    }

    override fun showError(requestError: RequestError) {
        mBinding.activityChangeNameSave.isEnabled = true
        super.showError(requestError)
    }

    override fun onUserUpdate() {
        activity?.finish()
    }
}