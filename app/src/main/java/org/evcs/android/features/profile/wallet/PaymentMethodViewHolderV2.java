package org.evcs.android.features.profile.wallet;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.evcs.android.databinding.ViewCreditCardWrapperBinding;
import org.evcs.android.model.PaymentMethod;

public class PaymentMethodViewHolderV2 extends RecyclerView.ViewHolder {

    private CreditCardViewListener mListener;
    @NonNull ViewCreditCardWrapperBinding mBinding;

    public PaymentMethodViewHolderV2(View itemView) {
        super(itemView);
        mBinding = ViewCreditCardWrapperBinding.bind(itemView);
    }

    public void setPaymentMethod(PaymentMethod creditCardInformation) {
        if (creditCardInformation.card != null) {
            mBinding.creditCardNumber.setText(creditCardInformation.card.getLast4());
            mBinding.creditCardProvider.setImageResource(creditCardInformation.card.getBrand().getDrawable());
        }
    }

    public void setListeners(CreditCardViewListener listener) {
        mListener = listener;
        ((View) mBinding.creditCardProvider.getParent()).setOnClickListener(
                view -> mListener.onDetailClicked());
    }

    public void setDefault(boolean def) {
        mBinding.creditCardDefault.setVisibility(def? View.VISIBLE : View.GONE);
    }

    public interface CreditCardViewListener {
        void onStarClicked();
        void onTrashClicked();
        void onDetailClicked();
    }

}
