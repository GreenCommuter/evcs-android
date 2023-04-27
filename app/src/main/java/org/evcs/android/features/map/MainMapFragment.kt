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
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.FilterActivity
import org.evcs.android.activity.location_list.LocationListFragment
import org.evcs.android.databinding.FragmentMainMapBinding
import org.evcs.android.model.FilterState
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.view.shared.SearchLocationChildFragment
import org.evcs.android.util.*

class MainMapFragment : ErrorFragment<MainMapPresenter>(), IMainMapView, FragmentLocationReceiver,
        InnerMapFragment.LocationClickListener {

    private lateinit var mToggleButton: TextView
    private lateinit var mSearchLocationChildFragment: SearchLocationChildFragment
    private lateinit var mInnerMapFragment: InnerMapFragment
    private lateinit var mListFragment: LocationListFragment

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
        mToggleButton = binding.mapToggle
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

        mInnerMapFragment = InnerMapFragment.newInstance()
        mInnerMapFragment.setLocationClickListener(this)
        requireFragmentManager().beginTransaction().replace(R.id.fragment_main_map_layout, mInnerMapFragment).commit()
        mListFragment = LocationListFragment.newInstance()
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
                mInnerMapFragment.findAndSelectMarker(location)
            }

            override fun onLocationRemoved() {
//                clear()
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

    override fun onLocationNotRetrieved() {
        getInitialLocations()
        mInnerMapFragment.onLocationNotRetrieved()
    }

    override fun onLocationResult(lastLocation: android.location.Location) {
        //Only for distances?
        presenter?.mLastLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
        mInnerMapFragment.onLocationResult(presenter.mLastLocation!!)
        getInitialLocations()
    }

    private fun getInitialLocations() {
        presenter?.getInitialLocations()
    }

    override fun onEmptyResponse() {
        hideLoading()
        showError(RequestError("No chargers found"))
        mListFragment.onEmptyResponse()
    }

    override fun showInitialLocations(response: List<Location?>, showInList: Boolean) {
        hideLoading()
        mInnerMapFragment.showInitialLocations(response)
        if (showInList)
            mListFragment.showLocations(response, null)
        else
            showHistoryInList()
    }

    private fun showHistoryInList() {
        val history = SearchLocationChildFragment.getLocationHistory().map { item -> item.location }
        mListFragment.showLocations(history, null)
    }

    override fun showLocations(response: List<Location?>, viewport: LatLngBounds?) {
        hideLoading()
        mListFragment.showLocations(response, null)
        mInnerMapFragment.showLocations(response, viewport)
    }

    private fun onLocationChosen(address: String, latLng: LatLng, viewport: LatLngBounds?) {
        showLoading()
        presenter.searchFromQuery(latLng, viewport)
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

    override fun onLocationClicked(location: Location, showAsSlider: Boolean) {
        SearchLocationChildFragment.saveToLocationHistory(location)
        if (showAsSlider) {
            val locationDialog = LocationSliderFragment(location)
            locationDialog.show(fragmentManager)
        } else {
            val args = Bundle()
            args.putSerializable(Extras.LocationActivity.LOCATION, location)
            findNavController().navigate(R.id.locationFragment, args)
        }
    }

}