package org.evcs.android.activity.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.evcs.android.databinding.AdapterMapCarouselItemBinding
import org.evcs.android.databinding.AdapterSearchResultsBinding
import org.evcs.android.model.Location

class SearchResultsLocationViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    private val mBinding: AdapterSearchResultsBinding

    init {
        mBinding = AdapterSearchResultsBinding.bind(itemView!!)
    }

    fun setLocation(location: Location) {
        mBinding.adapterSearchName.text = location.name
        mBinding.adapterSearchDistance.text = location.printableDistance
        mBinding.adapterSearchAddress.text = location.address.toString()
    }

}
