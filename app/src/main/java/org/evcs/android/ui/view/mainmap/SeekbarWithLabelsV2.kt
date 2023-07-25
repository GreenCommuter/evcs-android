package org.evcs.android.ui.view.mainmap

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import org.evcs.android.R


class SeekbarWithLabelsV2 : LinearLayout {
    private lateinit var mLabels: Array<String>
    private var mProgress = 0
    private var mViews = ArrayList<TextView>()
    private var mListener: ((progress: Int) -> Unit)? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs,defStyleAttr)

    private fun init(context: Context) {}

    fun setListener(listener: (progress: Int) -> Unit) {
        mListener = listener
    }

    fun getProgress(): Int {
        return mProgress
    }

//    0 : 1000
//    1 : 0111
//    2 : 0011
//    3 : 0001
    fun setProgress(progress: Int) {
        mProgress = progress
        for (i in mViews.indices) {
            setSelected(mViews[i], i >= progress && (progress > 0 || i == 0))
        }
    }

    fun setSelected(view: TextView, selected: Boolean) {
        view.isSelected = selected
        view.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
    }

    fun setLabels(labels: Array<String>) {
        mLabels = labels
        for (i in labels.indices) {
            val view = TextView(ContextThemeWrapper(context, R.style.ChipStandard), null, 0)
            view.text = labels[i]
            view.setOnClickListener {
                setProgress(i)
                mListener?.invoke(mProgress)
            }
            addView(view)
            mViews.add(view)
            if (i < labels.size - 1) {
                val spacing = View(context)
                val params = LayoutParams(0, -1, 1f)
                addView(spacing, params)
            }
        }
    }
}