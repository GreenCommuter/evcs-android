package org.evcs.android.features.charging

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.evcs.android.R
import org.evcs.android.databinding.AdapterChargingHistoryItemBinding
import org.evcs.android.features.shared.setText
import org.evcs.android.model.Charge

class ChargingHistoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var mCharge: Charge
    private val mBinding: AdapterChargingHistoryItemBinding

    init {
        mBinding = AdapterChargingHistoryItemBinding.bind(itemView)
    }

    fun setCharge(charge: Charge) {
        mCharge = charge
        if (charge.startedAt != null)
            mBinding.adapterChargingHistoryItemDate.setText(charge.startedAt)
        mBinding.adapterChargingHistoryItemStation.text = charge.locationName
        mBinding.adapterChargingHistoryItemPrice.text = itemView.context.getString(R.string.app_price_format, charge.price)
    }

}
