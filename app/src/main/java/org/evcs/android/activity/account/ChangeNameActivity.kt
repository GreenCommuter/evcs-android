package org.evcs.android.activity.account

import android.view.LayoutInflater
import android.view.View
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityChangeNameBinding
import org.evcs.android.model.shared.RequestError
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils
import org.evcs.android.util.validator.NonEmptyTextInputValidator
import org.evcs.android.util.validator.ValidatorManager


class ChangeNameActivity : BaseActivity2(), UpdateUserView {

    private lateinit var mValidatorManager: ValidatorManager
    private lateinit var mBinding: ActivityChangeNameBinding
    private lateinit var mPresenter: UpdateUserPresenter

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityChangeNameBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mPresenter = UpdateUserPresenter(this, EVCSApplication.getInstance().retrofitServices)
        mValidatorManager = ValidatorManager()
        mPresenter.onViewCreated()
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
            mPresenter.changeName(mBinding.fragmentChangeNameFirst.text.trim(), mBinding.fragmentChangeNameLast.text.trim())
        }
        mBinding.activityChangeNameToolbar.setNavigationOnClickListener { finish() }

        ViewUtils.setAdjustResize(mBinding.activityAccountLayout)
    }

    override fun showError(requestError: RequestError) {
        mBinding.activityChangeNameSave.isEnabled = true
        ToastUtils.show(requestError.body)
    }

    override fun onUserUpdate() {
        finish()
    }
}