package org.evcs.android.features.profile.wallet;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.databinding.ViewWalletHeaderBinding;
import org.evcs.android.features.shared.EVCSDialogFragment;
import org.evcs.android.model.PaymentMethod;
import org.evcs.android.ui.fragment.ErrorFragment;

import java.util.List;

public class WalletHeaderFragment extends ErrorFragment<PaymentMethodPresenter> implements IPaymentMethodView {

    RecyclerView mEndlessRecyclerView;
    FrameLayout mCreditCardsEmpty;

    private PaymentMethodAdapterV2 mCreditCardsAdapter;
    private boolean mInvalidateCreditCards = true;
    private WalletHeaderInterface mParent;

    public static WalletHeaderFragment newInstance() {

        Bundle args = new Bundle();

        WalletHeaderFragment fragment = new WalletHeaderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PaymentMethodPresenter createPresenter() {
        return new PaymentMethodPresenter(this, EVCSApplication.getInstance().getRetrofitServices());
    }

    @Override
    public int layout() {
        return R.layout.view_wallet_header;
    }

    @Override
    public void setUi(View v) {
        super.setUi(v);
        @NonNull ViewWalletHeaderBinding binding = ViewWalletHeaderBinding.bind(v);
        mEndlessRecyclerView = binding.creditCardsRecyclerView;
        mEndlessRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mCreditCardsEmpty = binding.creditCardViewEmpty;
        binding.walletAddNewPaymentMethod.setOnClickListener(view -> onAddPaymentMethodClicked());
    }

    @Override
    public void init() {

//        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (mInvalidateCreditCards) {
            mCreditCardsAdapter = new PaymentMethodAdapterV2();
        }
        mEndlessRecyclerView.setAdapter(mCreditCardsAdapter);
    }

    public void populate() {
        if (mInvalidateCreditCards) {
            showProgressDialog();
            getPresenter().getCreditCards();
        }
    }

    public void setListeners() {
        mCreditCardsAdapter.setOnItemClickListener(new PaymentMethodAdapterV2.CreditCardListener() {
            @Override
            public void onDetailClicked(@NonNull PaymentMethod item) {
                mParent.goToDetail(item);
            }

            @Override
            public void onStarClicked(@NonNull PaymentMethod item) {
                showMakeDefaultPaymentMethodDialog(item);
            }

            @Override
            public void onTrashClicked(@NonNull PaymentMethod item) {
                showRemovePaymentMethodDialog(item);
            }
        });

    }

    void onAddPaymentMethodClicked() {
        mInvalidateCreditCards = true;
        mParent.onAddPaymentMethodSelected();
    }

    public void showAndSavePaymentList(List<PaymentMethod> creditCardInformationList) {
        int height = mEndlessRecyclerView.getHeight();
        mCreditCardsAdapter.clear();
        if (creditCardInformationList.isEmpty()) {
            mCreditCardsEmpty.setVisibility(View.VISIBLE);
            mCreditCardsEmpty.setMinimumHeight(height);
        } else {
            mCreditCardsAdapter.appendTopAll(creditCardInformationList);
        }
    }

    private void showRemovePaymentMethodDialog(final PaymentMethod item) {
        new EVCSDialogFragment.Builder()
            .setTitle(getContext().getString(R.string.payment_method_dialog_remove_title))
            .setSubtitle(getString(R.string.payment_method_dialog_remove_subtitle))
            .addButton(getString(R.string.payment_method_dialog_remove_button), new EVCSDialogFragment.OnClickListener() {
                @Override
                public void onClick(@NonNull EVCSDialogFragment fragment) {
                    getPresenter().removePaymentMethod(item);
                    fragment.dismiss();
                }
            }/*, R.drawable.button_selector_ariel_red*/).showCancel(true)
            .setCancelable(true)
            .show(getFragmentManager());
    }

    private void showMakeDefaultPaymentMethodDialog(final PaymentMethod item) {
        new EVCSDialogFragment.Builder()
            .setSubtitle(getString(R.string.payment_method_dialog_default_subtitle))
            .addButton(getString(R.string.app_yes), new EVCSDialogFragment.OnClickListener() {
                @Override
                public void onClick(@NonNull EVCSDialogFragment fragment) {
                    getPresenter().makeDefaultPaymentMethod(item);
                    fragment.dismiss();
                }
            }).showCancel(getString(R.string.app_no))
        .setCancelable(true)
        .show(getFragmentManager());
    }

    @Override
    public void onPaymentMethodsReceived(List<PaymentMethod> creditCardInformationList) {
        hideProgressDialog();
        mInvalidateCreditCards = false;
        showAndSavePaymentList(creditCardInformationList);
    }

    @Override
    public void onPaymentMethodsNotReceived() {
        hideProgressDialog();
    }

    @Override
    public void onPaymentMethodRemoved(@NonNull PaymentMethod item) {
        hideProgressDialog();
        mCreditCardsAdapter.remove(item);
    }

    @Override
    public void onDefaultPaymentMethodSet(@NonNull PaymentMethod item) {
        hideProgressDialog();
        mCreditCardsAdapter.setDefault(item);
    }

    public void setParent(WalletHeaderInterface parent) {
        mParent = parent;
    }

    public interface WalletHeaderInterface {
        void onAddPaymentMethodSelected();

        void goToDetail(PaymentMethod item);
    }
}
