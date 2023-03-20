package org.evcs.android.features.profile.wallet;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.databinding.ViewWalletHeaderBinding;
import org.evcs.android.features.shared.EVCSDialogFragment;
import org.evcs.android.features.shared.EVCSSliderDialogFragment;
import org.evcs.android.model.PaymentMethod;
import org.evcs.android.model.shared.RequestError;
import org.evcs.android.model.user.User;
import org.evcs.android.ui.fragment.ErrorFragment;
import org.evcs.android.util.UserUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WalletHeaderFragment extends ErrorFragment<WalletHeaderPresenter> implements IWalletHeaderView {

    RecyclerView mEndlessRecyclerView;

    private PaymentMethodAdapterV2 mCreditCardsAdapter;
    private WalletHeaderInterface mParent;

    public static WalletHeaderFragment newInstance() {

        Bundle args = new Bundle();

        WalletHeaderFragment fragment = new WalletHeaderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public WalletHeaderPresenter createPresenter() {
        return new WalletHeaderPresenter(this, EVCSApplication.getInstance().getRetrofitServices());
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
        binding.walletAddNewPaymentMethod.setOnChangeClickListener(view -> onAddPaymentMethodClicked());
    }

    @Override
    public void init() {

//        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mCreditCardsAdapter = new PaymentMethodAdapterV2();
        mEndlessRecyclerView.setAdapter(mCreditCardsAdapter);
    }

    public void populate() {
        showProgressDialog();
        getPresenter().getCreditCards();
    }

    public void setListeners() {
        mCreditCardsAdapter.setOnItemClickListener(new PaymentMethodAdapterV2.CreditCardListener() {
            @Override
            public void onDetailClicked(@NonNull PaymentMethod item) {
                mParent.goToDetail(item);
            }

            @Override
            public void onStarClicked(@NonNull PaymentMethod item) {
                if (!item.getId().equals(UserUtils.getLoggedUser().getDefaultPm())) {
                    if (item.isCreditCard()) {
                        showProgressDialog();
                        getPresenter().makeDefaultPaymentMethod(item);
                    } else {
                        showGooglePayDialog(item);
                    }
            }

//            @Override
//            public void onTrashClicked(@NonNull PaymentMethod item) {
//                showRemovePaymentMethodDialog(item);
//            }
        });

    }

    private void showGooglePayDialog(PaymentMethod item) {
        new EVCSSliderDialogFragment.Builder()
                .setTitle("What would you like to do?")
                .addButton("Set As Default", fragment -> {
                    getPresenter().makeDefaultPaymentMethod(item);
                    fragment.dismiss();
                }, R.drawable.layout_corners_rounded_blue)
                .addButton("Remove", fragment -> {
                    getPresenter().removePaymentMethod(item);
                    fragment.dismiss();
                }, R.drawable.layout_corners_rounded_danger)
                .setCancelable(true)
                .show(getFragmentManager());
    }

    void onAddPaymentMethodClicked() {
        mParent.onAddPaymentMethodSelected(false, getPresenter().hasGooglePay());
    }

    public void showAndSavePaymentList(List<PaymentMethod> creditCardInformationList) {
        mCreditCardsAdapter.clear();
        if (!creditCardInformationList.isEmpty()) {
            mCreditCardsAdapter.appendTopAll(creditCardInformationList);
        } else {
            mParent.onAddPaymentMethodSelected(true, getPresenter().hasGooglePay());
        }
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
    public void onDefaultPaymentMethodSet(@NotNull PaymentMethod item) {
        hideProgressDialog();
        mCreditCardsAdapter.setDefault(item);
        ((WalletActivity) getActivity()).onPaymentMethodChanged(mCreditCardsAdapter.getDefault());
    }

    @Override
    public void onMakeDefaultError(@NonNull RequestError error) {
        //TODO: remove payment method
        showError(error);
    }

    public void setParent(WalletHeaderInterface parent) {
        mParent = parent;
    }

    public interface WalletHeaderInterface {
        void onAddPaymentMethodSelected(boolean clearStack, boolean hasGooglePay);

        void goToDetail(PaymentMethod item);
    }
}
