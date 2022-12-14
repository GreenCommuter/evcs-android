package org.evcs.android.activity.payment

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.EVCSApplication
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityAddCreditCardBinding
import org.evcs.android.util.validator.*
import org.evcs.android.util.watchers.DateFormatWatcher
import org.evcs.android.util.watchers.FourDigitCardFormatWatcher
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class AddCreditCardActivity : BaseActivity2(), AddCreditCardActivityView {

    private lateinit var mPresenter: AddCreditCardActivityPresenter
    private lateinit var mDateTimeFormatter: DateTimeFormatter
    private lateinit var mValidatorManager: ValidatorManager
    private lateinit var mBinding: ActivityAddCreditCardBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityAddCreditCardBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mDateTimeFormatter = DateTimeFormat.forPattern("MM/yy")
        createPresenter()
    }

    private fun createPresenter() {
        mPresenter = AddCreditCardActivityPresenter(this, EVCSApplication.getInstance().retrofitServices)
        mPresenter.onViewCreated()
    }

    override fun setListeners() {
        mBinding.activityAddCreditCardToolbar.setNavigationOnClickListener { finish() }

        mValidatorManager = ValidatorManager()
        mValidatorManager.addValidator(CreditCardValidator(mBinding.activityAddCreditCardNumber))
        mValidatorManager.addValidator(
            DateTextInputValidator(mBinding.activityAddCreditCardExpirationDate, mDateTimeFormatter)
        )
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mBinding.fragmentAddCreditCardCvv))
        mValidatorManager.setOnAnyTextChangedListener{
            mBinding.activityAddCreditCardSave.isEnabled = (mValidatorManager.areAllFieldsValid()
                    && isDateValid())
        }
        mBinding.activityAddCreditCardExpirationDate.editText?.addTextChangedListener(DateFormatWatcher())
        mBinding.activityAddCreditCardNumber.editText?.addTextChangedListener(FourDigitCardFormatWatcher())
        mBinding.activityAddCreditCardSave.setOnClickListener {
            mPresenter.addCreditCard(
                mBinding.activityAddCreditCardNumber.text,
                mBinding.activityAddCreditCardExpirationDate.text,
                mBinding.fragmentAddCreditCardCvv.text
            )
        }
    }

    private fun getDate(): LocalDate? {
        return mDateTimeFormatter.parseLocalDate(mBinding.activityAddCreditCardExpirationDate.text.toString())
    }

    private fun isDateValid(): Boolean {
        return getDate() != null && getDate()!!.isAfter(LocalDate())
    }
}