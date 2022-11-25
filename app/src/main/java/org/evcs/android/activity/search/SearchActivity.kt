package org.evcs.android.activity.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.networking.retrofit.serializer.BaseGsonBuilder
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivitySearchBinding
import org.evcs.android.model.Location
import org.evcs.android.ui.adapter.BaseRecyclerAdapterItemClickListener
import org.evcs.android.ui.recycler.EndlessRecyclerView
import org.evcs.android.ui.view.shared.SearchLocationChildFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.StorageUtils

class SearchActivity : BaseActivity2() {

    private lateinit var mSearchLocationChildFragment: SearchLocationChildFragment
    private lateinit var mEmptyView: TextView

    private lateinit var mHistoryRecycler: EndlessRecyclerView
    private lateinit var mHistoryAdapter: SearchResultsAdapter
    private lateinit var mHistoryLayout: View
    private lateinit var mHistoryClear: TextView

    override fun inflate(layoutInflater: LayoutInflater?): View {
        val binding = ActivitySearchBinding.inflate(layoutInflater!!)
        mEmptyView = binding.activitySearchEmpty
        mHistoryRecycler = binding.activitySearchHistory
        mHistoryLayout = binding.activitySearchHistoryLayout
        mHistoryClear = binding.activitySearchHistoryClear
        return binding.root
    }

    override fun init() {
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
        mHistoryClear.setOnClickListener {
            StorageUtils.storeInSharedPreferences(Extras.SearchActivity.HISTORY, arrayOf<Location>())
            refreshHistoryView()
        }
    }

    private fun refreshHistoryView() {
        mHistoryAdapter = SearchResultsAdapter()
        mHistoryRecycler.adapter = mHistoryAdapter
        mHistoryAdapter.appendBottomAll(getLocationHistory())
        mEmptyView.visibility = if (mHistoryAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    companion object {
        fun saveToLocationHistory(item: Location) {
            val history = getLocationHistory()
            history.removeAll { location -> location.latLng == item.latLng }
            history.add(0, item)
            StorageUtils.storeInSharedPreferences(
                Extras.SearchActivity.HISTORY,
                history.toTypedArray()
            )
        }
        private fun getLocationHistory(): MutableList<Location> {
            val json = StorageUtils.getStringFromSharedPreferences(Extras.SearchActivity.HISTORY, "")
            val gson: Gson = BaseGsonBuilder.getBaseGsonBuilder().create()
            return gson.fromJson(json, Array<Location>::class.java)?.toMutableList() ?: mutableListOf()
        }
    }

    private fun onLocationChosen(address: String, latLng: LatLng) {
        val data = Intent()
        data.putExtra(Extras.SearchActivity.LATLNG, latLng)
        setResult(RESULT_OK, data)
        finish()
    }

    //show history
    private fun clear() {
        mEmptyView.visibility = View.VISIBLE
        mHistoryLayout.visibility = View.VISIBLE
    }

}
