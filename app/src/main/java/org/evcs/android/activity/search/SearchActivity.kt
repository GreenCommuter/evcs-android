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
import org.evcs.android.ui.adapter.BaseRecyclerAdapterItemClickListener
import org.evcs.android.ui.recycler.EndlessRecyclerView
import org.evcs.android.ui.view.shared.SearchLocationChildFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.StorageUtils

class SearchActivity : BaseActivity2(), SearchActivityView {

    private lateinit var mLoading: ProgressBar
    private lateinit var mAdapter: SearchResultsAdapter
    private lateinit var mPresenter: SearchActivityPresenter
    private lateinit var mSearchLocationChildFragment: SearchLocationChildFragment
    private lateinit var mRecycler: EndlessRecyclerView
    private lateinit var mFailView: LinearLayout
    private lateinit var mEmptyView: TextView

    private lateinit var mAddress: String

    override fun inflate(layoutInflater: LayoutInflater?): View {
        val binding = ActivitySearchBinding.inflate(layoutInflater!!)
        mRecycler = binding.activitySearchResults
        mFailView = binding.activitySearchFail
        mEmptyView = binding.activitySearchEmpty
        mLoading = binding.activitySearchLoading
        return binding.root
    }

    override fun init() {
    }

    override fun onStart() {
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
        mAdapter = SearchResultsAdapter()
        mRecycler.adapter = mAdapter
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecycler.layoutManager = linearLayoutManager
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
        mAdapter.setItemClickListener(object : BaseRecyclerAdapterItemClickListener<Location>() {
            override fun onItemClicked(item: Location?, adapterPosition: Int) {
                saveToLocationHistory(item!!)
                val data = Intent()
                data.putExtra(Extras.LocationActivity.LOCATION, item)
                setResult(RESULT_OK, data)
                finish()
            }
        })
    }

    private fun saveToLocationHistory(item: Location) {
        val json = StorageUtils.getStringFromSharedPreferences(Extras.SearchActivity.HISTORY, "")
        val gson: Gson = BaseGsonBuilder.getBaseGsonBuilder().create()
        val history = gson.fromJson(json, Array<Location>::class.java)?.toMutableList() ?: mutableListOf()
        history.add(item)
        StorageUtils.storeInSharedPreferences(Extras.SearchActivity.HISTORY, history.toTypedArray())
    }

    private fun onLocationChosen(address: String, latLng: LatLng) {
        showLoading()
        mAdapter.clear()
        mRecycler.setUp(true, {
            mPresenter.search(latLng)
        }, 10)
        mAddress = address
    }

    override fun showLocations(page: List<Location?>?, pagesLeft: Boolean, onFirstPage: Boolean) {
        hideLoading()
        mEmptyView.visibility = View.GONE
        mFailView.visibility = View.GONE

        if (onFirstPage) {
            mAdapter.clear()
        }

        mAdapter.appendBottomAll(page)
        mRecycler.toggleEndlessScrolling(pagesLeft)
        mRecycler.notifyLoadingFinished()
    }

    override fun onEmptyResponse() {
        hideLoading()
        mEmptyView.visibility = View.GONE
        (mFailView.getChildAt(0) as TextView).text = "Cannot find \"" + mAddress + "\""
        mFailView.visibility = View.VISIBLE
    }

    //This will be "show history"
    private fun clear() {
        mAdapter.clear()
        mEmptyView.visibility = View.VISIBLE
        mFailView.visibility = View.GONE
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

}
