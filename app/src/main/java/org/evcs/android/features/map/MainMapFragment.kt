package org.evcs.android.features.map

import android.view.View
import com.base.core.util.ToastUtils
import com.google.android.gms.maps.model.LatLng
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentMainMapBinding
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.navigation.INavigationListener
import org.evcs.android.util.Extras

class MainMapFragment : AbstractMapFragment<MainMapPresenter>(), IMainMapView {

    private lateinit var mBinding: FragmentMainMapBinding
    private lateinit var mMainMapListener: IMainMapListener
    private var mAlreadyLoaded = false

    fun newInstance(): MainMapFragment {
        return MainMapFragment()
    }

    override fun layout(): Int {
        return R.layout.fragment_main_map
    }

    override fun createPresenter(): MainMapPresenter {
        return MainMapPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        mBinding = FragmentMainMapBinding.bind(v)
        super.setUi(v)
    }

    override fun init() {
        super.init()
        mMainMapListener = MainNavigationController.getInstance()
        if (!mAlreadyLoaded) {
            presenter.getLoggedUser()
        }
    }

    override fun populate() {
        super.populate()
        presenter?.getLocations();
    }

    override fun setListeners() {
    }

    override fun onResume() {
        super.onResume()
        if (isOpeningApp) {
        }
    }

    override fun getMapViewId(): Int {
        return R.id.map_view
    }

    private fun showTermsDialog() {}

    override fun showError(requestError: RequestError) {
        ToastUtils.show(requestError.body)
    }

    private val isOpeningApp: Boolean
        get() = requireActivity().intent.getBooleanExtra(Extras.Root.OPENING_KEY, false)

//    override fun onLoadError(user: User) {
//        mAlreadyLoaded = true
//        //        hideProgressDialog();
//    }

    override fun showLocations(response: List<Location?>?) {
        response?.forEach { location -> drawMarker(LatLng(location!!.latitude, location.longitude), true) }
    }

    /**
     * Listener for the ChooseService Fragment
     */
    interface IMainMapListener : INavigationListener {
        fun onCarSharingSelected(skipPlaceholder: Boolean)
    }

}