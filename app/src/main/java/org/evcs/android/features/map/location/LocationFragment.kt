package org.evcs.android.features.map.location

import android.view.View
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentLocationBinding
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.model.Location
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras

class LocationFragment : ErrorFragment<LocationPresenter>(), ILocationView {

    private lateinit var mBinding: FragmentLocationBinding

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentLocationBinding.bind(v)
    }

    override fun layout(): Int {
        return R.layout.fragment_location
    }

    override fun init() {}

    override fun populate() {
        super.populate()
        val l = arguments?.getSerializable(Extras.LocationActivity.LOCATION) as Location
        showLocation(l)
        //the presenter will not retrieve distance
//        mBinding.activityLocationDistance.text = l?.printableDistance
//        presenter.getLocation(l.id)
    }

    override fun createPresenter(): LocationPresenter {
        return LocationPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setListeners() {
        mBinding.activityLocationLocation.setStartChargingListener {
            MainNavigationController.getInstance().goToPreCharging()
        }
    }

    override fun showLocation(response: Location?) {
        mBinding.activityLocationLocation.setLocation(response!!)
        mBinding.activityLocationLocation.resizePicture(
            resources.getDimension(R.dimen.location_fragment_image_height).toInt())
    }

}