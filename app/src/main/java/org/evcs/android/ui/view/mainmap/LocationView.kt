package org.evcs.android.ui.view.mainmap

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.base.core.util.NavigationUtils
import com.base.core.util.NavigationUtils.IntentExtra
import org.evcs.android.R
import org.evcs.android.activity.ContactSupportActivity
import org.evcs.android.databinding.ViewLocationBinding
import org.evcs.android.model.Location
import org.evcs.android.model.Station
import org.evcs.android.util.Extras
import org.evcs.android.util.LocationUtils

class LocationView : LinearLayout {
    private lateinit var mBinding: ViewLocationBinding

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {}

    fun init(context: Context?) {
        mBinding = ViewLocationBinding.inflate(LayoutInflater.from(context), this, true)

        mBinding.fragmentLocationContactSupport.setOnClickListener {
            val extra = IntentExtra(Extras.ContactSupportActivity.SHOW_ADDRESS, true)
            NavigationUtils.jumpTo(context!!, ContactSupportActivity::class.java, extra)
        }
        mBinding.fragmentLocationReportIssue.setOnClickListener {
            //TODO: report issue
        }

    }

    fun setStartChargingListener(listener: (View) -> Unit) {
        mBinding.viewLocationStartCharging.setOnClickListener(listener)
    }

    fun setLocation(location: Location) {
        mBinding.viewLocationTitle.text = location.name
        mBinding.viewLocationAddress.text = location.address.toString()
        mBinding.viewLocationPicture.setImageURI(location.imageUrls?.firstOrNull())

        mBinding.viewLocationConnectors.removeAllViews()
        location.stations!!.forEach { station ->
            val v = StationView(context, station)
            mBinding.viewLocationConnectors.addView(v)
        }

        if (location.comingSoon!!) {
            mBinding.viewLocationGo.visibility = View.GONE
            mBinding.viewLocationStartCharging.visibility = View.GONE
            return
        }

        if (location.gatecode != null) {
            mBinding.viewLocationGatecode.text = context.getString(R.string.location_view_gatecode) + location.gatecode
            mBinding.viewLocationGatecode.visibility = VISIBLE
        }
        mBinding.viewLocationHint.text = location.directions
        if (location.directions == null) mBinding.viewLocationHint.visibility = View.GONE
        showPriceIfExists(mBinding.viewLocationTypePriceAc, location.acPrice, context.getString(R.string.location_view_ac_price_format))
        showPriceIfExists(mBinding.viewLocationTypePriceDc, location.dcPrice, context.getString(R.string.location_view_dc_price_format))

        mBinding.viewLocationGo.setOnClickListener {
            LocationUtils.launchGoogleMapsWithPin(context, location.latLng, location.gatecode, getFragmentManager())
        }

        //        mBinding.activityLocationHint.text = response?

    }

    private fun showPriceIfExists(textView: TextView, price: Float, format: String) {
        textView.text = String.format(format, price)
        textView.visibility = if (price > 0) VISIBLE else GONE
    }

    private fun getFragmentManager(): FragmentManager {
        return ((context as ContextWrapper).baseContext as FragmentActivity).supportFragmentManager
    }

    //Gotta love extensions
    fun View.resize(height: Int) {
        val params = this.layoutParams
        params.height = height
        this.layoutParams = params
    }

    fun resizePicture(height: Int) {
        mBinding.viewLocationPicture.resize(height)
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