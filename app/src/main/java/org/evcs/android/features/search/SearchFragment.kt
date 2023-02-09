package org.evcs.android.features.search

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.core.util.ToastUtils
import com.base.networking.retrofit.serializer.BaseGsonBuilder
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.gson.Gson
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.ActivitySearchBinding
import org.evcs.android.model.FilterState
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.adapter.BaseRecyclerAdapterItemClickListener
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.recycler.EndlessRecyclerView
import org.evcs.android.ui.view.shared.SearchLocationChildFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.LocationUtils
import org.evcs.android.util.StorageUtils

class SearchFragment : ErrorFragment<SearchPresenter>(), SearchView {

    private lateinit var mLoading: ProgressBar
    private lateinit var mSearchLocationChildFragment: SearchLocationChildFragment
    private lateinit var mFailView: LinearLayout
    private lateinit var mEmptyView: TextView
    private lateinit var mAddress: String

    private lateinit var mHistoryRecycler: EndlessRecyclerView
    private lateinit var mHistoryAdapter: SearchResultsAdapter
    private lateinit var mHistoryLayout: View
    private lateinit var mHistoryClear: TextView

    override fun createPresenter(): SearchPresenter {
        val presenter = SearchPresenter(this, EVCSApplication.getInstance().retrofitServices)
        presenter.mFilterState = requireActivity().intent
                .getSerializableExtra(Extras.FilterActivity.FILTER_STATE) as FilterState
        return presenter
    }

    override fun setUi(v: View) {
        val binding = ActivitySearchBinding.bind(v)
        mFailView = binding.activitySearchFail
        mLoading = binding.activitySearchLoading
        mEmptyView = binding.activitySearchEmpty
        mHistoryRecycler = binding.activitySearchHistory
        mHistoryLayout = binding.activitySearchHistoryLayout
        mHistoryClear = binding.activitySearchHistoryClear
    }

    override fun layout(): Int {
        return R.layout.activity_search
    }

    override fun init() {}

    override fun populate() {
        super.populate()
        mSearchLocationChildFragment = SearchLocationChildFragment.newInstance()
//        mSearchLocationChildFragment.setDefault("asdasd")
        requireFragmentManager().beginTransaction()
            .replace(R.id.fragment_search_location_address_layout, mSearchLocationChildFragment)
            .commit()
        mHistoryRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        refreshHistoryView()
    }

    override fun setListeners() {
        super.setListeners()
        mSearchLocationChildFragment.setListener(object : SearchLocationChildFragment.ISearchLocationListener {
            override fun onLocationChosen(address: String, latLng: LatLng, viewport: LatLngBounds?) {
                this@SearchFragment.onLocationChosen(address, latLng, LocationUtils.addDiagonal(viewport))
            }

            override fun onLocationRemoved() {
                clear()
            }

            override fun onCloseClicked() {
                activity?.finish()
            }
        })
        val itemClickListener = object : BaseRecyclerAdapterItemClickListener<Location>() {
            override fun onItemClicked(item: Location?, adapterPosition: Int) {
                saveToLocationHistory(item!!)
                val data = Intent()
                data.putExtra(Extras.LocationActivity.LOCATION, item)
                activity?.setResult(RESULT_OK, data)
                activity?.finish()
            }
        }
        mHistoryAdapter.setItemClickListener(itemClickListener)
        mHistoryClear.setOnClickListener { setLocationHistory(arrayOf()) }
    }

    private fun refreshHistoryView() {
        mHistoryAdapter = SearchResultsAdapter()
        mHistoryRecycler.adapter = mHistoryAdapter
        mHistoryAdapter.appendBottomAll(getLocationHistory())
        mHistoryAdapter.setOnXClickListener { item ->
            val history = getLocationHistory()
            history.removeAll { location -> location.latLng == item.latLng }
            setLocationHistory(history.toTypedArray())
        }
        mEmptyView.visibility = if (mHistoryAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    companion object {
        fun saveToLocationHistory(item: Location) {
            val history = getLocationHistory()
            history.removeAll { location -> location.latLng == item.latLng }
            history.add(0, item)
            StorageUtils.storeInSharedPreferences(Extras.SearchActivity.HISTORY, history.toTypedArray())
        }

        private fun getLocationHistory(): MutableList<Location> {
            val json = StorageUtils.getStringFromSharedPreferences(Extras.SearchActivity.HISTORY, "")
            val gson: Gson = BaseGsonBuilder.getBaseGsonBuilder().create()
            return gson.fromJson(json, Array<Location>::class.java)?.toMutableList() ?: mutableListOf()
        }
    }

    private fun setLocationHistory(history: Array<Location>) {
        StorageUtils.storeInSharedPreferences(Extras.SearchActivity.HISTORY, history)
        refreshHistoryView()
    }

    override fun showLocations(page: List<Location?>?, viewport: LatLngBounds?) {
        val data = Intent()
        data.putExtra(Extras.SearchActivity.LOCATIONS, page!!.toTypedArray())
        data.putExtra(Extras.SearchActivity.VIEWPORT, viewport)
        activity?.setResult(RESULT_OK, data)
        activity?.finish()
    }

    override fun showLoading() {
        mLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        mLoading.visibility = View.GONE
    }

    override fun onEmptyResponse() {
        hideLoading()
        mEmptyView.visibility = View.GONE
        mHistoryLayout.visibility = View.GONE
        (mFailView.getChildAt(0) as TextView).text = "Cannot find any chargers near " + mAddress
        mFailView.visibility = View.VISIBLE
    }

    private fun onLocationChosen(address: String, latLng: LatLng, viewport: LatLngBounds?) {
        showLoading()
        presenter.search(latLng, viewport)
        mHistoryLayout.visibility = View.GONE
        mAddress = address
    }

    //show history
    private fun clear() {
        mEmptyView.visibility = View.VISIBLE
        mFailView.visibility = View.GONE
        mHistoryLayout.visibility = View.VISIBLE
    }

}
