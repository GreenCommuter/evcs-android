package org.evcs.android.features.profile.wallet;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.evcs.android.databinding.ViewCreditCardWrapperBinding;
import org.evcs.android.model.CreditCard;

public class PaymentMethodViewHolderV2 extends RecyclerView.ViewHolder {

    CreditCardView mCreditCardView;
    ProgressBar mLoading;

    public PaymentMethodViewHolderV2(View itemView) {
        super(itemView);
        @NonNull ViewCreditCardWrapperBinding binding = ViewCreditCardWrapperBinding.bind(itemView);
        mCreditCardView = binding.creditCardView;
        mLoading = binding.creditCardViewLoading;
    }

    public void setPaymentMethod(CreditCard creditCardInformation) {
        mLoading.setVisibility(creditCardInformation.last4 == null ? View.VISIBLE : View.GONE);
        mCreditCardView.setCreditCard(creditCardInformation);
        mCreditCardView.showButtons(true);
    }

    public void setListeners(CreditCardView.CreditCardViewListener listener) {
        mCreditCardView.setListeners(listener);
    }

    public void setDefault(boolean def) {
        mCreditCardView.setDefault(def);
    }
}
