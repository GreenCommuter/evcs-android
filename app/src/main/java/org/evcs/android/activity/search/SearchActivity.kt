package org.evcs.android.activity.search

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLngBounds
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.ActivitySearchBinding
import org.evcs.android.features.map.MainMapFragment2
import org.evcs.android.model.Location
import org.evcs.android.ui.adapter.BaseRecyclerAdapterItemClickListener
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.recycler.EndlessRecyclerView
import org.evcs.android.util.LocationUtils

class SearchActivity : ErrorFragment<SearchActivityPresenter>(), SearchActivityView {

    private lateinit var mFailView: LinearLayout
    private lateinit var mEmptyView: TextView
    private lateinit var mAddress: String

    private lateinit var mHistoryRecycler: EndlessRecyclerView
    private lateinit var mHistoryAdapter: SearchResultsAdapter
    private lateinit var mParent: MainMapFragment2.LocationClickListener

    companion object {
        fun newInstance(): SearchActivity {
            val args = Bundle()
            val fragment = SearchActivity()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.activity_search
    }

    override fun setUi(v: View?) {
        super.setUi(v)
        val binding = ActivitySearchBinding.bind(v!!)
        mFailView = binding.activitySearchFail
        mEmptyView = binding.activitySearchEmpty
        mHistoryRecycler = binding.activitySearchHistory
    }

    override fun init() {
//        mPresenter.mFilterState = intent.getSerializableExtra(Extras.FilterActivity.FILTER_STATE) as FilterState
    }

    override fun populate() {
        super.populate()
        mHistoryRecycler.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        refreshHistoryView()
    }

    override fun setListeners() {
        super.setListeners()
        val itemClickListener = object : BaseRecyclerAdapterItemClickListener<Location>() {
            override fun onItemClicked(item: Location?, adapterPosition: Int) {
                mParent.onLocationClicked(item!!)
            }
        }
        mHistoryAdapter.setItemClickListener(itemClickListener)
    }

    override fun createPresenter(): SearchActivityPresenter {
        return SearchActivityPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    private fun refreshHistoryView() {
        mHistoryAdapter = SearchResultsAdapter()
        mHistoryRecycler.adapter = mHistoryAdapter
        mHistoryAdapter.setOnXClickListener { item ->
            LocationUtils.launchGoogleMapsWithPin(context, item.latLng)
        }
//        mEmptyView.visibility = if (mHistoryAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun showLocations(page: List<Location?>?, viewport: LatLngBounds?) {
        mEmptyView.visibility = View.GONE
        mFailView.visibility = View.GONE
        mHistoryAdapter.clear()
        mHistoryAdapter.appendBottomAll(page)
    }

    override fun onEmptyResponse() {
        mEmptyView.visibility = View.GONE
        mHistoryAdapter.clear()
        (mFailView.getChildAt(0) as TextView).text = "Cannot find any chargers"// near " + mAddress
        mFailView.visibility = View.VISIBLE
    }

    fun setLocationClickListener(listener: MainMapFragment2.LocationClickListener) {
        mParent = listener
    }

}
