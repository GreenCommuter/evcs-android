package org.evcs.android.features.search

import android.view.ViewGroup
import org.evcs.android.R
import org.evcs.android.model.Location
import org.evcs.android.ui.adapter.BaseRecyclerAdapter

class SearchResultsAdapter : BaseRecyclerAdapter<Location, SearchResultsLocationViewHolder>() {

    private var mOnXClickListener: ((Location) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsLocationViewHolder {
        return SearchResultsLocationViewHolder(inflateView(parent, R.layout.adapter_search_results))
    }

    override fun populate(holder: SearchResultsLocationViewHolder?, item: Location?, position: Int) {
        holder?.setLocation(item!!)
        holder?.setOnXClickListener(mOnXClickListener)
    }

    fun setOnXClickListener(listener: ((Location) -> Unit)?) {
        mOnXClickListener = listener
    }

}
