package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.SeekBar
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import org.evcs.android.databinding.ViewSeekbarWithLabelsBinding

class SeekbarWithLabels : LinearLayout {
    private lateinit var mLabels: Array<String>
    lateinit var seekbar: SeekBar
    private lateinit var mLabelLayout: LinearLayout

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs,defStyleAttr)

    private fun init(context: Context) {
        val binding = ViewSeekbarWithLabelsBinding.inflate(LayoutInflater.from(context), this, true)
        seekbar = binding.viewSeekbarWithLabelsSeekbar
        mLabelLayout = binding.viewSeekbarWithLabelsLabels
    }

    fun setLabels(labels: Array<String>) {
        mLabels = labels
        seekbar.max = labels.size - 1
        for (i in labels.indices) {
            val view = TextView(context)
            view.text = labels[i]
            mLabelLayout.addView(view)
            if (i < labels.size - 1) {
                val spacing = View(context)
                val params = LayoutParams(0, -1, 1f)
                mLabelLayout.addView(spacing, params)
            }
        }
    }
}