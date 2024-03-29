package org.evcs.android.features.profile.wallet;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.evcs.android.databinding.ViewCreditCardItemBinding;
import org.evcs.android.model.PaymentMethod;

public class PaymentMethodViewHolder extends RecyclerView.ViewHolder {

    private final boolean mShowArrow;
    private CreditCardViewListener mListener;
    @NonNull ViewCreditCardItemBinding mBinding;

    public PaymentMethodViewHolder(View itemView, boolean showArrow) {
        super(itemView);
        mBinding = ViewCreditCardItemBinding.bind(itemView);
        mShowArrow = showArrow;
    }

    public void setPaymentMethod(PaymentMethod creditCardInformation) {
        if (creditCardInformation.card != null) {
            mBinding.creditCardNumber.setText(String.format("•••• %s", creditCardInformation.card.getLast4()));
            mBinding.creditCardProvider.setImageResource(creditCardInformation.card.getBrand().getDrawable());
        }
        mBinding.viewListButtonChevron.setVisibility(mShowArrow ? View.VISIBLE : View.GONE);
    }

    public void setListeners(CreditCardViewListener listener) {
        mListener = listener;
        mBinding.viewListButtonChevron.setOnClickListener(
                view -> mListener.onDetailClicked());
        ((View) mBinding.creditCardProvider.getParent()).setOnClickListener(
                view -> mListener.onStarClicked());
    }

    public void setDefault(boolean def) {
        mBinding.creditCardDefault.setVisibility(def? View.VISIBLE : View.GONE);
    }

    public interface CreditCardViewListener {
        void onStarClicked();
//        void onTrashClicked();
        void onDetailClicked();
    }

}
