package org.evcs.android.features.auth.register

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentRegisterBinding
import org.evcs.android.features.auth.AbstractAuthFragment
import org.evcs.android.features.auth.AuthView
import org.evcs.android.features.shared.StandardTextField
import org.evcs.android.model.user.AuthUser.Companion.TestAuthUser
import org.evcs.android.ui.view.shared.EVCSToolbar2
import org.evcs.android.util.UserUtils
import org.evcs.android.util.validator.*

class RegisterFragment : AbstractAuthFragment<RegisterPresenter>(), AuthView {

    private lateinit var mLastNameInputLayout: StandardTextField
    private lateinit var mNameInputLayout: StandardTextField
    private lateinit var mEmailInputLayout: StandardTextField
    private lateinit var mPasswordInputLayout: StandardTextField
    private lateinit var mPasswordHint: TextView
    private lateinit var mContinueButton: TextView
    private lateinit var mToolbar: EVCSToolbar2

    /**
     * Returns a new RegisterFragment instance.
     *
     * @return new instance.
     */
    fun newInstance(): RegisterFragment {
        val args = Bundle()
        val fragment = RegisterFragment()
        fragment.arguments = args
        return fragment
    }

    override fun layout(): Int {
        return R.layout.fragment_register
    }

    override fun createPresenter(): RegisterPresenter {
        return RegisterPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentRegisterBinding.bind(v)
        mNameInputLayout = binding.fragmentRegisterFirstNameInput
        mLastNameInputLayout = binding.fragmentRegisterFirstNameInput
        mEmailInputLayout = binding.fragmentRegisterEmailInput
        mPasswordInputLayout = binding.fragmentRegisterPasswordInput
        mPasswordHint = binding.fragmentRegisterPasswordHint
        mContinueButton = binding.fragmentRegisterButton
        mToolbar = binding.fragmentRegisterToolbar
    }

    override fun init() {
        super.init()
        mPasswordHint.text = resources.getString(
            R.string.register_password_hint,
            BaseConfiguration.Validations.PASSWORD_MIN_LENGTH
        )
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mNameInputLayout))
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mLastNameInputLayout))
        mValidatorManager.addValidator(EmailTextInputValidator(mEmailInputLayout))
        mValidatorManager.addValidator(PasswordTextInputValidator(mPasswordInputLayout))
        mValidatorManager.setOnAnyTextChangedListener { setEnableButton(mValidatorManager.areAllFieldsValid()) }
    }

    override fun setListeners() {
        mContinueButton.setOnClickListener { onButtonClick() }
        mToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    //TODO: replace for validFields
    override fun setEnableButton(validFields: Boolean) {
        mContinueButton.isEnabled = true
    }

    override fun emailTextInputLayout(): TextInputLayoutInterface = mEmailInputLayout
    override fun passwordTextInputLayout(): TextInputLayoutInterface = mPasswordInputLayout

    private fun onButtonClick() {
        //TODO: switch
//        progressDialog.show()
//        presenter!!.register(
//            mNameInputLayout.text.toString(),
//            mLastNameInputLayout.text.toString(),
//            mEmailInputLayout.text.toString(),
//            mPasswordInputLayout.text.toString()
//        )
        UserUtils.saveAuthUser(TestAuthUser())
        findNavController()
            .navigate(RegisterFragmentDirections.actionRegisterFragmentToRegisterFragmentYourCar())
    }

    override fun onTokenSent() {
        progressDialog.dismiss()
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.signInFragment, true).build()
        findNavController().navigate(
            RegisterFragmentDirections.actionRegisterFragmentToRegisterFragmentYourCar(), navOptions)
    }

    //Ver password

}