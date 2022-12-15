package org.evcs.android.features.profile.wallet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import org.evcs.android.R;
import org.evcs.android.model.CreditCard;
import org.evcs.android.ui.adapter.BaseRecyclerAdapter;

public class PaymentMethodAdapterV2 extends BaseRecyclerAdapter<CreditCard, PaymentMethodViewHolderV2> {

    private CreditCardListener mListener;

    @Override
    protected void populate(PaymentMethodViewHolderV2 holder, final CreditCard item,
                            int position) {
        holder.setPaymentMethod(item);
        holder.setListeners(new CreditCardView.CreditCardViewListener() {
            @Override
            public void onStarClicked() {
                if (mListener != null) mListener.onStarClicked(item);
            }

            @Override
            public void onTrashClicked() {
                if (mListener != null) mListener.onTrashClicked(item);
            }
        });
    }

    public void setOnItemClickListener(CreditCardListener listener) {
        mListener = listener;
    }

    @Override
    @NonNull
    public PaymentMethodViewHolderV2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
            .from(parent.getContext()).inflate(R.layout.view_credit_card_wrapper, parent, false);
        return new PaymentMethodViewHolderV2(v);
    }

    public void setDefault(int adapterPosition) {
        for (int i = 0; i < getItemCount(); i++) {
            get(i).setDefault(i == adapterPosition);
        }
        notifyDataSetChanged();
    }

    public CreditCard getDefault() {
        for (int i = 0; i < getItemCount(); i++) {
            if (get(i).isDefault()) return get(i);
        }
        return null;
    }

    public interface CreditCardListener {
        void onStarClicked(CreditCard item);
        void onTrashClicked(CreditCard item);
    }
}
