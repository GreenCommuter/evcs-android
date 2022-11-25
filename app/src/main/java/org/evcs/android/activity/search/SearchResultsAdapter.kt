package org.evcs.android.activity.search

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import org.evcs.android.R
import org.evcs.android.model.Location
import org.evcs.android.ui.adapter.BaseRecyclerAdapter

class SearchResultsAdapter : BaseRecyclerAdapter<Location, SearchResultsLocationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsLocationViewHolder {
        return SearchResultsLocationViewHolder(inflateView(parent, R.layout.adapter_search_results))
    }

    override fun populate(holder: SearchResultsLocationViewHolder?, item: Location?, position: Int) {
        holder?.setLocation(item!!)
    }

//    fun setOnXClickListener(listener: View.OnClickListener) {
//
//    }

}
