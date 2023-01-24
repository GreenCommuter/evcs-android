package org.evcs.android.features.profile.wallet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import org.evcs.android.R;
import org.evcs.android.model.PaymentMethod;
import org.evcs.android.ui.adapter.BaseRecyclerAdapter;
import org.evcs.android.util.UserUtils;

public class PaymentMethodAdapterV2 extends BaseRecyclerAdapter<PaymentMethod, PaymentMethodViewHolderV2> {

    private CreditCardListener mListener;
    private String mDefaultPm;

    public PaymentMethodAdapterV2() {
        mDefaultPm = UserUtils.getLoggedUser().defaultPm;
    }

    @Override
    protected void populate(PaymentMethodViewHolderV2 holder, final PaymentMethod item,
                            int position) {
        holder.setPaymentMethod(item.card);
        holder.setDefault(item.id.equals(mDefaultPm));
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

    public void setDefault(PaymentMethod item) {
        mDefaultPm = item.id;
        notifyDataSetChanged();
    }

//    public CreditCard getDefault() {
//        for (int i = 0; i < getItemCount(); i++) {
//            if (get(i).card.isDefault()) return get(i).card;
//        }
//        return null;
//    }

    public interface CreditCardListener {
        void onStarClicked(PaymentMethod item);
        void onTrashClicked(PaymentMethod item);
    }
}
