package org.evcs.android.features.profile.payments

import android.view.ViewGroup
import org.evcs.android.R
import org.evcs.android.model.Charge
import org.evcs.android.model.Payment
import org.evcs.android.ui.adapter.BaseRecyclerAdapter

class PaymentHistoryAdapter : BaseRecyclerAdapter<Payment, PaymentHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHistoryViewHolder {
        return PaymentHistoryViewHolder(inflateView(parent, R.layout.adapter_payment_history_item))
    }

    override fun populate(holder: PaymentHistoryViewHolder, item: Payment, position: Int) {
        holder.setPayment(item)
    }

}
