package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.evcs.android.databinding.ViewStationBinding
import org.evcs.android.model.Station

class StationView : LinearLayout {
    private lateinit var mStation: Station

    constructor(context: Context?, station: Station) : super(context) {
        mStation = station
        init(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr)

    private fun init(context: Context?) {
        val binding = ViewStationBinding.inflate(LayoutInflater.from(context), this, true)
        binding.stationType.text = mStation.chargerType.printableName
        binding.stationAc.text = mStation.chargerType.mAc
        binding.stationPower.text = mStation.printKw()
        binding.stationId.text = String.format("Station ID: %d", mStation.id)
        binding.stationIcon.setImageDrawable(context!!.getDrawable(mStation.chargerType.mIcon))
        binding.stationAvailable.text = mStation.printAvailableStatus()
        binding.stationAvailableDot.isEnabled = mStation.isAvailable
    }


}