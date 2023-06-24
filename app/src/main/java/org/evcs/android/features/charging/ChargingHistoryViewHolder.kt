package org.evcs.android.features.charging

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.evcs.android.R
import org.evcs.android.databinding.AdapterChargingHistoryItemBinding
import org.evcs.android.model.Charge
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class ChargingHistoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var mCharge: Charge
    private val mDateTimeFormatter: DateTimeFormatter
    private val mBinding: AdapterChargingHistoryItemBinding

    init {
        mBinding = AdapterChargingHistoryItemBinding.bind(itemView)
        mDateTimeFormatter = DateTimeFormat.forPattern(itemView.context.getString(R.string.app_date_format))
    }

    fun setCharge(charge: Charge) {
        mCharge = charge
        if (charge.startedAt != null)
            mBinding.adapterChargingHistoryItemDate.text = mDateTimeFormatter.print(charge.startedAt)
        mBinding.adapterChargingHistoryItemStation.text = charge.locationName
        mBinding.adapterChargingHistoryItemPrice.text = itemView.context.getString(R.string.app_price_format, charge.price)
    }

}
