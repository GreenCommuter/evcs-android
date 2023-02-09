package org.evcs.android.features.account

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.base.core.util.ToastUtils
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentChangePasswordBinding
import org.evcs.android.features.shared.StandardTextField
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils
import org.evcs.android.util.validator.*

class ChangePasswordFragment : ErrorFragment<ChangePasswordPresenter>(), ChangePasswordView {

    private lateinit var mValidatorManager: ValidatorManager
    private lateinit var mBinding: FragmentChangePasswordBinding
    private lateinit var mPasswordInputLayout: StandardTextField
    private lateinit var mOldPasswordInputLayout: StandardTextField
    private lateinit var mConfirmInputLayout: StandardTextField
    private lateinit var mPasswordHint: TextView
    private lateinit var mContinueButton: Button

    override fun createPresenter(): ChangePasswordPresenter {
        return ChangePasswordPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentChangePasswordBinding.bind(v)
        mPasswordInputLayout = mBinding.fragmentChangePasswordNew
        mOldPasswordInputLayout = mBinding.fragmentChangePasswordOld
        mConfirmInputLayout = mBinding.fragmentChangePasswordConfirm
        mPasswordHint = mBinding.fragmentChangePasswordShortPassword
        mContinueButton = mBinding.fragmentChangePasswordButton
    }

    override fun layout(): Int {
        return R.layout.fragment_change_password
    }

    override fun init() {
        mPasswordHint.text = resources.getString(
            R.string.register_password_hint,
            BaseConfiguration.Validations.PASSWORD_MIN_LENGTH
        )
        mValidatorManager = ValidatorManager()
        mValidatorManager.addValidator(PasswordTextInputValidator(mPasswordInputLayout))
        mValidatorManager.addValidator(MatchingValidator(mConfirmInputLayout, mPasswordInputLayout))
        mValidatorManager.setOnAnyTextChangedListener { setEnableButton(mValidatorManager.areAllFieldsValid()) }

        if (isLoggedUser()) {
            presenter.setParamsForReset(requireActivity().intent.getStringExtra(Extras.ForgotPassword.EMAIL)!!,
                                         requireActivity().intent.getStringExtra(Extras.ForgotPassword.ID)!!)
        } else {
            mOldPasswordInputLayout.visibility = View.VISIBLE
            mValidatorManager.addValidator(PasswordTextInputValidator(mOldPasswordInputLayout))
        }

    }

    fun isLoggedUser(): Boolean {
        return requireActivity().intent.hasExtra(Extras.ForgotPassword.EMAIL)
    }

    override fun setListeners() {
        mContinueButton.setOnClickListener { onButtonClick() }
    }

    fun setEnableButton(validFields: Boolean) {
        mContinueButton.isEnabled = validFields
    }

    private fun onButtonClick() {
//        progressDialog.show()
        presenter.onButtonClick(
            mOldPasswordInputLayout.text.toString(),
            mPasswordInputLayout.text.toString()
        )
    }

    override fun onPasswordChanged() {
        ToastUtils.show(R.string.change_password_success)
        if (isLoggedUser()) {
            UserUtils.logout(null)
        } else {
            activity?.finish()
        }
    }
}