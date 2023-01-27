package org.evcs.android.features.profile.wallet

import org.evcs.android.ui.adapter.BaseRecyclerAdapter
import org.evcs.android.util.UserUtils
import org.evcs.android.features.profile.wallet.CreditCardView.CreditCardViewListener
import android.view.ViewGroup
import android.view.LayoutInflater
import org.evcs.android.R
import org.evcs.android.model.PaymentMethod

class PaymentMethodAdapterV2 : BaseRecyclerAdapter<PaymentMethod, PaymentMethodViewHolderV2>() {

    private var mListener: CreditCardListener? = null
    private var mDefaultPm: String? = null

    init {
        mDefaultPm = UserUtils.getLoggedUser().defaultPm
    }

    override fun populate(holder: PaymentMethodViewHolderV2, item: PaymentMethod, position: Int) {
        holder.setPaymentMethod(item)
        holder.setDefault(item.id == mDefaultPm)
        holder.setListeners(object : CreditCardViewListener {
            override fun onStarClicked() {
                if (mListener != null) mListener!!.onStarClicked(item)
            }

            override fun onTrashClicked() {
                if (mListener != null) mListener!!.onTrashClicked(item)
            }
        })
    }

    fun setOnItemClickListener(listener: CreditCardListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolderV2 {
        val v = LayoutInflater
            .from(parent.context).inflate(R.layout.view_credit_card_wrapper, parent, false)
        return PaymentMethodViewHolderV2(v)
    }

    fun setDefault(item: PaymentMethod) {
        mDefaultPm = item.id
        notifyDataSetChanged()
    }

    //    public CreditCard getDefault() {
    //        for (int i = 0; i < getItemCount(); i++) {
    //            if (get(i).card.isDefault()) return get(i).card;
    //        }
    //        return null;
    //    }
    interface CreditCardListener {
        fun onStarClicked(item: PaymentMethod?)
        fun onTrashClicked(item: PaymentMethod?)
    }
}