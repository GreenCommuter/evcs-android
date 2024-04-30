package org.evcs.android.features.auth.register

import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentRegisterCellPhoneBinding
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.ViewUtils.showOrHide
import org.evcs.android.util.validator.PhoneTextInputValidator
import org.evcs.android.util.validator.ValidatorManager

abstract class AbstractChangePhoneFragment : ErrorFragment<RegisterPresenterCellphone<RegisterViewCellphone>>(), RegisterViewCellphone {

    private lateinit var mBinding: FragmentRegisterCellPhoneBinding

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
        mBinding.fragmentRegisterCellphoneNumber.editText?.addTextChangedListener(
            PhoneNumberFormattingTextWatcher(BaseConfiguration.DEFAULT_LOCALE.country))
    }

    override fun populate() {
        mBinding.fragmentRegisterCellPhoneTitle.showOrHide(getTitle())
        mBinding.fragmentRegisterCellPhoneSubtitle.showOrHide(getSubtitle())
        if (!userCanExit())
            mBinding.fragmentRegisterCellPhoneToolbar.setNavigationText(null)
        mBinding.fragmentRegisterCellphoneSend.text = getButtonText()
    }

    protected open fun getButtonText(): CharSequence? {
        return getString(R.string.app_continue)
    }

    abstract fun getTitle(): String?

    abstract fun getSubtitle() : String?

    override fun setListeners() {
        mBinding.fragmentRegisterCellphoneSend.setOnClickListener { onButtonClick() }
        val validatorManager = ValidatorManager()
        validatorManager.addValidator(PhoneTextInputValidator(mBinding.fragmentRegisterCellphoneNumber))
        validatorManager.setOnAnyTextChangedListener { setEnableButton(validatorManager.areAllFieldsValid()) }
//        mBinding.fragmentRegisterCellphoneValidateLater.setOnClickListener {
//            (requireActivity() as VerifyPhoneActivity).onCancel()
//        }
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
        (requireActivity() as VerifyPhoneActivity).onCellphoneSent(mBinding.fragmentRegisterCellphoneNumber.text.toString())
    }

    abstract fun userCanExit(): Boolean

    override fun onBackPressed(): Boolean {
        return if (userCanExit()) super.onBackPressed() else true
    }
}