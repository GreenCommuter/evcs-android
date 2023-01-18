package org.evcs.android.features.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.annotation.CallSuper
import com.base.core.util.KeyboardUtils
import com.base.core.util.ToastUtils
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import org.evcs.android.R
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.fragment.LoadingFragment
import org.evcs.android.util.validator.EmailTextInputValidator
import org.evcs.android.util.validator.PasswordTextInputValidator
import org.evcs.android.util.validator.TextInputLayoutInterface
import org.evcs.android.util.validator.ValidatorManager
import java.util.*

/**
 * Abstract helper fragment to simplify checks in fields inside login or register screens.
 *
 * @param <T> Presenter type
 */
abstract class AbstractAuthFragment<T : AuthPresenter<*>> : LoadingFragment<T>(), AuthView {

    protected lateinit var mValidatorManager: ValidatorManager
    private lateinit var mCallbackManager: CallbackManager
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    companion object {
        private const val REQUEST_CODE = 1001
    }

    @CallSuper
    override fun init() {
        mValidatorManager = ValidatorManager()
        mValidatorManager.addValidator(EmailTextInputValidator(emailTextInputLayout()))
        mValidatorManager.addValidator(PasswordTextInputValidator(passwordTextInputLayout()))
        mValidatorManager.setOnAnyTextChangedListener { setEnableButton(mValidatorManager.areAllFieldsValid()) }
    }

    override fun setListeners() {
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .registerCallback(mCallbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {}
                override fun onCancel() {
                    showLoading(false)
                }

                override fun onError(exception: FacebookException) {
                    Log.i("FF:", exception.toString())
                    showLoading(false)
                }
            })
    }

    override fun onDestroyView() {
        KeyboardUtils.hideKeyboard(requireContext(), requireView())
        super.onDestroyView()
    }

    /**
     * Returns true if both fields (email and password) are valid.
     *
     * @return True if both fields are valid, false otherwise
     */
    protected fun areBothFieldsValid(): Boolean {
        return mValidatorManager.areAllFieldsValid()
    }

    /**
     * Returns the fragment's email TextInputLayout.
     * @return Email EditText
     */
    protected abstract fun emailTextInputLayout(): TextInputLayoutInterface

    /**
     * Returns the fragment's password TextInputLayout.
     * @return Password EditText
     */
    protected abstract fun passwordTextInputLayout(): TextInputLayoutInterface

    /**
     * Handler to enable or disable the "accept" button inside the fragment.
     * This method is called whenever there is a text change or focus change in both edit text.
     * @param validFields Both fields are valid
     */
    protected abstract fun setEnableButton(validFields: Boolean)

    fun onLoginWithGoogleClick() {
        showLoading(true)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_CODE)
    }

    fun onLoginWithFacebookClick() {
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            Arrays.asList("email")
        )
        showLoading(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    presenter.getAccessToken(account)
                    mGoogleSignInClient.signOut()
                } catch (e: ApiException) {
                    Log.w("signIn failed code=", e.statusCode.toString())
                    showLoading(false)
                }
            } else {
                showLoading(false)
            }
        }
    }

    override fun showAcceptTerms() {
        //To avoid repeating this we could show the dialog in ChooseService, but that would mean
        //making the dialog not cancelable.
        showLoading(false)
    }

    override fun showFacebookError() {
        showLoading(false)
    }

    override fun showError(error: RequestError) {
        showLoading(false)
        ToastUtils.show(error.body)
    }

    protected open fun showLoading(loading: Boolean) {
        if (loading) showProgressDialog() else hideProgressDialog()
    }

    override fun showErrorPopup(error: RequestError) {
        showLoading(false)
        ToastUtils.show(error.body)
    }

    override fun onTermsAcceptedSent() {
        presenter!!.getToken()
    }

    override fun onTermsAcceptedFailed() {
        //Nothing
    }

}