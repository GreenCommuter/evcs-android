package org.evcs.android.features.map

import ClusterRender
import android.os.Bundle
import android.view.View
import com.base.maps.IMapPresenter
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import org.evcs.android.network.service.presenter.ServicesPresenter

abstract class ClusterSelectionMapFragment<K, T : ClusterItem> : AbstractMapFragment<K>()
    where K : ServicesPresenter<*>, K : IMapPresenter {

    val ZOOM_LIMIT: Int = 8

    protected lateinit var mClusterManager: ClusterManager<T>
    protected lateinit var mRenderer: ClusterRender<T>
    protected var mSelectedContainer: Container? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView?.getMapAsync { map ->
            mClusterManager = ClusterManager<T>(requireContext(), map)
            mClusterManager.setAlgorithm(ZoomLimitedGridBasedAlgorithm(ZOOM_LIMIT))
            map.setOnCameraChangeListener(mClusterManager)
            map.setOnMarkerClickListener(mClusterManager)
            mRenderer = ClusterRender(requireContext(), map, mClusterManager)
            mClusterManager.setRenderer(mRenderer)

            mClusterManager.setOnClusterItemClickListener { selectedLocation ->

                toggleContainerSelection2(selectedLocation)

                true
            }

        }
    }

    open fun showMapItems(mapItems: List<T?>) {
        mClusterManager.clearItems()
        mClusterManager.addItems(mapItems)
        mClusterManager.cluster()
    }

    fun toggleContainerSelection(selectedLocation: T) {
        if (mRenderer.getMarker(selectedLocation) != null) {
            toggleContainerSelection2(selectedLocation)
        } else {
            //Should only happen when clusters are in place
            unselectCurrent()
//            mapView!!.getMapAsync { _ -> mClusterManager.cluster() }
        }
        centerMap(selectedLocation.position)
    }

    private fun unselectCurrent() {
        if (mSelectedContainer != null) {
            val marker = mRenderer.getMarker(mSelectedContainer!!.mapItem)
            if (marker != null)
                mRenderer.onClusterItemChange(mSelectedContainer!!.mapItem!!, marker, false)
        }
        mSelectedContainer = null
    }

    private fun toggleContainerSelection2(selectedLocation: T) {
        unselectCurrent()
        val marker = mRenderer.getMarker(selectedLocation)
        mSelectedContainer = Container(selectedLocation, marker)
        mRenderer.onClusterItemChange(selectedLocation, marker, true)

        if (mSelectedContainer != null)
            onContainerClicked(mSelectedContainer!!)
    }

    /**
     * Each extending class should handle what happens when a {@link Container} is clicked.
     * Whenever a marker is clicked, this is called with its container
     * @param container the container of the marker clicked.
     */
    protected abstract fun onContainerClicked(container: Container)

    protected inner class Container(mapItem: T, marker: Marker) :
        ClusterItem {
        val mapItem: T
        val marker: Marker

        override fun getPosition(): LatLng {
            return marker.position
        }

        init {
            this.mapItem = mapItem
            this.marker = marker
        }
    }

}