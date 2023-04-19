package org.evcs.android.activity.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.evcs.android.databinding.AdapterSearchResultsBinding
import org.evcs.android.model.Location

class SearchResultsLocationViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    private lateinit var mLocation: Location
    private val mBinding: AdapterSearchResultsBinding

    init {
        mBinding = AdapterSearchResultsBinding.bind(itemView!!)
    }

    fun setLocation(location: Location) {
        mLocation = location
        mBinding.adapterSearchName.text = location.name
        mBinding.adapterSearchAddress.text = location.address.toString()
    }

    fun setOnXClickListener(onXClickListener: ((Location) -> Unit)?) {
        mBinding.adapterSearchGo.setOnClickListener { onXClickListener?.invoke(mLocation) }
    }

}
