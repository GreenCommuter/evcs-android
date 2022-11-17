package org.evcs.android.activity.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivitySearchBinding
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.adapter.BaseRecyclerAdapterItemClickListener
import org.evcs.android.ui.recycler.EndlessRecyclerView
import org.evcs.android.ui.view.shared.SearchLocationChildFragment
import org.evcs.android.util.Extras

class SearchActivity : BaseActivity2(), SearchActivityView {

    private lateinit var mAdapter: SearchResultsAdapter
    private lateinit var mPresenter: SearchActivityPresenter
    private lateinit var mSearchLocationChildFragment: SearchLocationChildFragment
    private lateinit var mRecycler: EndlessRecyclerView

    override fun inflate(layoutInflater: LayoutInflater?): View {
        val binding = ActivitySearchBinding.inflate(layoutInflater!!)
        mRecycler = binding.activitySearchResults
        return binding.root
    }

    override fun init() {
    }

    override fun onStart() {
        mPresenter = SearchActivityPresenter(this, EVCSApplication.getInstance().retrofitServices)
        mPresenter.onViewCreated()
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

            override fun onLocationRemoved() {}

            override fun onCloseClicked() {
                this@SearchActivity.finish()
            }
        })
        mAdapter.setItemClickListener(object : BaseRecyclerAdapterItemClickListener<Location>() {
            override fun onItemClicked(item: Location?, adapterPosition: Int) {
                val data = Intent()
                data.putExtra(Extras.LocationActivity.LOCATION, item)
                setResult(RESULT_OK, data)
                finish()
            }
        })
        //TODO: Handle
    }

    private fun onLocationChosen(address: String, latLng: LatLng) {
        mRecycler.setUp(true, {
            mPresenter.search(latLng)
        }, 10)
    }

    override fun showLocations(page: List<Location?>?, pagesLeft: Boolean, onFirstPage: Boolean) {
//        if (mEmptyTextView != null) {
//            mEmptyTextView.setVisibility(View.GONE)
//        }

        if (onFirstPage) {
            mAdapter.clear()
        }

        mAdapter.appendBottomAll(page)
        if (mRecycler != null) {
            mRecycler.toggleEndlessScrolling(pagesLeft)
            mRecycler.notifyLoadingFinished()
        }
    }

    override fun showError(requestError: RequestError) {

    }

}
