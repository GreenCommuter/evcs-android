package org.evcs.android.activity.account

import android.app.Dialog
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
import org.evcs.android.util.Extras
import org.evcs.android.util.SpinnerUtils
import org.evcs.android.util.UserUtils
import org.evcs.android.util.validator.*

class ChangePasswordActivity : BaseActivity2(), ChangePasswordView {

    private lateinit var progressDialog: Dialog
    private lateinit var mValidatorManager: ValidatorManager
    private lateinit var mBinding: FragmentChangePasswordBinding
    private lateinit var mPasswordInputLayout: StandardTextField
    private lateinit var mOldPasswordInputLayout: StandardTextField
    private lateinit var mConfirmInputLayout: StandardTextField
    private lateinit var mContinueButton: Button
    private lateinit var mPresenter: ChangePasswordPresenter

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = FragmentChangePasswordBinding.inflate(layoutInflater)
        mPasswordInputLayout = mBinding.fragmentChangePasswordNew
        mOldPasswordInputLayout = mBinding.fragmentChangePasswordOld
        mConfirmInputLayout = mBinding.fragmentChangePasswordConfirm
        mContinueButton = mBinding.fragmentChangePasswordButton
        return mBinding.root
    }

    fun createPresenter(): ChangePasswordPresenter {
        return ChangePasswordPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {

        mPresenter = createPresenter()
        mPresenter.onViewCreated()

        progressDialog = SpinnerUtils.getNewProgressDialog(this, R.layout.spinner_layout_black)
//        mPasswordHint.text = resources.getString(
//            R.string.register_password_hint,
//            BaseConfiguration.Validations.PASSWORD_MIN_LENGTH
//        )
        mValidatorManager = ValidatorManager()
        mValidatorManager.addValidator(PasswordTextInputValidator(mPasswordInputLayout))
        mValidatorManager.addValidator(MatchingValidator(mConfirmInputLayout, mPasswordInputLayout))
        mValidatorManager.setOnAnyTextChangedListener { setEnableButton(mValidatorManager.areAllFieldsValid()) }

        if (isLoggedUser()) {
            mPresenter.setParamsForReset(intent.getStringExtra(Extras.ForgotPassword.EMAIL)!!,
                                         intent.getStringExtra(Extras.ForgotPassword.ID)!!)
        } else {
            mOldPasswordInputLayout.visibility = View.VISIBLE
            mValidatorManager.addValidator(PasswordTextInputValidator(mOldPasswordInputLayout))
        }

    }

    fun isLoggedUser(): Boolean {
        return intent.hasExtra(Extras.ForgotPassword.EMAIL)
    }

    override fun setListeners() {
        mContinueButton.setOnClickListener { onButtonClick() }
    }

    fun setEnableButton(validFields: Boolean) {
        mContinueButton.isEnabled = validFields
    }

    private fun onButtonClick() {
        progressDialog.show()
        mPresenter.onButtonClick(
            mOldPasswordInputLayout.text.toString(),
            mPasswordInputLayout.text.toString()
        )
    }

    override fun showError(requestError: RequestError) {
        progressDialog.dismiss()
        ToastUtils.show(requestError.body)
    }

    override fun onPasswordChanged() {
        progressDialog.dismiss()
        ToastUtils.show(R.string.change_password_success)
        if (isLoggedUser()) {
            UserUtils.logout(null)
        } else {
            finish()
        }
    }
}