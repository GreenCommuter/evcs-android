package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
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
        binding.stationType.text = mStation.getConnectorType().printableName
        binding.stationPower.text = mStation.getChargerType().printableName + ": " + mStation.printKw()
        binding.stationId.text = String.format("Station ID: %d", mStation.id)
        binding.stationIcon.setImageDrawable(context.getDrawable(mStation.getConnectorType().mIcon))
        binding.stationAvailable.text = mStation.printAvailableStatus()

        val state = intArrayOf(mStation.getAvailableStatus().state)
        binding.stationAvailableDot.setImageState(state, false)
        binding.stationIcon.setImageState(state, false)
        setTextViewState(binding.stationType, state)
        setTextViewState(binding.stationAvailable, state)
    }

    private fun setTextViewState(textView: TextView, state: IntArray) {
        textView.setTextColor(textView.textColors.getColorForState(state, 0))
    }

}