package org.evcs.android.features.map

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.base.core.util.NavigationUtils
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentMainMapBinding
import org.evcs.android.features.main.MainActivity
import org.evcs.android.features.map.clustermap.InnerMapFragment
import org.evcs.android.features.map.location_list.LocationListFragment
import org.evcs.android.features.map.search.SearchLocationChildFragment
import org.evcs.android.features.profile.plans.PlansActivity
import org.evcs.android.features.shared.EVCSSliderDialogFragment
import org.evcs.android.model.FilterState
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.FragmentLocationReceiver
import org.evcs.android.util.LocationHelper
import org.evcs.android.util.ViewUtils.setMargins


class MainMapFragment : ErrorFragment<MainMapPresenter>(), IMainMapView, FragmentLocationReceiver,
        InnerMapFragment.LocationClickListener, FilterDialogFragment.FilterDialogListener {

    private var mIsMapShowing: Boolean = true
    private lateinit var mStatusBarSpacing: View
    private lateinit var mToolbarBackground: View
    private lateinit var mMapLayout: FrameLayout
    private lateinit var mListLayout: FrameLayout
    private lateinit var mToggleButton: TextView
    private lateinit var mSearchLocationChildFragment: SearchLocationChildFragment
    private lateinit var mInnerMapFragment: InnerMapFragment
    private lateinit var mListFragment: LocationListFragment
    private lateinit var mBackButton: ImageView
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
        mBackButton = binding.fragmentMainMapBack
        mToolbarBackground = binding.fragmentSearchLocationAddressParent
        mStatusBarSpacing = binding.fragmentMainMapStatusBar
        mMapLayout = binding.fragmentMainMapLayout
        mListLayout = binding.fragmentListLayout
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

        if ((activity as MainActivity).isBottomOfStack) return
        mBackButton.visibility = View.VISIBLE
        mToolbarBackground.setBackgroundColor(resources.getColor(R.color.evcs_transparent_white))
        setStatusBarColor(resources.getColor(R.color.evcs_transparent_white))
    }

    private fun setStatusBarColor(color: Int) {
        mStatusBarSpacing.setBackgroundColor(color)
    }

    fun isMapShowing(): Boolean {
        return mMapLayout.isVisible
    }

    fun showMap() {
        mToggleButton.text = "List"
        mMapLayout.isVisible = true
        mListLayout.isVisible = false
    }

    fun hideMap() {
        mToggleButton.text = "Map"
        mMapLayout.isVisible = false
        mListLayout.isVisible = true
    }

    override fun setListeners() {
        (mToggleButton.parent as View).setOnClickListener {
            if (isMapShowing()) hideMap() else showMap()
        }
        mFilterButton.setOnClickListener {
            FilterDialogFragment(presenter.mFilterState).withListener(this).show(fragmentManager)
        }
//        addOnCameraChangeListener {
//                cameraPosition -> if (cameraPosition.zoom < ZOOM_LIMIT) showCarousel(false)
//        }

        mSearchLocationChildFragment.setListener(object : SearchLocationChildFragment.ISearchLocationListener {
            override fun onLatLngChosen(address: String, latLng: LatLng, viewport: LatLngBounds?) {
                this@MainMapFragment.onLocationChosen(address, latLng, viewport)
            }

            override fun onLocationChosen(location: Location) {
                mInnerMapFragment.findAndSelectMarker(location)
            }

            override fun onLocationRemoved() {
//                clear()
            }
        })
        mBackButton.setOnClickListener { requireActivity().finish() }
    }

    override fun onFilterResult(filterState: FilterState) {
        showLoading()
        mFilterButton.isSelected = !filterState.isEmpty()
        presenter.onFilterResult(filterState)
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
        if (presenter.mCachedLocations != null) {
            showInitialLocations(presenter.mCachedLocations!!, presenter.mLastLocation != null)
        } else {
            showProgressDialog()
            presenter?.getInitialLocations(presenter.mLastLocation)
        }
    }

    override fun onEmptyResponse() {
        hideLoading()
        showError(RequestError("No chargers found"))
        mListFragment.onEmptyResponse()
    }

    override fun showInitialLocations(response: List<Location>, showInList: Boolean) {
        hideLoading()
        mInnerMapFragment.showInitialLocations(response)
        if (showInList)
            mListFragment.showLocations(response, null)
        else
            showHistoryInList()
    }

    private fun showHistoryInList() {
        val history = SearchLocationChildFragment.getLocationHistory().map { item -> item.location }
        if (history.size > 0)
            mListFragment.showLocations(history, null)
    }

    override fun showLocations(response: List<Location>, viewport: LatLngBounds?) {
        hideLoading()
        mListFragment.showLocations(response, null)
        mInnerMapFragment.zoomToLocations(response, viewport)
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
        mIsMapShowing = isMapShowing()
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

    override fun onResume() {
        super.onResume()
        if (mIsMapShowing) showMap() else hideMap()
    }

    fun showWelcomeDialog() {
        EVCSSliderDialogFragment.Builder()
            .setTitle(getString(R.string.welcome_dialog_title), R.style.Label_Large)
            .setSubtitle(getString(R.string.welcome_dialog_subtitle))
            .addButton(getString(R.string.app_close)) { fragment -> fragment.dismiss() }
            .show(childFragmentManager)
    }

    fun showSuccessDialog() {
        val textView = TextView(context)
        textView.text = getString(R.string.success_dialog_cta)
        textView.setTextAppearance(context, R.style.Label_Medium)
        textView.gravity = Gravity.CENTER
        textView.setMargins(0, 0, 0, resources.getDimension(R.dimen.spacing_big_k).toInt())

        EVCSSliderDialogFragment.Builder()
            .setTitle(getString(R.string.success_dialog_title), R.style.Label_Large)
            .setSubtitle(getString(R.string.success_dialog_subtitle))
            .addView(textView)
            .addButton("Get 30 Days Free") { NavigationUtils.jumpTo(requireContext(), PlansActivity::class.java) }
            .show(childFragmentManager)
    }

    fun showCongratulationsDialog() {
        EVCSSliderDialogFragment.Builder()
            .setTitle(getString(R.string.congratulations_dialog_title), R.style.Label_Large)
            .setSubtitle(getString(R.string.congratulations_dialog_subtitle))
            .addButton(getString(R.string.app_close)) { fragment -> fragment.dismiss() }
            .show(childFragmentManager)
    }

    fun showAccountNotValidatedDialog() {
        EVCSSliderDialogFragment.Builder()
            .setTitle(getString(R.string.account_not_validated_title))
            .setSubtitle(getString(R.string.account_not_validated_subtitle))
            .addButton(getString(R.string.account_not_validated_button), {
                    fragment -> fragment.dismiss()
                    //TODO: go to validation (make activity)
                },
                R.drawable.layout_corners_rounded_blue)
            .addButton(getString(R.string.app_close), { fragment -> fragment.dismiss() }, R.drawable.layout_corners_rounded_black_outline, R.color.button_text_color_selector_black_outline)
            .show(childFragmentManager)
    }

}