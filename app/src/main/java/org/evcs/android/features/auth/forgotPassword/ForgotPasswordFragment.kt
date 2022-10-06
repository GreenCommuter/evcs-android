package org.evcs.android.features.auth.forgotPassword

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.base.core.fragment.BaseFragment
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentForgotPasswordBinding
import org.evcs.android.model.shared.RequestError
import org.evcs.android.util.validator.ValidatorManager

class ForgotPasswordFragment : BaseFragment<ForgotPasswordPresenter>(), ForgotPasswordView {

    private lateinit var mValidatorManager: ValidatorManager
    private var mEnableBack = true
    private lateinit var mButton: Button

    fun newInstance(): ForgotPasswordFragment {
        val args = Bundle()
        val fragment = ForgotPasswordFragment()
        fragment.arguments = args
        return fragment
    }

    override fun layout(): Int {
        return R.layout.fragment_forgot_password
    }

    override fun createPresenter(): ForgotPasswordPresenter {
        return ForgotPasswordPresenter(
            this,
            EVCSApplication.getInstance().retrofitServices
        )
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentForgotPasswordBinding.bind(v)
        mButton = binding.fragmentForgotPasswordButton
    }

    override fun setListeners() {
        mValidatorManager.setOnAnyTextChangedListener { setEnableButton(mValidatorManager.areAllFieldsValid()) }
        mButton.setOnClickListener { onButtonClick() }

    }

    protected fun setEnableButton(validFields: Boolean) {
        mButton.isEnabled = validFields
    }

    fun onButtonClick() {
        mEnableBack = false
        mButton.isEnabled = false
    }

    override fun onResetRequest() {
        mEnableBack = true
    }

    override fun init() {
        mValidatorManager = ValidatorManager()
    }

    override fun showError(error: RequestError) {
        mButton.isEnabled = true
        mEnableBack = true
        ToastUtils.show(error.body)
    }

}