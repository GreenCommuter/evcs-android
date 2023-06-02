package org.evcs.android.features.auth.register

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentRegisterEnterCodeBinding
import org.evcs.android.features.auth.initialScreen.AuthActivity
import org.evcs.android.network.service.SMSBroadcastReceiver
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.ViewUtils
import org.evcs.android.util.validator.PasswordTextInputValidator
import org.evcs.android.util.validator.ValidatorManager

class RegisterFragmentVerify : ErrorFragment<RegisterPresenterVerify>(), RegisterViewVerify {

    private lateinit var startForResult: ActivityResultLauncher<Intent>
    private var mPreviousNumber: String? = null
    private lateinit var mBinding : FragmentRegisterEnterCodeBinding

    /**
     * Returns a new RegisterFragment instance.
     *
     * @return new instance.
     */
    fun newInstance(): RegisterFragmentVerify {
        val args = Bundle()
        val fragment = RegisterFragmentVerify()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result -> presenter.onConsentResult(result)
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_register_enter_code
    }

    override fun createPresenter(): RegisterPresenterVerify {
        return RegisterPresenterVerify(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentRegisterEnterCodeBinding.bind(v)
    }

    override fun init() {
        mBinding.fragmentRegisterEnterCodeText.editText?.addTextChangedListener(PhoneNumberFormattingTextWatcher(
            BaseConfiguration.DEFAULT_LOCALE.country))
        mPreviousNumber = RegisterFragmentVerifyArgs.fromBundle(requireArguments()).previousNumber
//        ViewUtils.addUnderlines(mBinding.fragmentRegisterEnterCodeEdit)
        ViewUtils.addUnderlines(mBinding.fragmentRegisterEnterCodeResend)
    }

    override fun setListeners() {
        mBinding.fragmentRegisterEnterCodeButton.setOnClickListener { onButtonClick() }
        mBinding.fragmentRegisterEnterCodeEdit.setOnClickListener { findNavController().popBackStack() }
        mBinding.fragmentRegisterEnterCodeResend.setOnClickListener { presenter?.sendNumbertoVerify(mPreviousNumber!!) }
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireActivity().registerReceiver(SMSBroadcastReceiver(presenter), intentFilter)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            presenter?.startSMSListener(requireContext())
        }
        val validatorManager = ValidatorManager();
        validatorManager.addValidator(PasswordTextInputValidator(mBinding.fragmentRegisterEnterCodeText))
        validatorManager.setOnAnyTextChangedListener{setEnableButton(validatorManager.areAllFieldsValid())}
        mBinding.fragmentRegisterEnterCodeToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    override fun openConsentDialog(consentIntent: Intent) {
        startForResult.launch(consentIntent)
    }

    fun setEnableButton(validFields: Boolean) {
        mBinding.fragmentRegisterEnterCodeButton.isEnabled = validFields
    }

    private fun onButtonClick() {
        progressDialog.show()
        presenter!!.sendCode(
            mBinding.fragmentRegisterEnterCodeText.text.toString()
        )
    }

    override fun onCellphoneSent() {
        progressDialog.dismiss()
    }

    override fun showCode(s: String?) {
        mBinding.fragmentRegisterEnterCodeText.editText?.setText(s)
    }

    override fun onCellphoneVerified() {
        (requireActivity() as AuthActivity).onAuthFinished()
        progressDialog.dismiss()
    }

}