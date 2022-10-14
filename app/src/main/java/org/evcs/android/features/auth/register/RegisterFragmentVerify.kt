package org.evcs.android.features.auth.register

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.navigation.Navigation
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentRegisterEnterCodeBinding
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.ViewUtils

class RegisterFragmentVerify : ErrorFragment<RegisterPresenterVerify>(), RegisterViewVerify {

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
        mPreviousNumber = requireArguments().getString("previous_number")
        ViewUtils.addUnderlines(mBinding.fragmentRegisterEnterCodeEdit)
        ViewUtils.addUnderlines(mBinding.fragmentRegisterEnterCodeResend)
        mBinding.fragmentRegisterStep.text =
            String.format(getString(R.string.fragment_register_step), 4)
    }

    override fun setListeners() {
        mBinding.fragmentRegisterEnterCodeButton.setOnClickListener { onButtonClick() }
        mBinding.fragmentRegisterEnterCodeEdit.setOnClickListener { Navigation.findNavController(requireView()).popBackStack() }
        mBinding.fragmentRegisterEnterCodeResend.setOnClickListener { presenter!!.sendNumbertoVerify(mPreviousNumber!!) }
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

    override fun onCellphoneVerified() {
//        TODO("Not yet implemented")
        progressDialog.dismiss()
    }

}