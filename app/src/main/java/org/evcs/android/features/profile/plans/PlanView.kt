package org.evcs.android.features.profile.plans

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.evcs.android.databinding.ViewPlanBinding
import org.evcs.android.model.Plan
import org.evcs.android.util.UserUtils

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
        val period = plan.renewalPeriod.toSmall()
        mBinding.viewPlanPrice.text =
                formatIfPositive(plan.monthlyPrice, "\$%.2f/$period", "\$0 Membership Fee")
        mBinding.viewPlanButton.isEnabled = true //TODO: is current plan
        mBinding.viewPlanButton.text =
                formatIfPositive(plan.trialDays, "Get %d Days Free", "Get Started")

        mBinding.viewPlanFreq.text = "I charge publicly %d+ times/%s"
        //PAYG: I charge infrequently/I don't need a plan

//        mBinding.viewPlanLimits.text =
//        Standard anytime, Basic anytime: Up to %d kWh/month
//        PAYG: EVCS offers simple, flat rate pricing!

        //Standard anytime, basic anytime
        mBinding.viewPlanLimitAprox.text = "Approximately %d miles"

//        mBinding.viewPlanFlatRate =
//        Standard anytime: "Simple flat rate after %d kWh exceeded"
//        Basic anytime: "Flat rate after %d kWh exceeded"
//        PAYG: "Simple flat rate pricing"
//        unlimited anytime: "Unlimited charging 24/7"
//        unlimited offpeak: "Unlimited charging 10PM-6AM"

        //unlimited anytime: hide
        mBinding.viewPlanDcPrice.text = "DC fast: \$%.2f"
        mBinding.viewPlanDcPrice.text = "Level 2: \$%.2f"

        val currentPlanId = UserUtils.getLoggedUser().activeSubscription?.id
        if (currentPlanId == mPlan.id) {
            mBinding.viewPlanButton.isEnabled = false
            mBinding.viewPlanAd.text = "Current plan"
            mBinding.viewPlanAd.setTextColor(Color.parseColor("#005387"))
            mBinding.viewPlanAd.setBackgroundColor(Color.parseColor("#E7EFF6"))
        }
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