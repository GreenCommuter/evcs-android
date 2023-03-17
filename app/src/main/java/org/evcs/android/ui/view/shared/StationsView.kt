package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import org.evcs.android.databinding.ViewStationsBinding
import org.evcs.android.model.Station

class StationsView : RelativeLayout {

    private lateinit var mStation: Station
    private var mAvailable: Int = 0
    private var mTotal: Int = 0

    constructor(context: Context, station : Station, available: Int, total: Int) : super(context) {
        this.mStation = station
        this.mAvailable = available
        this.mTotal = total
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context)
    }

    protected fun init(context: Context) {
        val binding = ViewStationsBinding.inflate(LayoutInflater.from(context), this, true)
        binding.stationAc.text = mStation.getChargerType().mAc
        binding.stationIcon.setImageDrawable(resources.getDrawable(mStation.getChargerType().mIcon))
        binding.stationPower.text = mStation.printKw()
        binding.stationAvailable.text = mAvailable.toString()
        binding.stationAvailable.isEnabled = mAvailable > 0
        binding.stationTotal.text = "/$mTotal"
        binding.stationPrice.text = mStation.pricing!!.detail.printPriceKwh()
        binding.stationType.text = mStation.getChargerType().printableName
    }

}