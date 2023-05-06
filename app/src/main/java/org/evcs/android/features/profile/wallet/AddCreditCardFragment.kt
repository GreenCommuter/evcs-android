package org.evcs.android.features.profile.wallet

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.annotation.CallSuper
import com.base.core.util.ToastUtils
import com.stripe.android.ApiResultCallback
import com.stripe.android.SetupIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.*
import org.evcs.android.Configuration
import org.evcs.android.R
import org.evcs.android.util.validator.*
import org.evcs.android.util.watchers.DateFormatWatcher
import org.evcs.android.util.watchers.FourDigitCardFormatWatcher

/**
 * Fragment that helps with the payment using BrainTree.
 * It shows a message and a popup to pay, and notifies its children when the payment was accepted.
 *
 * @param <P> Presenter extending [AddCreditCardPresenter]
</P> */
class AddCreditCardFragment : AbstractCreditCardFragment(),
    AddCreditCardView {

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

    override fun setListeners() {
        super.setListeners()
        mValidatorManager = ValidatorManager()
        mValidatorManager.addValidator(CreditCardValidator(mCardNumber))
        mValidatorManager.addValidator(
            DateTextInputValidator(mCardExpirationMonth, mDateTimeFormatter)
        )
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mCvv))
        mValidatorManager.addValidator(ZipCodeTextInputValidator(mZipcode))
        mValidatorManager.setOnAnyTextChangedListener {
            mNext.isEnabled = mValidatorManager.areAllFieldsValid() && isDateValid()
        }
        mCardExpirationMonth.editText!!.addTextChangedListener(DateFormatWatcher())
        mCardNumber.editText!!.addTextChangedListener(FourDigitCardFormatWatcher())
        mCreditCardView.watchNumber(mCardNumber.editText)
    }

//    private fun getDate() : LocalDate {
//        return mDateTimeFormatter.parseLocalDate(mCardExpirationMonth.text.toString())
//    }

    /**
     * Method called when the payment was successfully processed.
     *
     * @param paymentMethodNonce Payment method nonce
     */
    //    protected void onNewNonceCreated(PaymentMethodNonce paymentMethodNonce) {
    //        ToastUtils.show(getString(R.string.profile_billing_information_braintree_success));
    //    }

    override fun getButtonColor(): ColorStateList {
        return ColorStateList.valueOf(resources.getColor(R.color.evcs_primary_600))
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
        mStripe.confirmSetupIntent(this, presenter.getConfirmParams(card, zipcode))
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
                    ToastUtils.show("Payment method added")
                    presenter.makeDefaultPaymentMethod(result.intent.paymentMethod!!.id)

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

}