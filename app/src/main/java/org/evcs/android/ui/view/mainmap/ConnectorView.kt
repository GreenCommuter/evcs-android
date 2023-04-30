package org.evcs.android.ui.view.mainmap

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import org.evcs.android.databinding.ViewConnectorBinding
import org.evcs.android.model.ConnectorType
import org.evcs.android.model.Station

class ConnectorView : LinearLayout {
    private lateinit var mAvailableStatus: Station.AvailableStatus
    private lateinit var mConnector: ConnectorType

    constructor(context: Context, connector: ConnectorType, availableStatus: Station.AvailableStatus) : super(context) {
        mConnector = connector
        mAvailableStatus = availableStatus
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr)

    private fun init(context: Context) {
        val binding = ViewConnectorBinding.inflate(LayoutInflater.from(context), this, true)
        binding.stationType.text = mConnector.printableName
        binding.stationIcon.setImageDrawable(context.getDrawable(mConnector.mIcon))
        binding.stationAvailable.text = mAvailableStatus.toPrintableString()

        val state = intArrayOf(mAvailableStatus.state)
        binding.stationAvailableDot.setImageState(state, false)
        binding.stationIcon.setImageState(state, false)
        setTextViewState(binding.stationType, state)
        setTextViewState(binding.stationAvailable, state)
    }

    private fun setTextViewState(textView: TextView, state: IntArray) {
        textView.setTextColor(textView.textColors.getColorForState(state, 0))
    }

}