package org.evcs.android.activity

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.databinding.ActivityStationsBinding
import org.evcs.android.model.Station
import org.evcs.android.ui.view.shared.StationView

class StationActivity : BaseActivity2() {
    private lateinit var mBinding: ActivityStationsBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityStationsBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mBinding.activityStationsToolbar.navigationIcon = null
        mBinding.activityStationsToolbar.title = "Details"
        mBinding.activityStationsToolbar.setNavigationOnClickListener { finish() }
        val stations = intent.getSerializableExtra("Stations") as ArrayList<Station>
        val price = stations.first().pricing.detail.priceKwh
        mBinding.activityStationsSessionFee.text = String.format("%.2f USD/kWh", price)
        stations.forEach { station ->
            val v = StationView(this, station)
            mBinding.activityStationsStations.addView(v)
        }
    }
}