package org.evcs.android.features.auth.signin

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.base.core.util.KeyboardUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentSignInBinding
import org.evcs.android.features.auth.AbstractAuthFragment
import org.evcs.android.features.auth.AuthView
import org.evcs.android.features.auth.initialScreen.AuthActivity
import org.evcs.android.features.shared.StandardTextField
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils
import org.evcs.android.util.validator.TextInputLayoutInterface

class SignInFragment : AbstractAuthFragment<SignInPresenter>(), AuthView {
    private lateinit var mEmailInputLayout: StandardTextField
    private lateinit var mPasswordInputLayout: StandardTextField
    private lateinit var mLoginButton: View
    private lateinit var mLoginFacebook: ImageButton
    private lateinit var mLoginGoogle: ImageButton
    private lateinit var binding: FragmentSignInBinding

    /**
     * Returns a new SignInFragment instance.
     *
     * @return new instance.
     */
    fun newInstance(): SignInFragment {
        val args = Bundle()
        val fragment = SignInFragment()
        fragment.arguments = args
        return fragment
    }

    override fun layout(): Int {
        return R.layout.fragment_sign_in
    }

    override fun createPresenter(): SignInPresenter {
        return SignInPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        binding = FragmentSignInBinding.bind(v)
        mEmailInputLayout = binding.fragmentSignInEmailInput
        mPasswordInputLayout = binding.fragmentSignInPasswordInput
        mLoginButton = binding.fragmentSignInButton
        mLoginFacebook = binding.fragmentSignInSocial.fragmentSignInFacebook
        mLoginGoogle = binding.fragmentSignInSocial.fragmentSignInGoogle
    }

    override fun populate() {
        mEmailInputLayout.editText!!.setText(UserUtils.getUserEmail())
        ViewUtils.addUnderlines(binding.fragmentSignInSignUp)
//        mLoginButton.setTypeface(null, Typeface.NORMAL)
    }

    override fun setListeners() {
        super.setListeners()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            val editText = mEmailInputLayout.editText!!
            editText.addTextChangedListener(object : TextWatcher {
                var previous: CharSequence? = null
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    previous = s.toString()
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (previous == UserUtils.getUserEmail() && start == 0) {
                        editText.setText(s.subSequence(start, start + count))
                        editText.setSelection(count)
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    //this method must be empty.
                }
            })
        }
        binding.fragmentSignInButton.setOnClickListener { onButtonClick() }
        binding.fragmentSignInForgotPassword.setOnClickListener { onForgotPasswordClick() }
        binding.fragmentSignInSignUp.setOnClickListener { onRegisterClick() }
        binding.fragmentSignInSocial.fragmentSignInGoogle.setOnClickListener { onLoginWithGoogleClick() }
        binding.fragmentSignInSocial.fragmentSignInFacebook.setOnClickListener { onLoginWithFacebookClick() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mEmailInputLayout.editText!!.setSelection(0)
        KeyboardUtils.showKeyboard(requireContext(), mEmailInputLayout.editText!!)
    }

    override fun emailTextInputLayout(): TextInputLayoutInterface = mEmailInputLayout
    override fun passwordTextInputLayout(): TextInputLayoutInterface = mPasswordInputLayout

    override fun setEnableButton(validFields: Boolean) {
        mLoginButton.isEnabled = validFields
    }

    fun onForgotPasswordClick() {
        findNavController()
            .navigate(SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment())
    }

    fun onButtonClick() {
        showLoading(true)
        presenter!!.logIn(
            mEmailInputLayout.text.toString(),
            mPasswordInputLayout.text.toString()
        )
//        onTokenSent()
    }

    override fun onTokenSent() {
        showLoading(false)
//        if (hasCompletedRegistration()) {
            (activity as AuthActivity).onAuthFinished()
//        } else {
//            findNavController()
//                .navigate(SignInFragmentDirections.actionSignInFragmentToRegisterFragmentYourCar())
//        }
    }

    private fun hasCompletedRegistration(): Boolean {
        return UserUtils.getLoggedUser().isPhoneVerified
    }

    protected fun onRegisterClick() {
        findNavController().popBackStack(R.id.rootFragment, false)
        findNavController().navigate(R.id.signInFragment)
        findNavController().navigate(R.id.registerFragment)
    }

    override fun showLoading(loading : Boolean) {
        mLoginButton.isActivated = loading
        binding.fragmentSignInProgressBar.visibility = if (loading) VISIBLE else INVISIBLE
    }

}