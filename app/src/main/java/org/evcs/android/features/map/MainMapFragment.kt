package org.evcs.android.features.map

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.FilterActivity
import org.evcs.android.activity.location.LocationActivity
import org.evcs.android.activity.search.SearchActivity
import org.evcs.android.databinding.FragmentMainMapBinding
import org.evcs.android.model.FilterState
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.view.shared.SearchLocationChildFragment
import org.evcs.android.ui.view.shared.SearchLocationChildFragment.LocationHistoryItem
import org.evcs.android.util.*

class MainMapFragment : ErrorFragment<MainMapPresenter>(), IMainMapView, FragmentLocationReceiver,
        MainMapFragment2.LocationClickListener {

    private lateinit var mToggleButton: TextView
    private lateinit var mSearchLocationChildFragment: SearchLocationChildFragment
    private lateinit var mMainMapFragment2: MainMapFragment2
    private lateinit var mListFragment: SearchActivity

    private lateinit var mFilterButton: ImageButton

    fun newInstance(): MainMapFragment {
        val args = Bundle()
        val fragment = MainMapFragment()
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
        mToggleButton = binding.mapSearch
        mFilterButton = binding.mapFilter
    }

    override fun init() {
        LocationHelper().init(this)
    }

    override fun populate() {
        super.populate()
        mSearchLocationChildFragment = SearchLocationChildFragment.newInstance()
//        mSearchLocationChildFragment.setDefault("asdasd")
        requireFragmentManager().beginTransaction().replace(R.id.fragment_search_location_address_layout, mSearchLocationChildFragment).commit()

        mMainMapFragment2 = MainMapFragment2.newInstance()
        mMainMapFragment2.setLocationClickListener(this)
        requireFragmentManager().beginTransaction().replace(R.id.fragment_main_map_layout, mMainMapFragment2).commit()
        mListFragment = SearchActivity.newInstance()
        mListFragment.setLocationClickListener(this)
        requireFragmentManager().beginTransaction().replace(R.id.fragment_list_layout, mListFragment).commit()

    }

    override fun setListeners() {
        val filterActivityTask =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                onFilterResult(result)
            }
        mToggleButton.setOnClickListener {
            val view = requireView().findViewById<FrameLayout>(R.id.fragment_main_map_layout)
            mToggleButton.text = if (view.isVisible) "Map" else "List"
            view.visibility = if (view.isVisible) View.GONE else View.VISIBLE
        }
        mFilterButton.setOnClickListener { filterActivityTask.launch(getIntentWithFilterState(FilterActivity::class.java)) }
//        addOnCameraChangeListener {
//                cameraPosition -> if (cameraPosition.zoom < ZOOM_LIMIT) showCarousel(false)
//        }

        mSearchLocationChildFragment.setListener(object : SearchLocationChildFragment.ISearchLocationListener {
            override fun onLatLngChosen(address: String, latLng: LatLng, viewport: LatLngBounds?) {
                this@MainMapFragment.onLocationChosen(address, latLng, LocationUtils.addDiagonal(viewport))
            }

            override fun onLocationChosen(location: Location) {
                //TODO: seleccionar pin
                this@MainMapFragment.onLocationClicked(location)
            }

            override fun onLocationRemoved() {
//                clear()
            }

            override fun onCloseClicked() {
//                this@MainMapFragment.finish()
            }
        })

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

    private fun showCarousel(show: Boolean) {
        //It starts as gone
//        if (show) mCarouselRecycler.visibility = View.VISIBLE
//        isCarouselShowing = show
//        AnimationUtils.animateTranslation(mCarouselRecycler, if (show) 0f else mMapBottomPadding.toFloat())
//        AnimationUtils.animateTranslation(mCenterButton, if (show) 0f else mMapBottomPadding.toFloat())
//        setMapPadding(0, mMapTopPadding, 0, if (show) mMapBottomPadding else 0)
    }

    override fun onLocationNotRetrieved() {
        getLocations()
        mMainMapFragment2.onLocationNotRetrieved()
    }

    override fun onLocationResult(lastLocation: android.location.Location) {
        //Only for distances
        presenter?.mLastLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
        mMainMapFragment2.onLocationResult(lastLocation)
        getLocations()
    }

    private fun getLocations() {
        presenter?.getLocations()
    }

    override fun onEmptyResponse() {
        hideLoading()
        showError(RequestError("No chargers found"))
        mListFragment.onEmptyResponse()
    }

    override fun showInitialLocations(response: List<Location?>) {
        hideLoading()
        mMainMapFragment2.showInitialLocations(response)
//        mListFragment.showLocations(response, viewport)
    }

    override fun showLocations(response: List<Location?>, viewport: LatLngBounds?) {
        hideLoading()
        mListFragment.showLocations(response, viewport)
        mMainMapFragment2.showLocations(response, viewport)
    }

    private fun onLocationChosen(address: String, latLng: LatLng, viewport: LatLngBounds?) {
        showLoading()
        presenter.search(latLng, viewport)
    }

    override fun showLoading() {
        showProgressDialog()
    }

    override fun hideLoading() {
        hideProgressDialog()
    }

    override fun getProgressDialogLayout(): Int {
        return R.layout.spinner_layout_black
    }

    override fun onLocationClicked(location: Location) {
        SearchLocationChildFragment.saveToLocationHistory(location)
        val view = requireView().findViewById<FrameLayout>(R.id.fragment_main_map_layout)
        if (view.isVisible) {
            val locationDialog = LocationItemView(location)
            locationDialog.show(fragmentManager)
        } else {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            intent.putExtra(Extras.LocationActivity.LOCATION, location)
            startActivity(intent)
        }
    }

}