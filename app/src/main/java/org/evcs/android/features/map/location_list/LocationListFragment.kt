package org.evcs.android.features.map.location_list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLngBounds
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentLocationListBinding
import org.evcs.android.features.map.clustermap.InnerMapFragment
import org.evcs.android.model.Location
import org.evcs.android.ui.adapter.BaseRecyclerAdapterItemClickListener
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.recycler.EndlessRecyclerView
import org.evcs.android.util.LocationUtils

class LocationListFragment : ErrorFragment<LocationListPresenter>(), LocationListView {

    private lateinit var mFailView: View
    private lateinit var mEmptyView: View

    private lateinit var mHistoryRecycler: EndlessRecyclerView
    private lateinit var mHistoryAdapter: LocationListAdapter
    private lateinit var mParent: InnerMapFragment.LocationClickListener

    companion object {
        fun newInstance(): LocationListFragment {
            val args = Bundle()
            val fragment = LocationListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_location_list
    }

    override fun setUi(v: View?) {
        super.setUi(v)
        val binding = FragmentLocationListBinding.bind(v!!)
        mFailView = binding.fragmentLocationListFail
        mEmptyView = binding.fragmentLocationListEmpty
        mHistoryRecycler = binding.fragmentLocationListHistory
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
                mParent.onLocationClicked(item!!, false)
            }
        }
        mHistoryAdapter.setItemClickListener(itemClickListener)
    }

    override fun createPresenter(): LocationListPresenter {
        return LocationListPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    private fun refreshHistoryView() {
        mHistoryAdapter = LocationListAdapter()
        mHistoryRecycler.adapter = mHistoryAdapter
        mHistoryAdapter.setOnXClickListener { item ->
            LocationUtils.launchGoogleMapsWithPin(context, item.latLng, item.gatecode, childFragmentManager)
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
        mFailView.visibility = View.VISIBLE
    }

    fun setLocationClickListener(listener: InnerMapFragment.LocationClickListener) {
        mParent = listener
    }

}
