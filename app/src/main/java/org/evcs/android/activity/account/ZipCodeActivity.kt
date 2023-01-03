package org.evcs.android.activity.account

import android.view.LayoutInflater
import android.view.View
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityChangeZipcodeBinding
import org.evcs.android.model.shared.RequestError
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils
import org.evcs.android.util.validator.ValidatorManager
import org.evcs.android.util.validator.ZipCodeTextInputValidator


class ZipCodeActivity : BaseActivity2(), UpdateUserView {

    private lateinit var mValidatorManager: ValidatorManager
    private lateinit var mBinding: ActivityChangeZipcodeBinding
    private lateinit var mPresenter: UpdateUserPresenter

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityChangeZipcodeBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mPresenter = UpdateUserPresenter(this, EVCSApplication.getInstance().retrofitServices)
        mValidatorManager = ValidatorManager()
        mPresenter.onViewCreated()
    }

    override fun populate() {
        super.populate()
        mValidatorManager.addValidator(ZipCodeTextInputValidator(mBinding.activityZipcodeText))

        mBinding.activityZipcodeText.editText?.setText(UserUtils.getLoggedUser().zipCode)
        mValidatorManager.setOnAnyTextChangedListener {
            mBinding.activityZipcodeSave.isEnabled = mValidatorManager.areAllFieldsValid()
        }
    }

    override fun setListeners() {
        mBinding.activityZipcodeSave.setOnClickListener {
            mBinding.activityZipcodeSave.isEnabled = false
            mPresenter.changeZipCode(mBinding.activityZipcodeText.text)
        }
        mBinding.activityChangeZipcodeToolbar.setNavigationOnClickListener { finish() }

        ViewUtils.setAdjustResize(mBinding.activityZipcodeLayout)
    }

    override fun showError(requestError: RequestError) {
        mBinding.activityZipcodeSave.isEnabled = true
        ToastUtils.show(requestError.body)
    }

    override fun onUserUpdate() {
        finish()
    }
}