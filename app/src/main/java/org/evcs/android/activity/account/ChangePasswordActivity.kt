package org.evcs.android.activity.account

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.base.core.util.ToastUtils
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.FragmentChangePasswordBinding
import org.evcs.android.features.shared.StandardTextField
import org.evcs.android.model.shared.RequestError
import org.evcs.android.util.validator.*

class ChangePasswordActivity : BaseActivity2(), ChangePasswordView {

    private lateinit var mValidatorManager: ValidatorManager
    private lateinit var mBinding: FragmentChangePasswordBinding
    private lateinit var mPasswordInputLayout: StandardTextField
    private lateinit var mConfirmInputLayout: StandardTextField
    private lateinit var mPasswordHint: TextView
    private lateinit var mContinueButton: Button
    private lateinit var mPresenter: ChangePasswordPresenter

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = FragmentChangePasswordBinding.inflate(layoutInflater)
        mPasswordInputLayout = mBinding.fragmentChangePasswordNew
        mConfirmInputLayout = mBinding.fragmentChangePasswordConfirm
        mPasswordHint = mBinding.fragmentChangePasswordShortPassword
        mContinueButton = mBinding.fragmentChangePasswordButton
        return mBinding.root
    }

    fun createPresenter(): ChangePasswordPresenter {
        return ChangePasswordPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {

        mPresenter = createPresenter()
        mPresenter.onViewCreated()

        mPasswordHint.text = resources.getString(
            R.string.register_password_hint,
            BaseConfiguration.Validations.PASSWORD_MIN_LENGTH
        )
        mValidatorManager = ValidatorManager()
        mValidatorManager.addValidator(PasswordTextInputValidator(mPasswordInputLayout))
        mValidatorManager.addValidator(MatchingValidator(mPasswordInputLayout, mConfirmInputLayout))
        mValidatorManager.setOnAnyTextChangedListener { setEnableButton(mValidatorManager.areAllFieldsValid()) }
    }

    override fun setListeners() {
        mContinueButton.setOnClickListener { onButtonClick() }
    }

    fun setEnableButton(validFields: Boolean) {
        mContinueButton.isEnabled = validFields
    }

    private fun onButtonClick() {
//        progressDialog.show()
        mPresenter.changePassword(
            mPasswordInputLayout.text.toString()
        )
    }

    override fun showError(requestError: RequestError) {
        ToastUtils.show(requestError.body)
    }

    override fun onPasswordChanged() {
        ToastUtils.show(R.string.change_password_success)
        finish()
    }
}