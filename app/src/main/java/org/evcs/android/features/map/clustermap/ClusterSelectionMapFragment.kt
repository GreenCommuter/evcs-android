package org.evcs.android.features.map.clustermap

import android.content.Context
import android.os.Bundle
import android.view.View
import com.base.maps.IMapPresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import org.evcs.android.network.service.presenter.ServicesPresenter


abstract class ClusterSelectionMapFragment<K, T : ClusterItem> : AbstractMapFragment<K>()
    where K : ServicesPresenter<*>, K : IMapPresenter {

    val ZOOM_LIMIT: Int = 8
    protected lateinit var mClusterManager: ClusterManager<T>

    protected lateinit var mRenderer: ClusterRenderer<T>
    protected var mSelectedContainer: Container? = null
    private var mOnCameraChangeListener: GoogleMap.OnCameraChangeListener? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView?.getMapAsync { map ->
            mClusterManager = ClusterManager<T>(requireContext(), map)
            mClusterManager.setAlgorithm(NonHierarchicalDistanceBasedAlgorithm())
            mRenderer = createClusterRenderer(requireContext(), map, mClusterManager)
            mClusterManager.setRenderer(mRenderer)
            setListeners(map)
        }
    }

    protected open fun createClusterRenderer(context: Context, map: GoogleMap, clusterManager: ClusterManager<T>): ClusterRenderer<T> {
        return ClusterRenderer(requireContext(), map, this.mClusterManager)
    }

    private fun setListeners(map : GoogleMap) {
        map.setOnCameraChangeListener { cameraPosition ->
            mOnCameraChangeListener?.onCameraChange(cameraPosition)
            mClusterManager.onCameraChange(cameraPosition)
        }
        map.setOnMarkerClickListener(mClusterManager)
        mClusterManager.setOnClusterItemClickListener { selectedLocation ->
//            if (map.cameraPosition.zoom >= ZOOM_LIMIT) {
            toggleContainerSelection2(selectedLocation)
            centerMap(selectedLocation.position)
//            } else {
//                zoomTo(map, selectedLocation.position)
//            }
            true
        }
        mClusterManager.setOnClusterClickListener { cluster ->
            zoomTo(map, cluster.position, map.cameraPosition.zoom + 2.5f)
            true
        }
    }

    fun zoomTo(map: GoogleMap, position: LatLng, zoom: Float = ZOOM_LIMIT.toFloat()) {
        val cameraPosition = CameraPosition.Builder()
            .target(position).zoom(zoom).build()

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun addOnCameraChangeListener(listener: GoogleMap.OnCameraChangeListener?) {
        mOnCameraChangeListener = listener
    }

    open fun showMapItems(mapItems: List<T?>) {
        //Mainly to make sure the clustermanager is init
        mapView?.getMapAsync {
            mClusterManager.clearItems()
            mClusterManager.addItems(mapItems)
            mClusterManager.cluster()
        }
    }

    open fun addSingleItem(location: T) {
        mapView?.getMapAsync {
            mClusterManager.addItem(location)
            mClusterManager.cluster()
        }
    }

    fun toggleContainerSelection(selectedLocation: T) {
        if (mRenderer.getMarker(selectedLocation) != null) {
            toggleContainerSelection2(selectedLocation)
        } else {
            //Should only happen when clusters are in place
            //Maybe instead of doing this we should go to the renderer and set the selected item,
            //so that it's rendered as selected. But that means keeping it clustered now.
            unselectCurrent()
//            mapView!!.getMapAsync { _ -> mClusterManager.cluster() }
        }
        centerMap(selectedLocation.position)
    }

    private fun unselectCurrent() {
        if (mSelectedContainer != null) {
            val marker = mRenderer.getMarker(mSelectedContainer!!.mapItem)
            if (marker != null)
                mRenderer.onClusterItemChange(mSelectedContainer!!.mapItem, marker, false)
        }
        mSelectedContainer = null
    }

    private fun toggleContainerSelection2(selectedLocation: T) {
        if (selectedLocation == mSelectedContainer?.mapItem) {
            unselectCurrent() //unselect changes mSelectedContainer
            return
        }

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

    protected inner class Container(mapItem: T, marker: Marker) : ClusterItem {
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