package org.evcs.android.features.map.clustermap

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentInnerMapBinding
import org.evcs.android.model.Location
import org.evcs.android.util.LocationUtils
import org.evcs.android.util.ViewUtils.setParentVisibility

class InnerMapFragment : ClusterSelectionMapFragment<InnerMapPresenter, Location>(), InnerMapView {

    private var mCarouselItemSpacing = 0
    private var mMapTopPadding = 0
    private var mMapBottomPadding = 0
    private lateinit var mCenterButton: ImageView
    private lateinit var mParent: LocationClickListener

    companion object {
        fun newInstance(): InnerMapFragment {
            val args = Bundle()
            val fragment = InnerMapFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_inner_map
    }

    override fun createPresenter(): InnerMapPresenter {
        return InnerMapPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        val binding = FragmentInnerMapBinding.bind(v)
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
        (mCenterButton.parent as View).setOnClickListener {
            if (presenter?.mLastLocation != null) centerMap(presenter?.mLastLocation!!)
        }
        addOnCameraChangeListener {
//                cameraPosition -> if (cameraPosition.zoom < ZOOM_LIMIT) showCarousel(false)
        }

    }

    fun findAndSelectMarker(location: Location) {
        if (mRenderer.getMarker(location) != null)
            toggleContainerSelection(location)
        //Ex: markers didn't load
        else {
            addSingleItem(location)
            applyCameraUpdate(CameraUpdateFactory.newLatLngZoom(location.latLng, 18f))
            mParent.onLocationClicked(location, true)
        //            getLocations()
        }
    }

    fun onLocationNotRetrieved() {
//        mCenterButton.setImageDrawable(resources.getDrawable(R.drawable.no_location))
    }

    fun onLocationResult(lastLocation: LatLng) {
        presenter.mLastLocation = lastLocation
        mCenterButton.setParentVisibility(true)
        mCenterButton.setImageDrawable(resources.getDrawable(R.drawable.ic_map_center))
        drawLocationMarker()
    }

    override fun onContainerClicked(container: Container) {
        mParent.onLocationClicked(container.mapItem, true)
    }

    fun showInitialLocations(response: List<Location>) {
        showMapItems(response)
    }

    fun zoomToClosest(locations: List<Location>, nullableViewport: LatLngBounds?) {
        val closest = locations[0].latLng
        val viewport = nullableViewport ?: LocationUtils.addDiagonal(LatLngBounds(closest, closest))
        if (viewport.contains(closest)) {
            applyCameraUpdate2(CameraUpdateFactory.newLatLngBounds(viewport, 20))
        } else {
            //I want to try to make sure that at least something is shown, and if there is a single point it may get clustered
            applyCameraUpdate2(CameraUpdateFactory.newLatLngBounds(viewport.including(closest), 200))
        }
    }

    fun zoomToLocations(locations: List<Location>) {
        val builder = LatLngBounds.builder()
        locations.forEach { location -> builder.include(location.latLng) }
        applyCameraUpdate2(CameraUpdateFactory
                .newLatLngBounds(LocationUtils.addDiagonal(builder.build()), 100))
    }

    private fun applyCameraUpdate2(newLatLngBounds: CameraUpdate) {
        mapView?.getMapAsync {
            map -> map.setOnMapLoadedCallback {
                applyCameraUpdate(newLatLngBounds)
            }
        }
    }


    override fun showMapItems(mapItems: List<Location?>) {
//        clearMap()
        super.showMapItems(mapItems)
    }

    override fun onMapReady() {
        mRenderer.setSumItems(true)
//        mRenderer.setIcons(R.drawable.ic_map_pin,
//                R.drawable.ic_map_pin_selected,
//                R.drawable.ic_map_pin_coming_soon,
//                R.drawable.ic_map_pin_coming_soon_selected)
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
        fun onLocationClicked(location: Location, showAsSlider: Boolean)
    }

}