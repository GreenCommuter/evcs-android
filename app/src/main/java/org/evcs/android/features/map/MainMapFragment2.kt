package org.evcs.android.features.map

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.evcs.android.R
import org.evcs.android.EVCSApplication
import org.evcs.android.ui.recycler.decoration.SpaceItemDecoration
import androidx.recyclerview.widget.LinearSnapHelper
import org.evcs.android.model.shared.RequestError
import com.base.core.util.ToastUtils
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import org.evcs.android.activity.FilterActivity
import org.evcs.android.activity.SearchActivity
import org.evcs.android.databinding.FragmentMainMapBinding
import org.evcs.android.model.Location
import org.evcs.android.ui.adapter.BaseRecyclerAdapter
import org.evcs.android.ui.adapter.BaseRecyclerAdapterItemClickListener

class MainMapFragment2 : SelectionMapFragment<MainMapPresenter?, Location?>(), IMainMapView {

    val DEFAULT_SELECTED_ITEM = 0
    var mVanpoolCarouselItemSpacing = 0
    var mVanpoolDetailsSize = 0

    var mPaddingExtra = 0
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mMapAdapter: CarouselAdapter
    private lateinit var mVanpoolCarouselScrollListener: RecyclerView.OnScrollListener
    private val mNavigationListener: IVanpoolMapNavigationListener? = null
    private lateinit var mCarouselRecycler: RecyclerView
    private lateinit var mSearchButton: ImageButton
    private lateinit var mFilterButton: ImageButton

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
    }

    override fun init() {
        super.init()
        mVanpoolCarouselItemSpacing = resources.getDimension(R.dimen.spacing_medium).toInt()
        mVanpoolDetailsSize = resources.getDimension(R.dimen.vanpool_map_bottom_padding).toInt()
        mPaddingExtra = resources.getDimension(R.dimen.spacing_medium).toInt()
        initializeRecycler()
        mVanpoolCarouselScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val selectedVanpoolPosition =
                        mLinearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (selectedVanpoolPosition == RecyclerView.NO_POSITION) {
                        return
                    }
                    val selectedLocation = mMapAdapter[selectedVanpoolPosition]
                    val selectedContainer = mVContainerMap[selectedLocation.hashCode()]
                    onContainerClicked(selectedContainer!!)
                }
            }
        }
    }

    override fun populate() {
        super.populate()
        presenter!!.getLocations()
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

    override fun setListeners() {
        mCarouselRecycler.addOnScrollListener(mVanpoolCarouselScrollListener)
        mSearchButton.setOnClickListener { startActivity(Intent(requireContext(), SearchActivity::class.java)) }
        mFilterButton.setOnClickListener { startActivity(Intent(requireContext(), FilterActivity::class.java)) }
    }

    private fun initializeRecycler() {
        mMapAdapter = CarouselAdapter()
        mCarouselRecycler.adapter = mMapAdapter
        mLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mCarouselRecycler.layoutManager = mLinearLayoutManager
        mCarouselRecycler.addItemDecoration(
            SpaceItemDecoration(
                mVanpoolCarouselItemSpacing,
                SpaceItemDecoration.HORIZONTAL
            )
        )
        val onFlingListener = LinearSnapHelper()
        onFlingListener.attachToRecyclerView(mCarouselRecycler)
    }

    override fun showLocations(response: List<Location?>?) {
        showMapItems(response!!)
    }

    //TODO: esto va a cambiar el mapa por cada page
    override fun showMapItems(mapItems: List<Location?>) {
        super.showMapItems(mapItems)
        mMapAdapter.appendBottomAll(mapItems)
        mMapAdapter.setItemClickListener(object : BaseRecyclerAdapterItemClickListener<Location>() {
            override fun onItemClicked(item: Location, adapterPosition: Int) {
                mNavigationListener!!.onLocationDetail(item)
            }
        })
    }

    override fun getContainer(location: Location?, selected: Boolean): Container {
        return Container(location, drawMarker(location?.latLng!!, selected))
    }

//    override fun setMapParameters() {
//        super.setMapParameters()
//        setMapPadding(0, 0, 0, mVanpoolDetailsSize + mPaddingExtra)
//    }

    public override fun getDefaultSelectedMapItem(): Int {
        return DEFAULT_SELECTED_ITEM
    }

    override fun showError(requestError: RequestError) {
        ToastUtils.show(requestError.body)
    }

    /**
     * Listener that provides navigation.
     */
    interface IVanpoolMapNavigationListener /*extends INavigationListener*/ {
        fun onLocationDetail(location: Location?)
    }

    override fun getMapViewId(): Int {
        return R.id.map_view
    }

}