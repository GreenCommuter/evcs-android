package org.evcs.android.features.map

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import org.evcs.android.databinding.ViewLocationBinding
import org.evcs.android.model.Location
import org.evcs.android.model.Station
import org.evcs.android.ui.view.shared.StationView
import org.evcs.android.util.LocationUtils

class LocationView : LinearLayout {
    private lateinit var mBinding: ViewLocationBinding

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr) {
    }

    fun init(context: Context?) {
        mBinding = ViewLocationBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setStartChargingListener(listener: (View) -> Unit) {
        mBinding.viewLocationStartCharging.setOnClickListener(listener)
    }

    fun setLocation(location: Location) {
        mBinding.viewLocationTitle.text = location.name
        mBinding.viewLocationAddress.text = location.address.toString()
        mBinding.viewLocationPicture.setImageURI(location.imageUrls?.get(0))
        mBinding.viewLocationGo.setOnClickListener {
            LocationUtils.launchGoogleMapsWithPin(context, location.latLng, location.gatecode,
                ((context as ContextWrapper).baseContext as FragmentActivity).supportFragmentManager
            )
        }

        //        mBinding.activityLocationHint.text = response?
        mBinding.activityLocationConnectors.removeAllViews()
        location?.stations!!.forEach { station ->
            val v = StationView(context, station)
            mBinding.activityLocationConnectors.addView(v)
        }

    }

    /*static*/ fun resize(view: View, height: Int) {
        val params = view.layoutParams
        params.height = height
        view.layoutParams = params
    }

    fun resizePicture(height: Int) {
        resize(mBinding.viewLocationPicture, height)
    }

    fun pack(stations: List<Station>): ArrayList<List<Station>> {
//        val res = ArrayList<Triple<Station, Int, Int>>()
        val map = stations.groupBy { station -> station.chargerType }
        return ArrayList(map.values)
//        res.addAll(map.map { entry ->
//            Triple(entry.value.first(),
//                   entry.value.count { station -> station.availableStatus == "AVAILABLE" },
//                   entry.value.size)
//        })
//        return res
    }

    fun countAvailable(stations: List<Station>): Int {
        return stations.count { station -> station.isAvailable }
    }
}