package org.evcs.android.features.profile.wallet

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import com.base.core.util.ToastUtils
import com.stripe.android.ApiResultCallback
import com.stripe.android.SetupIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.*
import org.evcs.android.Configuration
import org.evcs.android.R
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.shared.RequestError
import org.evcs.android.util.ViewUtils.setVisibility
import org.evcs.android.util.validator.*
import org.evcs.android.util.watchers.DateFormatWatcher
import org.evcs.android.util.watchers.FourDigitCardFormatWatcher

class AddCreditCardFragment : AbstractCreditCardFragment(), AddCreditCardView {

    private lateinit var mValidatorManager: ValidatorManager

    private lateinit var mStripe: Stripe

    companion object {
        fun newInstance(): AddCreditCardFragment {
            val args = Bundle()
            val fragment = AddCreditCardFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @CallSuper
    override fun init() {
//        showProgressDialog();
        super.init()
        presenter!!.getClientSecret();
    }

    override fun populate() {
        mSetDefault.setVisibility(!(activity as WalletActivity).mFinishOnClick)
        mSetDefault.setDescription(getString(R.string.add_credit_card_set_default))
    }

    override fun setListeners() {
        super.setListeners()
        mValidatorManager = ValidatorManager()
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mCardName))
        mValidatorManager.addValidator(CreditCardValidator(mCardNumber))
        mValidatorManager.addValidator(
            DateTextInputValidator(mCardExpirationMonth, mDateTimeFormatter)
        )
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mCvv))
        mValidatorManager.addValidator(ZipCodeTextInputValidator(mZipcode))
        mValidatorManager.setOnAnyTextChangedListener {
            mNext.isEnabled = mValidatorManager.areAllFieldsValid() && isDateValid()
            setFields()
        }
        mCardExpirationMonth.editText!!.addTextChangedListener(DateFormatWatcher())
        mCardNumber.editText!!.addTextChangedListener(FourDigitCardFormatWatcher())
        mCreditCardView.watchNumber(mCardNumber.editText)
    }

    override fun getButtonBackground(): Drawable {
        return resources.getDrawable(R.drawable.layout_corners_rounded_orange)
    }

    override fun getButtonText(): String {
        return getString(R.string.add_credit_card_ok)
    }

    override fun areFieldsEditable(): Boolean {
        return true
    }

    override fun onNextClicked() {
        showProgressDialog()
        mStripe = Stripe(requireContext(), Configuration.STRIPE_KEY)

        val card = PaymentMethodCreateParams.Card.Builder()
            .setNumber(mCardNumber.text.toString())
            .setExpiryYear(getDate().year)
            .setExpiryMonth(getDate().monthOfYear)
            .setCvc(mCvv.text.toString())
            .build()

        val zipcode = mZipcode.text.toString()
        val name = mCardName.text.toString()
        mStripe.confirmSetupIntent(this, presenter.getConfirmParams(name, card, zipcode))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Handle the result of stripe.confirmSetupIntent
        mStripe.onSetupResult(requestCode, data, object : ApiResultCallback<SetupIntentResult> {
            override fun onSuccess(result: SetupIntentResult) {
                hideProgressDialog()
                val setupIntent = result.intent
                val status = setupIntent.status
                if (status == StripeIntent.Status.Succeeded) {
                    // Setup completed successfully
                    onSetupCompletedSuccessfully(result.intent.paymentMethod!!.id!!)

                } else if (status == StripeIntent.Status.RequiresPaymentMethod) {
                    ToastUtils.show(setupIntent.lastSetupError!!.message!!)
                }
            }

            override fun onError(e: Exception) {
                hideProgressDialog()
                ToastUtils.show(e.toString())
            }
        })
    }

    private fun onSetupCompletedSuccessfully(id: String) {
        if (mSetDefault.isChecked or !mSetDefault.isVisible) {
            presenter.makeDefaultPaymentMethod(id)
        } else {
            ToastUtils.show(getString(R.string.add_credit_card_success))
            finish()
        }
    }

    override fun onDefaultPaymentMethodSet(item: PaymentMethod) {
        ToastUtils.show(getString(R.string.add_credit_card_success))
        finish()
        (activity as WalletActivity).onPaymentMethodChanged(item)
    }

    override fun onMakeDefaultError(error: RequestError) {
        presenter.getClientSecret()
    }

}