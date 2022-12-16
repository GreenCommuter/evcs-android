package org.evcs.android.features.map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.base.core.util.ToastUtils
import com.google.android.gms.maps.model.LatLng
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.FilterActivity
import org.evcs.android.activity.location.LocationActivity
import org.evcs.android.activity.search.SearchActivity
import org.evcs.android.databinding.FragmentMainMapBinding
import org.evcs.android.model.FilterState
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.adapter.BaseRecyclerAdapterItemClickListener
import org.evcs.android.ui.recycler.decoration.SpaceItemDecoration
import org.evcs.android.util.AnimationUtils
import org.evcs.android.util.Extras
import org.evcs.android.util.FragmentLocationReceiver
import org.evcs.android.util.LocationHelper

class MainMapFragment2 : ClusterSelectionMapFragment<MainMapPresenter, Location>(), IMainMapView,
    FragmentLocationReceiver {

    private var mCarouselItemSpacing = 0
    private var mMapTopPadding = 0
    private var mMapBottomPadding = 0
    private var isCarouselShowing: Boolean = false
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mMapAdapter: CarouselAdapter
    private lateinit var mVanpoolCarouselScrollListener: RecyclerView.OnScrollListener
    private lateinit var mCarouselRecycler: RecyclerView
    private lateinit var mSearchButton: ImageButton
    private lateinit var mFilterButton: ImageButton
    private lateinit var mCenterButton: Button
    private lateinit var mLoading: ProgressBar

    fun newInstance(): MainMapFragment2 {
        val args = Bundle()
        val fragment = MainMapFragment2()
        fragment.arguments = args
        return fragment
    }

    override fun layout(): Int {
        return R.layout.fragment_main_map
    }

    override fun createPresenter(): MainMapPresenter {
        return MainMapPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        val binding = FragmentMainMapBinding.bind(v)
        mCarouselRecycler = binding.mapCarouselRecycler
        mSearchButton = binding.mapSearch
        mFilterButton = binding.mapFilter
        mCenterButton = binding.mapCenter
        mLoading = binding.mapLoading
    }

    override fun init() {
        super.init()
        LocationHelper().init(this)
        mCarouselItemSpacing = resources.getDimension(R.dimen.spacing_medium).toInt()
        val carouselHeight = resources.getDimension(R.dimen.map_bottom_padding).toInt()
        val paddingExtra = resources.getDimension(R.dimen.spacing_medium).toInt()
        mMapBottomPadding = carouselHeight + paddingExtra
        mMapTopPadding = resources.getDimension(R.dimen.status_bar_height).toInt()
        initializeRecycler()
    }

    override fun setListeners() {
        mVanpoolCarouselScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val selectedItemPosition =
                        mLinearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (selectedItemPosition == RecyclerView.NO_POSITION) {
                        return
                    }
                    val selectedLocation = mMapAdapter[selectedItemPosition]
                    toggleContainerSelection(selectedLocation!!)
                }
            }
        }
        mCarouselRecycler.addOnScrollListener(mVanpoolCarouselScrollListener)
        val searchActivityTask =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK)
                    onSearchResult(result.data!!)
            }
        val filterActivityTask =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                onFilterResult(result)
            }
        mSearchButton.setOnClickListener { searchActivityTask.launch(getIntentWithFilterState(SearchActivity::class.java)) }
        mFilterButton.setOnClickListener { filterActivityTask.launch(getIntentWithFilterState(FilterActivity::class.java)) }
        mCenterButton.setOnClickListener { if (presenter?.mLastLocation != null) centerMap(presenter?.mLastLocation!!) }
        addOnCameraChangeListener {
                cameraPosition -> if (cameraPosition.zoom < ZOOM_LIMIT) showCarousel(false)
        }

    }

    private fun onFilterResult(result: ActivityResult) {
        if (result.data == null) return
        showLoading()
        val filterState = result.data!!.getSerializableExtra(Extras.FilterActivity.FILTER_STATE) as FilterState
        mFilterButton.isSelected = !filterState.isEmpty()
        presenter.onFilterResult(filterState)
    }

    private fun getIntentWithFilterState(activity : Class <*>) : Intent {
        val intent = Intent(requireContext(), activity)
        intent.putExtra(Extras.FilterActivity.FILTER_STATE, presenter.mFilterState)
        return intent
    }

    private fun onSearchResult(result: Intent) {
        if (result.hasExtra(Extras.LocationActivity.LOCATION)) {
            val location = result.getSerializableExtra(Extras.LocationActivity.LOCATION) as Location
            findAndSelectMarker(location)
        } else if (result.hasExtra(Extras.SearchActivity.LATLNG)) {
            val latlng = result.getParcelableExtra<LatLng>(Extras.SearchActivity.LATLNG)
            presenter.getLocations(latlng)
            mapView!!.getMapAsync { map -> zoomTo(map, latlng!!, ZOOM_LIMIT * 2.0f) }
        } else {
            val locations = result.extras!!.get(Extras.SearchActivity.LOCATIONS) as Array<Location>
            val newDataset = arrayListOf<Location>()
            newDataset.addAll(locations)
            for (i in 0..mMapAdapter.itemCount - 1) {
                val elem = mMapAdapter.get(i)!!
                if (!newDataset.any { other -> other.id == elem.id }) {
                    newDataset.add(elem)
                }
            }

            mMapAdapter.clear()
            mMapAdapter.appendBottomAll(presenter.populateDistances(newDataset))
            findAndSelectMarker(mMapAdapter[0]!!)
        }
    }

    fun findAndSelectMarker(location: Location) {
        val marker = mMapAdapter.find(location)
        if (marker != null)
            toggleContainerSelection(marker)
        //Ex: markers didn't load
        else {
            centerMap(location.latLng)
            getLocations()
        }
    }

    private fun showCarousel(show: Boolean) {
        isCarouselShowing = show
        AnimationUtils.animateTranslation(mCarouselRecycler, if (show) 0f else mMapBottomPadding.toFloat())
        AnimationUtils.animateTranslation(mCenterButton, if (show) 0f else mMapBottomPadding.toFloat())
        setMapPadding(0, mMapTopPadding, 0, if (show) mMapBottomPadding else 0)
    }

    override fun onLocationNotRetrieved() {
        getLocations()
        mCenterButton.text = "No location"
    }

    override fun onLocationResult(lastLocation: android.location.Location) {
        mCenterButton.text = "Center"
        presenter?.mLastLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
        drawLocationMarker()
        getLocations()
    }

    private fun getLocations() {
        mapView!!.getMapAsync{ presenter?.getLocations() }
    }

    override fun onContainerClicked(container: Container) {
        //This line in particular is not working because the listener is set back before the scrolling ends
        mCarouselRecycler.removeOnScrollListener(mVanpoolCarouselScrollListener)
        val scrollTo = mMapAdapter.getItemPosition(container.mapItem)
        if (isCarouselShowing)
            mCarouselRecycler.smoothScrollToPosition(scrollTo)
        else
            mCarouselRecycler.scrollToPosition(scrollTo)
        mCarouselRecycler.addOnScrollListener(mVanpoolCarouselScrollListener)
//        centerFromContainer(container)
        showCarousel(true)
    }

    private fun initializeRecycler() {
        mMapAdapter = CarouselAdapter()
        mCarouselRecycler.adapter = mMapAdapter
        mLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mCarouselRecycler.layoutManager = mLinearLayoutManager
        mCarouselRecycler.addItemDecoration(
            SpaceItemDecoration(mCarouselItemSpacing, SpaceItemDecoration.HORIZONTAL)
        )
        val onFlingListener = LinearSnapHelper()
        onFlingListener.attachToRecyclerView(mCarouselRecycler)
        mMapAdapter.setItemClickListener(object : BaseRecyclerAdapterItemClickListener<Location>() {
            override fun onItemClicked(item: Location, adapterPosition: Int) {
                SearchActivity.saveToLocationHistory(item)
                val intent = Intent(requireContext(), LocationActivity::class.java)
                intent.putExtra(Extras.LocationActivity.LOCATION, item)
                startActivity(intent)
            }
        })
    }

    override fun showLocations(response: List<Location?>) {
        hideLoading()
        showMapItems(response)
    }

    override fun showMapItems(mapItems: List<Location?>) {
        clearMap()
        mMapAdapter.clear()
        super.showMapItems(mapItems)

        mMapAdapter.appendBottomAll(mapItems)
    }

    override fun onMapReady() {
        showCarousel(false)
    }

//    fun getDefaultSelectedMapItem(): Int {
//        return DEFAULT_SELECTED_ITEM
//    }

    override fun showError(requestError: RequestError) {
        hideLoading()
        ToastUtils.show(requestError.body)
    }

    override fun showLoading() {
        mLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        mLoading.visibility = View.GONE
    }

    override fun getMapViewId(): Int {
        return R.id.map_view
    }

    override fun getDefaultZoom(): Float {
        return BaseConfiguration.Map.DEFAULT_ZOOM
    }

    override fun getDefaultLatlng(): LatLng {
        return LatLng(
            BaseConfiguration.Map.DEFAULT_LATITUDE,
            BaseConfiguration.Map.DEFAULT_LONGITUDE
        )
    }

}