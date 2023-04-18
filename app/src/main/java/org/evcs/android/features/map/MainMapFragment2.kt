package org.evcs.android.features.map

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentMainMap2Binding
import org.evcs.android.model.Location

class MainMapFragment2 : ClusterSelectionMapFragment<MainMapPresenter2, Location>(), IMainMapView2 {

    private var mCarouselItemSpacing = 0
    private var mMapTopPadding = 0
    private var mMapBottomPadding = 0
    private lateinit var mCenterButton: ImageView
    private lateinit var mParent: LocationClickListener

    companion object {
        fun newInstance(): MainMapFragment2 {
            val args = Bundle()
            val fragment = MainMapFragment2()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_main_map_2
    }

    override fun createPresenter(): MainMapPresenter2 {
        return MainMapPresenter2(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        val binding = FragmentMainMap2Binding.bind(v)
        mCenterButton = binding.mapCenter
    }

    override fun init() {
        super.init()
        mCarouselItemSpacing = resources.getDimension(R.dimen.spacing_medium).toInt()
        val carouselHeight = resources.getDimension(R.dimen.map_bottom_padding).toInt()
        val paddingExtra = resources.getDimension(R.dimen.spacing_medium).toInt()
        mMapBottomPadding = carouselHeight + paddingExtra
        mMapTopPadding = resources.getDimension(R.dimen.status_bar_height).toInt()
    }

    override fun setListeners() {
//        mCenterButton.setOnClickListener { if (presenter?.mLastLocation != null) centerMap(presenter?.mLastLocation!!) }
        addOnCameraChangeListener {
//                cameraPosition -> if (cameraPosition.zoom < ZOOM_LIMIT) showCarousel(false)
        }

    }

    fun findAndSelectMarker(location: Location) {
        if (mRenderer.getMarker(location) != null)
            toggleContainerSelection(location)
        //Ex: markers didn't load
        else {
            centerMap(location.latLng)
//            getLocations()
        }
    }

    fun onLocationNotRetrieved() {
        mCenterButton.setImageDrawable(resources.getDrawable(R.drawable.no_location))
    }

    fun onLocationResult(lastLocation: android.location.Location) {
        mCenterButton.setImageDrawable(resources.getDrawable(R.drawable.center))
        drawLocationMarker()
    }

    override fun onContainerClicked(container: Container) {
        mParent.onLocationClicked(container.mapItem)
    }

    fun showInitialLocations(response: List<Location?>) {
        showMapItems(response)
    }

    fun showLocations(locations: List<Location?>, viewport: LatLngBounds?) {
//        if (result.hasExtra(Extras.LocationActivity.LOCATION)) {
//            val location = result.getSerializableExtra(Extras.LocationActivity.LOCATION) as Location
//            findAndSelectMarker(location)
//        } else {
//            findAndSelectMarker(locations[0]!!)
            val viewport2 = viewport?.including(locations[0]!!.latLng)
            if (viewport2 != null)
                applyCameraUpdate(CameraUpdateFactory.newLatLngBounds(viewport2, 0))
//        }
    }

    override fun showMapItems(mapItems: List<Location?>) {
        clearMap()
        super.showMapItems(mapItems)
    }

    override fun onMapReady() {
//        showCarousel(false)
    }

//    fun getDefaultSelectedMapItem(): Int {
//        return DEFAULT_SELECTED_ITEM
//    }

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

    fun setLocationClickListener(listener: LocationClickListener) {
        mParent = listener;
    }

    interface LocationClickListener {
        fun onLocationClicked(location: Location)
    }

}