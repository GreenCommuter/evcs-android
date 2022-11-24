package org.evcs.android.activity.location

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.activity.StationActivity
import org.evcs.android.databinding.ActivityLocationBinding
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.view.shared.StationsView
import org.evcs.android.util.Extras
import org.evcs.android.util.LocationUtils
import java.io.Serializable

class LocationActivity : BaseActivity2(), LocationActivityView {
    private lateinit var mPresenter: LocationActivityPresenter
    private lateinit var mBinding: ActivityLocationBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityLocationBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mPresenter = LocationActivityPresenter(this, EVCSApplication.getInstance().retrofitServices)
        mPresenter.onViewCreated()
    }

    override fun populate() {
        super.populate()
        val l = intent.extras?.get(Extras.LocationActivity.LOCATION) as Location
        showLocation(l)
        //the presenter will not retrieve distance
        mBinding.activityLocationDistance.text = l?.printableDistance
        mPresenter.getLocation(l.id)
    }

    override fun setListeners() {
        mBinding.activityLocationGo.setOnClickListener{
            if (mPresenter.mLocation != null) {
                LocationUtils.launchGoogleMapsWithPin(this, mPresenter.mLocation!!.latLng)
            }
        }
    }

    override fun showLocation(response: Location?) {
        mBinding.activityLocationTitle.text = response?.name
        mBinding.activityLocationPicture.setImageURI(response?.imageUrls?.get(0))
//        mBinding.activityLocationHint.text = response?
        mBinding.activityLocationAddress.text = response?.address.toString()
        mBinding.activityLocationConnectors.removeAllViews()
        mPresenter.pack(response?.stations!!).forEach{ stations ->
            val v = StationsView(this, stations.first(), mPresenter.countAvailable(stations), stations.size)
            v.setOnClickListener {
                val intent = Intent(this, StationActivity::class.java)
                intent.putExtra(Extras.StationsActivity.STATIONS, stations as Serializable)
                startActivity(intent)
            }
            mBinding.activityLocationConnectors.addView(v)
        }
    }

    override fun showError(requestError: RequestError) {
        ToastUtils.show(requestError.body)
    }
}