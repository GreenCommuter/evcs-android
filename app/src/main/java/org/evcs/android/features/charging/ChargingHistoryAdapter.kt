package org.evcs.android.features.charging

import android.view.ViewGroup
import org.evcs.android.R
import org.evcs.android.model.Charge
import org.evcs.android.ui.adapter.BaseRecyclerAdapter

class ChargingHistoryAdapter : BaseRecyclerAdapter<Charge, ChargingHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargingHistoryViewHolder {
        return ChargingHistoryViewHolder(inflateView(parent, R.layout.adapter_charging_history_item))
    }

    override fun populate(holder: ChargingHistoryViewHolder, item: Charge, position: Int) {
        holder.setCharge(item)
    }

}
