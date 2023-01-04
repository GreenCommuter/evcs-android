package org.evcs.android.features.map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.base.core.util.ToastUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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
    private lateinit var mCarouselRecycler: LocationItemView
    private lateinit var mSearchButton: ImageButton
    private lateinit var mFilterButton: ImageButton
    private lateinit var mCenterButton: ImageView
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
        } else {
            val locations = result.extras!!.get(Extras.SearchActivity.LOCATIONS) as Array<Location>
            findAndSelectMarker(locations[0])

            var viewport = result.extras!!.get(Extras.SearchActivity.VIEWPORT) as LatLngBounds?
            viewport = viewport!!.including(locations[0].latLng)
            if (viewport != null)
                applyCameraUpdate(CameraUpdateFactory.newLatLngBounds(viewport, 0))
        }
    }

    fun findAndSelectMarker(location: Location) {
        if (mRenderer.getMarker(location) != null)
            toggleContainerSelection(location)
        //Ex: markers didn't load
        else {
            centerMap(location.latLng)
            getLocations()
        }
    }

    private fun showCarousel(show: Boolean) {
        //It starts as gone
        if (show) mCarouselRecycler.visibility = View.VISIBLE
        isCarouselShowing = show
        AnimationUtils.animateTranslation(mCarouselRecycler, if (show) 0f else mMapBottomPadding.toFloat())
        AnimationUtils.animateTranslation(mCenterButton, if (show) 0f else mMapBottomPadding.toFloat())
        setMapPadding(0, mMapTopPadding, 0, if (show) mMapBottomPadding else 0)
    }

    override fun onLocationNotRetrieved() {
        getLocations()
        mCenterButton.setImageDrawable(resources.getDrawable(R.drawable.no_location))
    }

    override fun onLocationResult(lastLocation: android.location.Location) {
        mCenterButton.setImageDrawable(resources.getDrawable(R.drawable.center))
        //Only for distances
        presenter?.mLastLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
        drawLocationMarker()
        getLocations()
    }

    private fun getLocations() {
        mapView!!.getMapAsync{ presenter?.getLocations() }
    }

    override fun onContainerClicked(container: Container) {
        mCarouselRecycler.setLocation(container.mapItem)
        showCarousel(true)
    }

    private fun initializeRecycler() {
        mCarouselRecycler.setOnClickListener {
            val item = mSelectedContainer!!.mapItem
            SearchActivity.saveToLocationHistory(item)
            val intent = Intent(requireContext(), LocationActivity::class.java)
            intent.putExtra(Extras.LocationActivity.LOCATION, item)
            startActivity(intent)
        }
    }

    override fun showLocations(response: List<Location?>) {
        hideLoading()
        showMapItems(response)
    }

    override fun showMapItems(mapItems: List<Location?>) {
        clearMap()
        super.showMapItems(mapItems)
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