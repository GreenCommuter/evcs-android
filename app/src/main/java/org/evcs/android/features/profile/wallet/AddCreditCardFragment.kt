package org.evcs.android.features.profile.wallet

import android.content.Context
import org.evcs.android.ui.fragment.LoadingFragment
import org.evcs.android.features.profile.wallet.AddCreditCardPresenter
import org.evcs.android.features.profile.wallet.IBrainTreeView
import org.evcs.android.features.profile.wallet.CreditCardView
import org.evcs.android.features.shared.StandardTextField
import org.evcs.android.features.profile.wallet.AddCreditCardFragment.IBrainTreeListener
import org.evcs.android.util.validator.ValidatorManager
import org.evcs.android.R
import androidx.annotation.CallSuper
import android.graphics.Typeface
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import org.joda.time.format.DateTimeFormat
import org.evcs.android.EVCSApplication
import org.evcs.android.features.profile.wallet.WalletNavigationController
import org.evcs.android.util.validator.CreditCardValidator
import org.evcs.android.util.validator.DateTextInputValidator
import org.evcs.android.util.validator.NonEmptyTextInputValidator
import org.evcs.android.util.validator.ValidatorManager.OnAnyTextChangedListener
import org.evcs.android.util.validator.TextInputLayoutInterface
import org.evcs.android.util.watchers.DateFormatWatcher
import org.evcs.android.util.watchers.FourDigitCardFormatWatcher
import org.evcs.android.model.shared.RequestError
import com.base.core.util.ToastUtils
import com.stripe.android.model.CardParams
import com.stripe.android.Stripe
import org.evcs.android.databinding.FragmentBraintreeBinding
import org.evcs.android.navigation.INavigationListener
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormatter

/**
 * Fragment that helps with the payment using BrainTree.
 * It shows a message and a popup to pay, and notifies its children when the payment was accepted.
 *
 * @param <P> Presenter extending [AddCreditCardPresenter]
</P> */
class AddCreditCardFragment : LoadingFragment<AddCreditCardPresenter<*>?>(), IBrainTreeView {
    //        PaymentMethodNonceCreatedListener, BraintreeListener, BraintreeErrorListener {
    private lateinit var mCreditCardView: CreditCardView
    private lateinit var mCardNumber: StandardTextField
    private lateinit var mCardExpirationMonth: StandardTextField
    private lateinit var mCvv: StandardTextField
    private lateinit var mNext: Button

    private var listener: IBrainTreeListener? = null

    private lateinit var mValidatorManager: ValidatorManager
    private lateinit var mDateTimeFormatter: DateTimeFormatter

    override fun layout(): Int {
        return R.layout.fragment_braintree
    }

    @CallSuper
    override fun init() {
//        showProgressDialog();
        presenter!!.getTokenEndpoint()
        //        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mCardNumber.editText!!.typeface = Typeface.MONOSPACE
        mDateTimeFormatter = DateTimeFormat.forPattern("MM/yy")
    }

    override fun createPresenter(): AddCreditCardPresenter<*> {
        return AddCreditCardPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentBraintreeBinding.bind(v)
        mCreditCardView = binding.fragmentBraintreeCreditCard
        mCardNumber = binding.fragmentBraintreeCardNumber
        mCardExpirationMonth = binding.fragmentBraintreeCardExpirationMonth
        mCvv = binding.fragmentBraintreeCardCvv
        mNext = binding.fragmentBraintreeNext
    }

    fun setListener() {
        listener = WalletNavigationController.getInstance()
    }

    override fun setListeners() {
        setListener()
        mValidatorManager = ValidatorManager()
        mValidatorManager.addValidator(CreditCardValidator(mCardNumber))
        mValidatorManager.addValidator(
            DateTextInputValidator(mCardExpirationMonth, mDateTimeFormatter)
        )
        mValidatorManager.addValidator(NonEmptyTextInputValidator(mCvv))
        mValidatorManager.setOnAnyTextChangedListener {
            mNext.isEnabled = mValidatorManager.areAllFieldsValid() && isDateValid()
        }
        mCardExpirationMonth.editText!!.addTextChangedListener(DateFormatWatcher())
        mCardNumber.editText!!.addTextChangedListener(FourDigitCardFormatWatcher())
        mCreditCardView.watchNumber(mCardNumber.editText)
        mNext.setOnClickListener { onNextClicked() }
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

    override fun onTokenResponse(token: String) {
        hideProgressDialog()
        mCardNumber.requestFocus()
        //        KeyboardUtils.showKeyboard() doesn't work;
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    override fun onTokenError() {
        hideProgressDialog()
        ToastUtils.show(getString(R.string.braintree_error_token))
    }

    fun onNextClicked() {
        showProgressDialog()
//        val (brand, loggingTokens, number, expMonth, expYear, cvc, name, address, currency, metadata) = CardParams(
//            mCardExpirationMonth!!.text.toString(),
//            date!!.monthOfYear,
//            date!!.year,
//            mCvv!!.text.toString()
//        )
//        val stripe = Stripe(context!!, "pk_test_6pRNASCoBOKtIshFeQd4XMUh")

//        stripe.createCardToken(cardParams, "YOUR-API-KEY", new ApiResultCallback<Token>() {
//            @Override
//            public void onSuccess(@NonNull Token token) {
//                onTokenResponse(token);
//            }
//
//            @Override
//            public void onError(@NonNull Exception e) {
//
//            }
//        });
    }

    //    @Override
    //    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
    ////        hideProgressDialog();
    //        onNewNonceCreated(paymentMethodNonce);
    //        getPresenter().getTokenAndMakeDefaultPaymentMethod(mCardNumber.getText().toString());
    //    }
    override fun onMakeDefaultFinished() {
        hideProgressDialog()
        listener!!.onAddCreditCardFragmentFinished()
    }

    //    @Override
    //    public void onError(Exception error) {
    //        hideProgressDialog();
    //    }
    interface IBrainTreeListener : INavigationListener {
        /**
         * Method called when the [AddCreditCardFragment] finishes.
         */
        fun onAddCreditCardFragmentFinished()
    }
}