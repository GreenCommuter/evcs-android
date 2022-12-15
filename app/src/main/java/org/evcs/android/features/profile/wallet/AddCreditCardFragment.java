package org.evcs.android.features.profile.wallet;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.base.core.util.ToastUtils;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.CardParams;
import com.stripe.android.model.Token;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.databinding.FragmentBraintreeBinding;
import org.evcs.android.features.main.MainNavigationController;
import org.evcs.android.features.shared.StandardTextField;
import org.evcs.android.model.shared.RequestError;
import org.evcs.android.navigation.INavigationListener;
import org.evcs.android.ui.fragment.LoadingFragment;
import org.evcs.android.util.validator.CreditCardValidator;
import org.evcs.android.util.validator.DateTextInputValidator;
import org.evcs.android.util.validator.NonEmptyTextInputValidator;
import org.evcs.android.util.validator.ValidatorManager;
import org.evcs.android.util.watchers.DateFormatWatcher;
import org.evcs.android.util.watchers.FourDigitCardFormatWatcher;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Fragment that helps with the payment using BrainTree.
 * It shows a message and a popup to pay, and notifies its children when the payment was accepted.
 *
 * @param <P> Presenter extending {@link AddCreditCardPresenter}
 */
public class AddCreditCardFragment extends LoadingFragment<AddCreditCardPresenter>
    implements IBrainTreeView {
//        PaymentMethodNonceCreatedListener, BraintreeListener, BraintreeErrorListener {

    CreditCardView mCreditCardView;
    StandardTextField mCardNumber;
    StandardTextField mCardExpirationMonth;
    StandardTextField mCvv;
    Button mNext;

    private IBrainTreeListener mBrainTreeListener;

    private ValidatorManager mValidatorManager;
    private DateTimeFormatter mDateTimeFormatter;

    @Override
    public int layout() {
        return R.layout.fragment_braintree;
    }

    @Override
    @CallSuper
    public void init() {
//        showProgressDialog();
        getPresenter().getTokenEndpoint();
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mCardNumber.getEditText().setTypeface(Typeface.MONOSPACE);
        mDateTimeFormatter = DateTimeFormat.forPattern("MM/yy");
    }

    @Override
    public AddCreditCardPresenter createPresenter() {
        return new AddCreditCardPresenter(this, EVCSApplication.getInstance().getRetrofitServices());
    }

    @Override
    public void setUi(View v) {
        super.setUi(v);
        @NonNull FragmentBraintreeBinding binding = FragmentBraintreeBinding.bind(v);
        mCreditCardView = binding.fragmentBraintreeCreditCard;
        mCardNumber = binding.fragmentBraintreeCardNumber;
        mCardExpirationMonth = binding.fragmentBraintreeCardExpirationMonth;
        mCvv = binding.fragmentBraintreeCardCvv;
        mNext = binding.fragmentBraintreeNext;
    }

    public void setListener() {
        mBrainTreeListener = WalletNavigationController.getInstance();
    }

    @Override
    public void setListeners() {
        setListener();
        mValidatorManager = new ValidatorManager();
        mValidatorManager.addValidator(new CreditCardValidator(mCardNumber));
        mValidatorManager.addValidator(new DateTextInputValidator(mCardExpirationMonth, mDateTimeFormatter));
        mValidatorManager.addValidator(new NonEmptyTextInputValidator(mCvv));
        mValidatorManager.setOnAnyTextChangedListener(x ->
                mNext.setEnabled(mValidatorManager.areAllFieldsValid() && isDateValid()));
        mCardExpirationMonth.getEditText().addTextChangedListener(new DateFormatWatcher());
        mCardNumber.getEditText().addTextChangedListener(new FourDigitCardFormatWatcher());
        mCreditCardView.watchNumber(mCardNumber.getEditText());

        mNext.setOnClickListener(view -> { onNextClicked(); });
    }

    private LocalDate getDate() {
        return mDateTimeFormatter.parseLocalDate(mCardExpirationMonth.getText().toString());
    }

    private boolean isDateValid() {
        return getDate() != null && getDate().isAfter(new LocalDate());
    }

    /**
     * Returns the listener to notify events in this fragment
     *
     * @return Fragment Listener
     */
    protected final IBrainTreeListener getListener() {
        return mBrainTreeListener;
    }

    /**
     * Method called when the payment was successfully processed.
     *
     * @param paymentMethodNonce Payment method nonce
     */
//    protected void onNewNonceCreated(PaymentMethodNonce paymentMethodNonce) {
//        ToastUtils.show(getString(R.string.profile_billing_information_braintree_success));
//    }

    @Override
    public void showError(@NonNull RequestError requestError) {
        hideProgressDialog();
        ToastUtils.show(requestError.getBody());
    }

    @Override
    public void onTokenResponse(@NonNull String token) {
        hideProgressDialog();

        mCardNumber.requestFocus();
//        KeyboardUtils.showKeyboard() doesn't work;
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onTokenError() {
        hideProgressDialog();
        ToastUtils.show(getString(R.string.braintree_error_token));
    }

    public void onNextClicked() {
        showProgressDialog();

        CardParams cardParams = new CardParams(mCardExpirationMonth.getText().toString(),
                getDate().getMonthOfYear(),
                getDate().getYear(),
                mCvv.getText().toString());

        final Stripe stripe = new Stripe(getContext(), "pk_test_6pRNASCoBOKtIshFeQd4XMUh");

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

    @Override
    public void onMakeDefaultFinished() {
        hideProgressDialog();
        getListener().onAddCreditCardFragmentFinished();
    }

//    @Override
//    public void onError(Exception error) {
//        hideProgressDialog();
//    }

    public interface IBrainTreeListener extends INavigationListener {

        /**
         * Method called when the {@link AddCreditCardFragment} finishes.
         */
        void onAddCreditCardFragmentFinished();

    }
}
