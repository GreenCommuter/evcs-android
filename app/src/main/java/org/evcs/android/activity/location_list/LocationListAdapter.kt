package org.evcs.android.activity.location_list

import android.view.ViewGroup
import org.evcs.android.R
import org.evcs.android.model.Location
import org.evcs.android.ui.adapter.BaseRecyclerAdapter

class LocationListAdapter : BaseRecyclerAdapter<Location, LocationListViewHolder>() {

    private var mOnXClickListener: ((Location) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationListViewHolder {
        return LocationListViewHolder(inflateView(parent, R.layout.adapter_list_locations))
    }

    override fun populate(holder: LocationListViewHolder?, item: Location?, position: Int) {
        holder?.setLocation(item!!)
        holder?.setOnXClickListener(mOnXClickListener)
    }

    fun setOnXClickListener(listener: ((Location) -> Unit)?) {
        mOnXClickListener = listener
    }

}
