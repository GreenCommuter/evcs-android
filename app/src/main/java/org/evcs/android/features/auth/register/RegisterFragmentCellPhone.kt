package org.evcs.android.features.auth.register

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.navigation.Navigation
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentRegisterCellPhoneBinding
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.validator.PhoneTextInputValidator
import org.evcs.android.util.validator.ValidatorManager

class RegisterFragmentCellPhone : ErrorFragment<RegisterPresenterCellphone<RegisterViewCellphone>>(), RegisterViewCellphone {

    private lateinit var mBinding: FragmentRegisterCellPhoneBinding

    /**
     * Returns a new RegisterFragment instance.
     *
     * @return new instance.
     */
    fun newInstance(): RegisterFragmentCellPhone {
        val args = Bundle()
        val fragment = RegisterFragmentCellPhone()
        fragment.arguments = args
        return fragment
    }

    override fun layout(): Int {
        return R.layout.fragment_register_cell_phone
    }

    override fun createPresenter(): RegisterPresenterCellphone<RegisterViewCellphone> {
        return RegisterPresenterCellphone(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentRegisterCellPhoneBinding.bind(v)
    }

    override fun init() {
        mBinding.fragmentRegisterCellphoneNumber.editText?.addTextChangedListener(PhoneNumberFormattingTextWatcher(
            BaseConfiguration.DEFAULT_LOCALE.country))
        mBinding.fragmentRegisterStep.text =
            String.format(getString(R.string.fragment_register_step), 3)
    }

    override fun setListeners() {
        mBinding.fragmentRegisterCellphoneSend.setOnClickListener { onButtonClick() }
        val validatorManager = ValidatorManager()
        validatorManager.addValidator(PhoneTextInputValidator(mBinding.fragmentRegisterCellphoneNumber))
        validatorManager.setOnAnyTextChangedListener { setEnableButton(validatorManager.areAllFieldsValid()) }
    }

    fun setEnableButton(validFields: Boolean) {
        mBinding.fragmentRegisterCellphoneSend.isEnabled = validFields
    }

    private fun onButtonClick() {
        progressDialog.show()
        presenter!!.sendNumbertoVerify(
            mBinding.fragmentRegisterCellphoneNumber.text.toString()
        )
    }

    override fun onCellphoneSent() {
        progressDialog.dismiss()
        Navigation.findNavController(requireView())
            .navigate(RegisterFragmentCellPhoneDirections.actionRegisterFragmentCellPhoneToRegisterFragmentVerify(
                mBinding.fragmentRegisterCellphoneNumber.text.toString()
            ))
    }
}