package org.evcs.android.features.map

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.evcs.android.databinding.AdapterMapCarouselItemBinding
import org.evcs.android.model.Location

class LocationViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    private val mBinding: AdapterMapCarouselItemBinding

    init {
        mBinding = AdapterMapCarouselItemBinding.bind(itemView!!)
    }

    fun setLocation(location: Location) {
        mBinding.adapterMapName.text = location.name
        mBinding.adapterMapDistance.text = String.format("%.1f mi", location.distance)
        mBinding.adapterMapAddress.text = location.address.toString()
    }
}