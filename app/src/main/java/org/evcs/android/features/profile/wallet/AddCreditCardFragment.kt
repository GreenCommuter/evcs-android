package org.evcs.android.features.profile.wallet

import android.content.Intent
import org.evcs.android.features.shared.StandardTextField
import org.evcs.android.util.validator.ValidatorManager
import org.evcs.android.R
import androidx.annotation.CallSuper
import android.graphics.Typeface
import android.view.View
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import org.joda.time.format.DateTimeFormat
import org.evcs.android.EVCSApplication
import org.evcs.android.util.validator.CreditCardValidator
import org.evcs.android.util.validator.DateTextInputValidator
import org.evcs.android.util.validator.NonEmptyTextInputValidator
import org.evcs.android.util.watchers.DateFormatWatcher
import org.evcs.android.util.watchers.FourDigitCardFormatWatcher
import org.evcs.android.model.shared.RequestError
import com.base.core.util.ToastUtils
import com.stripe.android.ApiResultCallback
import com.stripe.android.SetupIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.*
import org.evcs.android.Configuration
import org.evcs.android.databinding.FragmentAddCreditCardBinding
import org.evcs.android.navigation.INavigationListener
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.view.shared.EVCSToolbar
import org.evcs.android.util.ViewUtils
import org.evcs.android.util.validator.ZipCodeTextInputValidator
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormatter
import java.lang.Exception

/**
 * Fragment that helps with the payment using BrainTree.
 * It shows a message and a popup to pay, and notifies its children when the payment was accepted.
 *
 * @param <P> Presenter extending [AddCreditCardPresenter]
</P> */
class AddCreditCardFragment : ErrorFragment<AddCreditCardPresenter<*>>(),
    AddCreditCardView {

    private lateinit var mZipcode: StandardTextField
    private lateinit var mCreditCardView: CreditCardView
    private lateinit var mCardNumber: StandardTextField
    private lateinit var mCardExpirationMonth: StandardTextField
    private lateinit var mCvv: StandardTextField
    private lateinit var mNext: Button
    private lateinit var mToolbar: EVCSToolbar

    private var listener: IBrainTreeListener? = null

    private lateinit var mValidatorManager: ValidatorManager
    private lateinit var mDateTimeFormatter: DateTimeFormatter

    private lateinit var mStripe: Stripe

    override fun layout(): Int {
        return R.layout.fragment_add_credit_card
    }

    @CallSuper
    override fun init() {
//        showProgressDialog();
        mCardNumber.editText!!.typeface = Typeface.MONOSPACE
        mDateTimeFormatter = DateTimeFormat.forPattern("MM/yy")
        presenter!!.getClientSecret();
    }

    override fun createPresenter(): AddCreditCardPresenter<*> {
        return AddCreditCardPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentAddCreditCardBinding.bind(v)
        mCreditCardView = binding.fragmentBraintreeCreditCard
        mCardNumber = binding.fragmentBraintreeCardNumber
        mCardExpirationMonth = binding.fragmentBraintreeCardExpirationMonth
        mCvv = binding.fragmentBraintreeCardCvv
        mZipcode = binding.fragmentBraintreeCardZipcode
        mNext = binding.fragmentBraintreeNext
        mToolbar = binding.fragmentAddCreditCardToolbar

        ViewUtils.setAdjustResize(binding.fragmentAddCreditCardLayout)
    }

    override fun setListeners() {
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
        mNext.setOnClickListener { onNextClicked() }
        mToolbar.setNavigationOnClickListener { finish() }
    }

    private fun getDate() : LocalDate {
        return mDateTimeFormatter.parseLocalDate(mCardExpirationMonth.text.toString())
    }

    private fun isDateValid(): Boolean {
        return getDate() != null && getDate().isAfter(LocalDate())
    }

    /**
     * Method called when the payment was successfully processed.
     *
     * @param paymentMethodNonce Payment method nonce
     */
    //    protected void onNewNonceCreated(PaymentMethodNonce paymentMethodNonce) {
    //        ToastUtils.show(getString(R.string.profile_billing_information_braintree_success));
    //    }
    override fun showError(requestError: RequestError) {
        hideProgressDialog()
        ToastUtils.show(requestError.body)
    }

    fun onNextClicked() {
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

    override fun onMakeDefaultFinished() {
        hideProgressDialog()
        listener!!.onAddCreditCardFragmentFinished()
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

    fun finish() {
        NavHostFragment.findNavController(this).popBackStack()
    }

    interface IBrainTreeListener : INavigationListener {
        /**
         * Method called when the [AddCreditCardFragment] finishes.
         */
        fun onAddCreditCardFragmentFinished()
    }
}