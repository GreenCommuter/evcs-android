package org.evcs.android.features.profile.wallet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.viewpager2.widget.ViewPager2;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.databinding.ViewWalletHeaderBinding;
import org.evcs.android.model.PaymentMethod;
import org.evcs.android.model.shared.RequestError;

import java.util.List;

public class WalletHeaderView extends LinearLayout implements IPaymentMethodView {

    ViewPager2 mEndlessRecyclerView;
    CustomTabLayout mTabLayout;
    FrameLayout mCreditCardsEmpty;

    private PaymentMethodAdapterV2 mCreditCardsAdapter;
    private boolean mInvalidateCreditCards = true;
    private PaymentMethodPresenter mPresenter;
    private WalletHeaderInterface mParent;

    public WalletHeaderView(Context context) {
        super(context);
        init(context);
    }

    public WalletHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WalletHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PaymentMethodPresenter createPresenter() {
        return new PaymentMethodPresenter(this, EVCSApplication.getInstance().getRetrofitServices());
    }

    public void init(Context context) {
        @NonNull ViewWalletHeaderBinding binding = ViewWalletHeaderBinding.inflate(LayoutInflater.from(context), this, true);
        mEndlessRecyclerView = binding.creditCardsRecyclerView;
        mTabLayout = binding.creditCardsRecyclerViewDots;
        mCreditCardsEmpty = binding.creditCardViewEmpty;
        binding.walletAddNewPaymentMethod.setOnClickListener(view -> onAddPaymentMethodClicked());

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setClipChildren(false);
        setClipToPadding(false);

        mPresenter = createPresenter();

        if (mInvalidateCreditCards) {
            mCreditCardsAdapter = new PaymentMethodAdapterV2();
            mCreditCardsAdapter.appendBottom(new PaymentMethod());
        }
        mEndlessRecyclerView.setAdapter(mCreditCardsAdapter);

        populate();
        setListeners();
    }

    public void populate() {
        getPresenter().onViewCreated();
        if (mInvalidateCreditCards) getPresenter().getCreditCards();
    }

    private PaymentMethodPresenter getPresenter() {
        return mPresenter;
    }

    private String getString(@StringRes int id) {
        return getContext().getString(id);
    }

    public void setListeners() {
        mCreditCardsAdapter.setOnItemClickListener(new PaymentMethodAdapterV2.CreditCardListener() {
            @Override
            public void onStarClicked(PaymentMethod item) {
                showMakeDefaultPaymentMethodDialog(item);
            }

            @Override
            public void onTrashClicked(PaymentMethod item) {
                showRemovePaymentMethodDialog(item);
            }
        });

        mEndlessRecyclerView.setOffscreenPageLimit(1);

        final float spacing = getResources().getDimension(R.dimen.spacing_ariel_related) * 1.5f;
        mEndlessRecyclerView.setPageTransformer((page, position)
                -> page.setTranslationX(spacing * position));


    }

    void onAddPaymentMethodClicked() {
        mInvalidateCreditCards = true;
        mParent.onAddPaymentMethodSelected();
    }

    public void showAndSavePaymentList(List<PaymentMethod> creditCardInformationList) {
        int height = mEndlessRecyclerView.getHeight();
        mCreditCardsAdapter.clear();
        if (creditCardInformationList.isEmpty()) {
            mCreditCardsEmpty.setVisibility(VISIBLE);
            mCreditCardsEmpty.setMinimumHeight(height);
        } else {
            mCreditCardsAdapter.appendTopAll(creditCardInformationList);
        }
    }

    private void showRemovePaymentMethodDialog(final PaymentMethod item) {
//        WolmoDialogFragment dialog = new GreenCommuterNewDialogFragment.Builder()
//            .setTitle(getContext().getString(R.string.payment_method_dialog_remove_title))
//            .setSubtitle(getString(R.string.payment_method_dialog_remove_subtitle))
//            .addButton(getString(R.string.payment_method_dialog_remove_button), false, new GreenCommuterDialogFragment.OnClickListener() {
//                @Override
//                public void onClick(@NonNull GreenCommuterDialogFragment fragment) {
                    getPresenter().removePaymentMethod(item);
//                    fragment.dismiss();
//                }
//            }, R.drawable.button_selector_ariel_red).showCancel(true)
//            .setCancelable(true)
//            .build();
//        mParent.showDialog(dialog);
    }

    private void showMakeDefaultPaymentMethodDialog(final PaymentMethod item) {
//        WolmoDialogFragment dialog = new GreenCommuterNewDialogFragment.Builder()
//            .setSubtitle(getString(R.string.payment_method_dialog_default_subtitle))
//            .addButton(getString(R.string.app_yes), new GreenCommuterDialogFragment.OnClickListener() {
//                @Override
//                public void onClick(@NonNull GreenCommuterDialogFragment fragment) {
                    getPresenter().makeDefaultPaymentMethod(item);
//                    fragment.dismiss();
//                }
//            }).showCancel(getString(R.string.app_no))
//        .setCancelable(true)
//        .build();
//        mParent.showDialog(dialog);
    }

    @Override
    public void showError(@NonNull RequestError error) {
//        WolmoDialogFragment dialog = new GreenCommuterDialogFragment.Builder()
//                .setTitle(error.getTitle())
//                .setSubtitle(error.getBody())
//                .addButton(getString(R.string.app_ok), GreenCommuterDialogFragment.getDismissOnClickListener())
//                .build();
//        mParent.showDialog(dialog);
    }

    @Override
    public void onPaymentMethodsReceived(List<PaymentMethod> creditCardInformationList) {
        mInvalidateCreditCards = false;
        showAndSavePaymentList(creditCardInformationList);
        mTabLayout.setupWithViewPager(mEndlessRecyclerView);
    }

    @Override
    public void onPaymentMethodsNotReceived() {
    }

    @Override
    public void onPaymentMethodRemoved(@NonNull PaymentMethod item) {
        mCreditCardsAdapter.remove(item);
        mTabLayout.setupWithViewPager(mEndlessRecyclerView);
    }

    @Override
    public void onDefaultPaymentMethodSet(@NonNull PaymentMethod item) {
        mCreditCardsAdapter.setDefault(item);
    }

    public void setParent(WalletHeaderInterface parent) {
        mParent = parent;
    }

    public interface WalletHeaderInterface {
//        void showDialog(WolmoDialogFragment dialog);

        void onAddPaymentMethodSelected();
    }
}
