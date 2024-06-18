package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import org.evcs.android.R
import org.evcs.android.databinding.ViewStateOfChargeBinding
import org.evcs.android.util.ViewUtils.setParentVisibility

class StateOfChargeView : FrameLayout {

    private lateinit var mBinding: ViewStateOfChargeBinding

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private fun init(context: Context) {
        mBinding = ViewStateOfChargeBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setStart(start: Int) {
        mBinding.viewStateOfCargeBar.progress = start + 2 //The bar looks odd at 1, so I set max at 102
        mBinding.viewStateOfChargeStart.text = context.getString(R.string.state_of_charge_start, start)
    }

    fun setEnd(end: Int) {
        mBinding.viewStateOfCargeBar.secondaryProgress = end + 2
        mBinding.viewStateOfChargeEnd.text = context.getString(R.string.state_of_charge_end, end)
    }

    fun setSupported(supported: Boolean) {
        mBinding.viewStateOfCargeBar.isVisible = supported
        mBinding.viewStateOfChargeStart.setParentVisibility(supported)
        mBinding.viewStateOfCargeNotSupported.isVisible = !supported
    }

    fun setState(start: Int?, end: Int?) {
        if (start == null || end == null) {
            setSupported(false)
        } else {
            setSupported(true)
            setStart(start)
            setEnd(end)
        }
    }
}