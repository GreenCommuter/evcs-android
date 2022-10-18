package org.evcs.android.features.auth.register

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentRegisterBinding
import org.evcs.android.features.auth.AbstractAuthFragment
import org.evcs.android.features.auth.AuthView
import org.evcs.android.features.auth.initialScreen.AuthActivity
import org.evcs.android.features.shared.StandardTextField
import org.evcs.android.model.user.AuthUser
import org.evcs.android.model.user.AuthUser.TestAuthUser
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils
import org.evcs.android.util.validator.MatchingValidator
import org.evcs.android.util.validator.TextInputLayoutInterface
import org.evcs.android.util.validator.ValidatorManager
import kotlin.system.measureNanoTime

class RegisterFragment : AbstractAuthFragment<RegisterPresenter>(), AuthView {

    private lateinit var mLastNameInputLayout: StandardTextField
    private lateinit var mNameInputLayout: StandardTextField
    private lateinit var mConfirmEmailInputLayout: StandardTextField
    private lateinit var mEmailInputLayout: StandardTextField
    private lateinit var mPasswordInputLayout: StandardTextField
    private lateinit var mGoToLogin: TextView
    private lateinit var mPasswordHint: TextView
    private lateinit var mContinueButton: Button

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
        mConfirmEmailInputLayout = binding.fragmentRegisterConfirmEmailInput
        mPasswordInputLayout = binding.fragmentRegisterPasswordInput
        mPasswordHint = binding.fragmentRegisterPasswordHint
        mContinueButton = binding.fragmentRegisterButton
        mGoToLogin = binding.fragmentRegisterGotologin
        binding.fragmentRegisterStep.text = String.format(getString(R.string.fragment_register_step), 1)
    }

    override fun init() {
        super.init()
        mPasswordHint.text = resources.getString(
            R.string.register_password_hint,
            BaseConfiguration.Validations.PASSWORD_MIN_LENGTH
        )
        ViewUtils.addUnderlines(mGoToLogin)
        mValidatorManager.addValidator(MatchingValidator(mConfirmEmailInputLayout, mEmailInputLayout))
    }

    override fun setListeners() {
        mContinueButton.setOnClickListener { onButtonClick() }
        mGoToLogin.setOnClickListener { onLoginClick() }

    }

    override fun setEnableButton(validFields: Boolean) {
        mContinueButton.isEnabled = true
    }

    override fun emailTextInputLayout(): TextInputLayoutInterface = mEmailInputLayout
    override fun passwordTextInputLayout(): TextInputLayoutInterface = mPasswordInputLayout

    private fun onButtonClick() {
//        progressDialog.show()
//        presenter!!.register(
//            mNameInputLayout.text.toString() + " " + mLastNameInputLayout.text.toString(),
//            mEmailInputLayout.text.toString(),
//            mPasswordInputLayout.text.toString()
//        )
        UserUtils.saveAuthUser(TestAuthUser())
        Navigation.findNavController(requireView())
            .navigate(RegisterFragmentDirections.actionRegisterFragmentToRegisterFragmentVerify(""))
    }

    override fun onTokenSent() {
        progressDialog.dismiss()
        Navigation.findNavController(requireView())
            .navigate(RegisterFragmentDirections.actionRegisterFragmentToRegisterFragmentYourCar())
    }

    protected fun onLoginClick() {
        Navigation.findNavController(requireView()).popBackStack()
    }

    //Ver password

}