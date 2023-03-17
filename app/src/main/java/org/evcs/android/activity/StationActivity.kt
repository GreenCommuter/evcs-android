package org.evcs.android.activity

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.databinding.ActivityStationsBinding
import org.evcs.android.model.PricingDetail
import org.evcs.android.model.Station
import org.evcs.android.ui.view.shared.StationView
import org.evcs.android.util.Extras

class StationActivity : BaseActivity2() {
    private lateinit var mBinding: ActivityStationsBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityStationsBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mBinding.activityStationsToolbar.setNavigationOnClickListener { finish() }
        val stations = intent.getSerializableExtra(Extras.StationsActivity.STATIONS) as ArrayList<Station>

        showPricing(stations.first().pricing!!.detail)

        stations.forEach { station ->
            val v = StationView(this, station)
            mBinding.activityStationsStations.addView(v)
        }
    }

    private fun showPricing(pricingDetail: PricingDetail) {
        mBinding.activityStationsSessionFee.text = pricingDetail.printPriceKwh()

        if (pricingDetail.printConnectionFee() != null) {
            mBinding.activityStationsConnectionFee.visibility = View.VISIBLE
            mBinding.activityStationsConnectionFeeText.visibility = View.VISIBLE
            mBinding.activityStationsConnectionFee.text = pricingDetail.printConnectionFee()
        }

        if (pricingDetail.printBufferTime() != null) {
            mBinding.activityStationsGracePeriod.visibility = View.VISIBLE
            mBinding.activityStationsGracePeriodClarification.visibility = View.VISIBLE
            mBinding.activityStationsGracePeriod.text = "(Grace period: " + pricingDetail.printBufferTime() + ")"
        }

        if (pricingDetail.printOccupancyFee() != null) {
            mBinding.activityStationsOccupancySeparator.visibility = View.VISIBLE
            mBinding.activityStationsOccupancyFeeText.visibility = View.VISIBLE
            mBinding.activityStationsOccupancyFee.visibility = View.VISIBLE
            mBinding.activityStationsOccupancyFee.text = pricingDetail.printOccupancyFee()
        }

    }
}