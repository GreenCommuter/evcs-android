package org.evcs.android.features.account

import android.view.View
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.ActivityChangeZipcodeBinding
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils
import org.evcs.android.util.validator.ValidatorManager
import org.evcs.android.util.validator.ZipCodeTextInputValidator


class ZipCodeFragment : ErrorFragment<UpdateUserPresenter>(), UpdateUserView {

    private lateinit var mValidatorManager: ValidatorManager
    private lateinit var mBinding: ActivityChangeZipcodeBinding

    override fun createPresenter(): UpdateUserPresenter {
        return UpdateUserPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {
        mValidatorManager = ValidatorManager()
    }

    override fun layout(): Int {
        return R.layout.activity_change_zipcode
    }

    override fun setUi(v: View) {
        mBinding = ActivityChangeZipcodeBinding.bind(v)
        super.setUi(v)
    }

    override fun populate() {
        super.populate()
        mValidatorManager.addValidator(ZipCodeTextInputValidator(mBinding.activityZipcodeText))

        mBinding.activityZipcodeText.editText?.setText(UserUtils.getLoggedUser().zipCode)
        mValidatorManager.setOnAnyTextChangedListener {
            mBinding.activityZipcodeSave.isEnabled = mValidatorManager.areAllFieldsValid()
        }
    }

    override fun setListeners() {
        mBinding.activityZipcodeSave.setOnClickListener {
            mBinding.activityZipcodeSave.isEnabled = false
            presenter.changeZipCode(mBinding.activityZipcodeText.text)
        }
        ViewUtils.setAdjustResize(mBinding.activityZipcodeLayout)
    }

    override fun showError(requestError: RequestError) {
        mBinding.activityZipcodeSave.isEnabled = true
        super.showError(requestError)
    }

    override fun onUserUpdate() {
        activity?.finish()
    }
}