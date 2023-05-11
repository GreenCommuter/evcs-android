package org.evcs.android.features.profile.plans

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.evcs.android.databinding.ViewPlanBinding
import org.evcs.android.model.Plan

class PlanView : LinearLayout {

    private lateinit var mPlan: Plan
    private lateinit var mBinding: ViewPlanBinding

    constructor(context: Context, plan: Plan) : super(context) {
        init(context)
        setPlan(plan)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {}


    private fun init(context: Context) {
        mBinding = ViewPlanBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setPlan(plan: Plan) {
        mPlan = plan
        mBinding.viewPlanName.text = plan.name
        //TODO. yearly, etc
        mBinding.viewPlanPrice.text =
                formatIfPositive(plan.monthlyPrice, "\$%.2f/mo", "\$0 Membership Fee")
        mBinding.viewPlanButton.isEnabled = true //TODO: is current plan
        mBinding.viewPlanButton.text =
                formatIfPositive(plan.trialDays, "Get %d Days Free", "Get Started")
    }

    fun formatIfPositive(field: Number, format: String, default: String): String {
        return if (field.toFloat() != 0f) String.format(format, field) else default
    }

    fun setListener(listener: PlanViewListener) {
        mBinding.viewPlanButton.setOnClickListener { listener.onGetPlanClicked(mPlan) }
        mBinding.viewPlanLearnMore.setOnClickListener { listener.onLearnMoreClicked(mPlan) }
    }

    interface PlanViewListener {
        fun onGetPlanClicked(plan: Plan)
        fun onLearnMoreClicked(plan: Plan)
    }

}