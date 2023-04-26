package org.evcs.android.activity.location

import android.view.View
import androidx.navigation.fragment.findNavController
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentLocationBinding
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.model.Location
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras

class LocationFragment : ErrorFragment<LocationPresenter>(), LocationView {
    private lateinit var mBinding: FragmentLocationBinding

    override fun setUi(v: View?) {
        super.setUi(v)
        mBinding = FragmentLocationBinding.inflate(layoutInflater)
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
        presenter.getLocation(l.id)
    }

    override fun createPresenter(): LocationPresenter {
        return LocationPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setListeners() {
        mBinding.activityLocationClose.setOnClickListener { findNavController().popBackStack() }
    }

    override fun showLocation(response: Location?) {
        mBinding.activityLocationLocation.setLocation(response!!)
        mBinding.activityLocationLocation.resizePicture(200)
        mBinding.activityLocationLocation.setStartChargingListener {
            MainNavigationController.getInstance().goToCharging()
        }
    }

}