package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import org.evcs.android.databinding.ViewStationBinding
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

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs, defStyleAttr) {
        init(context)
    }

    protected fun init(context: Context) {
        val binding = ViewStationBinding.inflate(LayoutInflater.from(context), this, true)
        binding.stationAc.text = if (mStation.chargerType.mIsAc) "Ac" else "Dc"
        binding.stationIcon.setImageDrawable(resources.getDrawable(mStation.chargerType.mIcon))
        binding.stationPower.text = String.format("%.0fkW", mStation.kw)
        binding.stationAvailable.text = mAvailable.toString()
        binding.stationTotal.text = "/$mTotal"
        binding.stationPrice.text = String.format("%.2f USD/kWh", mStation.pricing.detail.priceKwh)
        binding.stationType.text = mStation.chargerType.printableName
    }

}