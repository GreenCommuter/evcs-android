package org.evcs.android.features.map

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.base.core.util.ToastUtils
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.FilterActivity
import org.evcs.android.activity.LocationActivity
import org.evcs.android.activity.SearchActivity
import org.evcs.android.databinding.FragmentMainMapBinding
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.adapter.BaseRecyclerAdapterItemClickListener
import org.evcs.android.ui.recycler.decoration.SpaceItemDecoration
import org.evcs.android.util.FragmentLocationReceiver
import org.evcs.android.util.LocationHelper

class MainMapFragment2 : SelectionMapFragment<MainMapPresenter?, Location?>(), IMainMapView,
    FragmentLocationReceiver {

    val DEFAULT_SELECTED_ITEM = 0
    var mCarouselItemSpacing = 0
    var mCarouselHeight = 0
    var mPaddingExtra = 0
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mMapAdapter: CarouselAdapter
    private lateinit var mVanpoolCarouselScrollListener: RecyclerView.OnScrollListener
    private lateinit var mCarouselRecycler: RecyclerView

    private lateinit var mSearchButton: ImageButton
    private lateinit var mFilterButton: ImageButton
    private lateinit var mCenterButton: Button
    private var mLocationMarker: Marker? = null

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
    }

    override fun init() {
        super.init()
        mCarouselItemSpacing = resources.getDimension(R.dimen.spacing_medium).toInt()
        mCarouselHeight = resources.getDimension(R.dimen.map_bottom_padding).toInt()
        mPaddingExtra = resources.getDimension(R.dimen.spacing_medium).toInt()
        initializeRecycler()
        LocationHelper().init(this)
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
                    val selectedContainer = mVContainerMap[selectedLocation.hashCode()]
                    if (selectedContainer != null) onContainerClicked(selectedContainer)
                }
            }
        }
        mCarouselRecycler.addOnScrollListener(mVanpoolCarouselScrollListener)
        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                presenter?.onFilterResult(result)
            }
        mSearchButton.setOnClickListener { startActivity(Intent(requireContext(), SearchActivity::class.java)) }
        mFilterButton.setOnClickListener { startForResult.launch(Intent(requireContext(), FilterActivity::class.java)) }
        mCenterButton.setOnClickListener { if (presenter?.mLastLocation != null) centerMap(presenter?.mLastLocation!!) }

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

    private fun drawLocationMarker() {
        if (mLocationMarker == null)
            mLocationMarker = drawMarker(presenter?.mLastLocation!!, true)
        else
            mLocationMarker!!.position = presenter?.mLastLocation
    }

    private fun getLocations() {
        mapView?.getMapAsync{ presenter?.getLocations() }
    }

    override fun onContainerClicked(container: Container) {
        val markersToggled = toggleContainerSelection(container)
        if (!markersToggled) {
            return
        }
        mCarouselRecycler.removeOnScrollListener(mVanpoolCarouselScrollListener)
        mCarouselRecycler.smoothScrollToPosition(
            mMapAdapter.getItemPosition(
                selectedContainer.mapItem
            )
        )
        mCarouselRecycler.addOnScrollListener(mVanpoolCarouselScrollListener)
        centerFromContainer(container)
    }

    private fun initializeRecycler() {
        mMapAdapter = CarouselAdapter()
        mCarouselRecycler.adapter = mMapAdapter
        mLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mCarouselRecycler.layoutManager = mLinearLayoutManager
        mCarouselRecycler.addItemDecoration(
            SpaceItemDecoration(
                mCarouselItemSpacing,
                SpaceItemDecoration.HORIZONTAL
            )
        )
        val onFlingListener = LinearSnapHelper()
        onFlingListener.attachToRecyclerView(mCarouselRecycler)
    }

    override fun showLocations(response: List<Location?>?) {
        showMapItems(response!!)
    }

    override fun showMapItems(mapItems: List<Location?>) {
        clearMap()
        mMapAdapter.clear()
        if (presenter?.mLastLocation != null) drawLocationMarker()
        super.showMapItems(mapItems)
        mMapAdapter.appendBottomAll(mapItems)
        mMapAdapter.setItemClickListener(object : BaseRecyclerAdapterItemClickListener<Location>() {
            override fun onItemClicked(item: Location, adapterPosition: Int) {
                val intent = Intent(requireContext(), LocationActivity::class.java)
                intent.putExtra("Location", item)
                startActivity(intent)
            }
        })
    }

    override fun clearMap() {
        super.clearMap()
        mLocationMarker = null
    }

    override fun getContainer(location: Location?, selected: Boolean): Container {
        return Container(location, drawMarker(location?.latLng!!, selected))
    }

    override fun setMapParameters() {
        super.setMapParameters()
        val top = resources.getDimension(R.dimen.status_bar_height).toInt()
        setMapPadding(0, top, 0, mCarouselHeight + mPaddingExtra)
    }

    public override fun getDefaultSelectedMapItem(): Int {
        return DEFAULT_SELECTED_ITEM
    }

    override fun showError(requestError: RequestError) {
        ToastUtils.show(requestError.body)
    }

    override fun getMapViewId(): Int {
        return R.id.map_view
    }

    //TODO: remove updates

}