package org.evcs.android.features.profile.plans

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.evcs.android.databinding.ViewLocationBinding
import org.evcs.android.databinding.ViewPlanBinding

class PlanView : LinearLayout {

    private lateinit var mBinding: ViewPlanBinding

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {}


    private fun init(context: Context) {
        mBinding = ViewPlanBinding.inflate(LayoutInflater.from(context), this, true)
    }
}