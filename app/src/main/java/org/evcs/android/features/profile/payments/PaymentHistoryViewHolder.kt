package org.evcs.android.features.profile.payments

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.evcs.android.R
import org.evcs.android.databinding.AdapterPaymentHistoryItemBinding
import org.evcs.android.features.shared.setText
import org.evcs.android.model.Payment

class PaymentHistoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var mPayment: Payment
    private val mBinding: AdapterPaymentHistoryItemBinding

    init {
        mBinding = AdapterPaymentHistoryItemBinding.bind(itemView)
    }

    fun setPayment(payment: Payment) {
        mPayment = payment
        if (payment.createdAt != null)
            mBinding.adapterPaymentHistoryItemDate.setText(payment.createdAt)
        mBinding.adapterPaymentHistoryItemStation.text = payment.description?: payment.id
        mBinding.adapterPaymentHistoryItemPrice.text = itemView.context.getString(R.string.app_price_format, payment.amount)
    }

}
