package org.evcs.android.ui.view.mainmap

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.fragment.app.FragmentManager
import com.base.core.util.NavigationUtils
import org.evcs.android.BaseConfiguration
import org.evcs.android.R
import org.evcs.android.activity.ContactSupportActivity
import org.evcs.android.activity.WebViewActivity
import org.evcs.android.databinding.ViewLocationBinding
import org.evcs.android.model.Location
import org.evcs.android.model.Station
import org.evcs.android.util.LocationUtils
import org.evcs.android.util.ViewUtils.showOrHide

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
            NavigationUtils.jumpTo(context!!, ContactSupportActivity::class.java)
        }
        mBinding.fragmentLocationReportIssue.setOnClickListener {
            context!!.startActivity(WebViewActivity.buildIntent(context,
                    "Report An Issue", BaseConfiguration.WebViews.REPORT_URL))
        }

    }

    fun setStartChargingListener(listener: (View) -> Unit) {
        mBinding.viewLocationStartCharging.setOnClickListener(listener)
    }

    fun setLocation(location: Location) {
        mBinding.viewLocationStartCharging.isEnabled = true
        mBinding.viewLocationTitle.text = location.name
        mBinding.viewLocationAddress.text = location.address.toString()
        mBinding.viewLocationPicture.setImageURI(location.imageUrls?.firstOrNull())
        mBinding.viewLocationPicture.isVisible = location.imageUrls?.firstOrNull() != null

        mBinding.viewLocationConnectors.removeAllViews()
        location.stations!!.forEach { station ->
            val v = StationView(context, station)
            mBinding.viewLocationConnectors.addView(v)
        }

        if (location.comingSoon!!) {
            mBinding.viewLocationStartCharging.visibility = View.GONE
            mBinding.viewLocationHint.showOrHide("Coming soon")
            return
        }

        if (!TextUtils.isEmpty(location.gatecode)) {
            mBinding.viewLocationGatecode.text = context.getString(R.string.location_view_gatecode, location.gatecode)
            mBinding.viewLocationGatecode.visibility = VISIBLE
        }
        mBinding.viewLocationHint.showOrHide(location.directions)
//        mBinding.viewLocationHint.text = location.directions
//        if (location.directions == null) mBinding.viewLocationHint.visibility = View.GONE
        showPriceIfExists(mBinding.viewLocationTypePriceAc, location.acPrice, context.getString(R.string.location_view_ac_price_format))
        showPriceIfExists(mBinding.viewLocationTypePriceDc, location.dcPrice, context.getString(R.string.location_view_dc_price_format))

        //        mBinding.activityLocationHint.text = response?

    }

    fun addGoButton(location: Location, fragmentManager: FragmentManager) {
        mBinding.viewLocationGo.isInvisible = location.comingSoon!!
        mBinding.viewLocationGo.setOnClickListener {
            LocationUtils.launchGoogleMapsWithPin(context, location.latLng, location.gatecode, fragmentManager)
        }
    }

    private fun showPriceIfExists(textView: TextView, price: Float, format: String) {
        textView.text = String.format(format, price)
        textView.visibility = if (price > 0) VISIBLE else GONE
    }

//    private fun getFragmentManager(): FragmentManager {
//        return ((context as ContextWrapper).baseContext as FragmentActivity).supportFragmentManager
//    }

    //Gotta love extensions
    fun View.resize(height: Int) {
        val params = this.layoutParams
        params.height = height
        this.layoutParams = params
    }

    fun resizePicture(height: Int) {
        mBinding.viewLocationPicture.resize(height)
    }

    //The space for the image will be counted even if it's set as gone later, but it's ok
    fun getMinVisibleHeight(): Int {
        mBinding.root.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        var res = 0
        for (i in 0..mBinding.root.childCount) {
            val v = mBinding.root.getChildAt(i)
            res += v.measuredHeight
            res += v.marginBottom
            res += v.paddingBottom
            if (v == mBinding.viewLocationGatecode)
                return res + 4
        }
        return 0
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