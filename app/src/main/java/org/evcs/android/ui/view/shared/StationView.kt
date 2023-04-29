package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.evcs.android.databinding.ViewStationBinding
import org.evcs.android.model.Station

class StationView : LinearLayout {
    private lateinit var mStation: Station

    constructor(context: Context, station: Station) : super(context) {
        mStation = station
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr)

    private fun init(context: Context) {
        val binding = ViewStationBinding.inflate(LayoutInflater.from(context), this, true)
        binding.stationPower.text = mStation.getChargerType().printableName + ": " + mStation.printKw()
        binding.stationId.text = String.format("Station ID: %d", mStation.id)

        mStation.connectorTypes.forEach { connectorType ->
            val cv = ConnectorView(context, connectorType, mStation.getAvailableStatus())
            val param = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
            cv.layoutParams = param
            binding.stationConnectors.addView(cv)
        }
    }

}