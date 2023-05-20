package org.evcs.android.features.profile.payments

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.evcs.android.databinding.AdapterPaymentHistoryItemBinding
import org.evcs.android.model.Payment
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class PaymentHistoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var mPayment: Payment
    private val mDateTimeFormatter: DateTimeFormatter
    private val mBinding: AdapterPaymentHistoryItemBinding

    init {
        mBinding = AdapterPaymentHistoryItemBinding.bind(itemView)
        mDateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy")
    }

    fun setPayment(payment: Payment) {
        mPayment = payment
        if (payment.createdAt != null)
            mBinding.adapterPaymentHistoryItemDate.text = mDateTimeFormatter.print(payment.createdAt)
//        mBinding.adapterPaymentHistoryItemStation.text = payment.locationName
        mBinding.adapterPaymentHistoryItemPrice.text = String.format("$%.2f", payment.amount)
    }

}
