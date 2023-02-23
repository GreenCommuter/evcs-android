package org.evcs.android.features.charging

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.evcs.android.databinding.AdapterChargingHistoryItemBinding
import org.evcs.android.model.Charge
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.PeriodFormatterBuilder

class ChargingHistoryViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!) {

    private lateinit var mCharge: Charge
    private val mDateTimeFormatter: DateTimeFormatter
    private val mBinding: AdapterChargingHistoryItemBinding

    init {
        mBinding = AdapterChargingHistoryItemBinding.bind(itemView!!)
        mDateTimeFormatter = DateTimeFormat.forPattern("MMM dd, yyyy 'at' hh:mm a")
    }

    fun setCharge(charge: Charge) {
        mCharge = charge
        if (charge.startedAt != null)
            mBinding.adapterChargingHistoryItemDate.text = "Date: " + mDateTimeFormatter.print(charge.startedAt)
        mBinding.adapterChargingHistoryItemPrice.text = "Total: " + String.format("$%.2f", charge.price)
        mBinding.adapterChargingHistoryItemDuration.text = charge.printableDuration
    }

//    fun setOnXClickListener(onXClickListener: ((Location) -> Unit)?) {
//        mBinding.adapterSearchRemove.setOnClickListener { onXClickListener?.invoke(mLocation) }
//    }

}
