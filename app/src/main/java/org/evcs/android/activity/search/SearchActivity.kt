package org.evcs.android.activity.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.core.util.ToastUtils
import com.base.networking.retrofit.serializer.BaseGsonBuilder
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivitySearchBinding
import org.evcs.android.model.FilterState
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.service.EVCSRetrofitServices
import org.evcs.android.ui.adapter.BaseRecyclerAdapterItemClickListener
import org.evcs.android.ui.recycler.EndlessRecyclerView
import org.evcs.android.ui.view.shared.SearchLocationChildFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.StorageUtils

class SearchActivity : BaseActivity2(), SearchActivityView {

    private lateinit var mLoading: ProgressBar
    private lateinit var mPresenter: SearchActivityPresenter
    private lateinit var mSearchLocationChildFragment: SearchLocationChildFragment
    private lateinit var mFailView: LinearLayout
    private lateinit var mEmptyView: TextView
    private lateinit var mAddress: String

    private lateinit var mHistoryRecycler: EndlessRecyclerView
    private lateinit var mHistoryAdapter: SearchResultsAdapter
    private lateinit var mHistoryLayout: View
    private lateinit var mHistoryClear: TextView

    override fun inflate(layoutInflater: LayoutInflater?): View {
        val binding = ActivitySearchBinding.inflate(layoutInflater!!)
        mFailView = binding.activitySearchFail
        mLoading = binding.activitySearchLoading
        mEmptyView = binding.activitySearchEmpty
        mHistoryRecycler = binding.activitySearchHistory
        mHistoryLayout = binding.activitySearchHistoryLayout
        mHistoryClear = binding.activitySearchHistoryClear
        return binding.root
    }

    override fun init() {
        mPresenter = SearchActivityPresenter(this, EVCSApplication.getInstance().retrofitServices)
        mPresenter.onViewCreated()
        mPresenter.mFilterState = intent.getSerializableExtra(Extras.FilterActivity.FILTER_STATE) as FilterState
        super.onStart()
    }

    override fun populate() {
        super.populate()
        mSearchLocationChildFragment = SearchLocationChildFragment.newInstance()
//        mSearchLocationChildFragment.setDefault("asdasd")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_search_location_address_layout, mSearchLocationChildFragment)
            .commit()
        mHistoryRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        refreshHistoryView()
    }

    override fun setListeners() {
        super.setListeners()
        mSearchLocationChildFragment.setListener(object : SearchLocationChildFragment.ISearchLocationListener {
            override fun onLocationChosen(address: String, latLng: LatLng) {
                this@SearchActivity.onLocationChosen(address, latLng)
            }

            override fun onLocationRemoved() {
                clear()
            }

            override fun onCloseClicked() {
                this@SearchActivity.finish()
            }
        })
        val itemClickListener = object : BaseRecyclerAdapterItemClickListener<Location>() {
            override fun onItemClicked(item: Location?, adapterPosition: Int) {
                saveToLocationHistory(item!!)
                val data = Intent()
                data.putExtra(Extras.LocationActivity.LOCATION, item)
                setResult(RESULT_OK, data)
                finish()
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

    override fun showLocations(page: List<Location?>?) {
        val data = Intent()
        data.putExtra(Extras.SearchActivity.LOCATIONS, page!!.toTypedArray())
        setResult(RESULT_OK, data)
        finish()
    }

    override fun showLoading() {
        mLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        mLoading.visibility = View.GONE
    }

    override fun showError(requestError: RequestError) {
        ToastUtils.show(requestError.body)
    }

    override fun onEmptyResponse() {
        hideLoading()
        mEmptyView.visibility = View.GONE
        mHistoryLayout.visibility = View.GONE
        (mFailView.getChildAt(0) as TextView).text = "Cannot find \"" + mAddress + "\""
        mFailView.visibility = View.VISIBLE
    }

    private fun onLocationChosen(address: String, latLng: LatLng) {
        showLoading()
        mPresenter.search(latLng)
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
