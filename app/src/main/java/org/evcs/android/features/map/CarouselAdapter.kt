package org.evcs.android.features.map

import org.evcs.android.ui.adapter.BaseRecyclerAdapter
import android.view.ViewGroup
import org.evcs.android.R
import org.evcs.android.model.Location

class CarouselAdapter : BaseRecyclerAdapter<Location?, LocationViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(inflateView(parent, R.layout.adapter_map_carousel_item))
    }

    override fun populate(holder: LocationViewHolder?, item: Location?, position: Int) {
        holder?.setLocation(item!!)
    }

    //Ugly af
    fun find(location: Location): Location? {
        for (i in 0..itemCount - 1) {
            if (get(i)?.equals(location)!!) return get(i)
        }
        return null
    }

}