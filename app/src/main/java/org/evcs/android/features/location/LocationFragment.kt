package org.evcs.android.features.location

import android.view.View
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.SimpleToolbarActivity
import org.evcs.android.features.StationFragment
import org.evcs.android.databinding.ActivityLocationBinding
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.view.shared.StationsView
import org.evcs.android.util.Extras
import org.evcs.android.util.LocationUtils
import java.io.Serializable

class LocationFragment : ErrorFragment<LocationPresenter>(), LocationView {
    private lateinit var mBinding: ActivityLocationBinding

    override fun init() {}

    override fun layout(): Int {
        return R.layout.activity_location
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = ActivityLocationBinding.bind(v)
    }

    override fun createPresenter(): LocationPresenter {
        return LocationPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun populate() {
        super.populate()
        val l = requireActivity().intent.extras?.get(Extras.LocationActivity.LOCATION) as Location
        showLocation(l)
        //the presenter will not retrieve distance
        mBinding.activityLocationDistance.text = l?.printableDistance
        presenter.getLocation(l.id)
    }

    override fun setListeners() {
        mBinding.activityLocationGo.setOnClickListener{
            if (presenter.mLocation != null) {
                LocationUtils.launchGoogleMapsWithPin(requireContext(), presenter.mLocation!!.latLng)
            }
        }
        mBinding.activityLocationClose.setOnClickListener { activity?.finish() }
    }

    override fun showLocation(response: Location?) {
        mBinding.activityLocationTitle.text = response?.name
        mBinding.activityLocationPicture.setImageURI(response?.imageUrls?.get(0))
//        mBinding.activityLocationHint.text = response?
        mBinding.activityLocationAddress.text = response?.address.toString()
        mBinding.activityLocationConnectors.removeAllViews()
        presenter.pack(response?.stations!!).forEach{ stations ->
            val v = StationsView(requireContext(), stations.first(), presenter.countAvailable(stations), stations.size)
            v.setOnClickListener {
                val intent = SimpleToolbarActivity.getIntent(requireContext(), StationFragment::class.java, "Details")
                intent.putExtra(Extras.StationsActivity.STATIONS, stations as Serializable)
                startActivity(intent)
            }
            mBinding.activityLocationConnectors.addView(v)
        }
    }

}