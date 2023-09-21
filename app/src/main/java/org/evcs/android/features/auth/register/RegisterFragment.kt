package org.evcs.android.features.auth.register

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.base.core.util.NavigationUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentRegisterBinding
import org.evcs.android.databinding.FragmentSignInSocialBinding
import org.evcs.android.features.auth.AbstractAuthFragment
import org.evcs.android.features.auth.AuthView
import org.evcs.android.features.auth.initialScreen.AuthActivity
import org.evcs.android.features.main.MainActivity
import org.evcs.android.features.shared.StandardTextField
import org.evcs.android.util.Extras.VerifyActivity
import org.evcs.android.util.ViewUtils
import org.evcs.android.util.validator.EmailTextInputValidator
import org.evcs.android.util.validator.NonEmptyTextInputValidator
import org.evcs.android.util.validator.PasswordTextInputValidator
import org.evcs.android.util.validator.TextInputLayoutInterface

class RegisterFragment : AbstractAuthFragment<RegisterPresenter>(), AuthView {

    private lateinit var mLastNameInputLayout: StandardTextField
    private lateinit var mNameInputLayout: StandardTextField
    private lateinit var mEmailInputLayout: StandardTextField
    private lateinit var mPasswordInputLayout: StandardTextField
    private lateinit var mPasswordHint: TextView
    private lateinit var mContinueButton: TextView
    private lateinit var mRegisterSocial: FragmentSignInSocialBinding
    private lateinit var mGoToLogin: TextView

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
        mLastNameInputLayout = binding.fragmentRegisterLastNameInput
        mEmailInputLayout = binding.fragmentRegisterEmailInput
        mPasswordInputLayout = binding.fragmentRegisterPasswordInput
        mPasswordHint = binding.fragmentRegisterPasswordHint
        mContinueButton = binding.fragmentRegisterButton
        mRegisterSocial = binding.fragmentRegisterSocial
        mGoToLogin = binding.fragmentRegisterLogin
    }

    override fun init() {
        super.init()
        ViewUtils.addUnderlines(mGoToLogin)
        mPasswordHint.movementMethod = LinkMovementMethod.getInstance()
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mNameInputLayout))
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mLastNameInputLayout))
        mValidatorManager.addValidator(EmailTextInputValidator(mEmailInputLayout))
        mValidatorManager.addValidator(PasswordTextInputValidator(mPasswordInputLayout))
        mValidatorManager.setOnAnyTextChangedListener { setEnableButton(mValidatorManager.areAllFieldsValid()) }
    }

    override fun setListeners() {
        super.setListeners()
        mContinueButton.setOnClickListener { onButtonClick() }
        mRegisterSocial.fragmentSignInGoogle.setOnClickListener { onLoginWithGoogleClick() }
        mRegisterSocial.fragmentSignInFacebook.setOnClickListener { onLoginWithFacebookClick() }
        mGoToLogin.setOnClickListener {
            findNavController().popBackStack(R.id.rootFragment, false)
            findNavController().navigate(R.id.registerFragment)
            findNavController().navigate(R.id.signInFragment)
        }
    }

    override fun setEnableButton(validFields: Boolean) {
        mContinueButton.isEnabled = validFields
    }

    override fun emailTextInputLayout(): TextInputLayoutInterface = mEmailInputLayout
    override fun passwordTextInputLayout(): TextInputLayoutInterface = mPasswordInputLayout

    private fun onButtonClick() {
        presenter!!.register(
            mNameInputLayout.text.toString(),
            mLastNameInputLayout.text.toString(),
            mEmailInputLayout.text.toString(),
            mPasswordInputLayout.text.toString()
        )
    }

    override fun onTokenSent() {
        (activity as AuthActivity).goToVerify()
    }

    //Ver password

}