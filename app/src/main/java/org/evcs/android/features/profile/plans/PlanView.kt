package org.evcs.android.features.profile.plans

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import org.evcs.android.R
import org.evcs.android.databinding.ViewPlanBinding
import org.evcs.android.model.Plan
import org.evcs.android.model.user.User
import org.evcs.android.util.UserUtils

class PlanView : LinearLayout {

    private var mPlan: Plan? = null
    private lateinit var mBinding: ViewPlanBinding

    constructor(context: Context, plan: Plan?) : super(context) {
        init(context)
        if (plan == null) {
            setPayAsYouGo()
        } else {
            setPlan(plan)
        }
    }

    fun setPayAsYouGo() {
        mBinding.viewPlanName.text = "Pay as you go"
        mBinding.viewPlanPrice.text = "\$0 Membership Fee"

        mBinding.viewPlanButton.text = "Get Started"

        mBinding.viewPlanFreq.text = "I charge infrequently / I don't need a plan"
        mBinding.viewPlanLimits.text = "EVCS offers simple, flat rate pricing!"

        mBinding.viewPlanFlatRate.text = "Simple flat rate pricing"
        mBinding.viewPlanDcPrice.text = "DC fast: \$%.2f"
        mBinding.viewPlanAcPrice.text = "Level 2: \$%.2f"

        if (UserUtils.getLoggedUser()?.activeSubscription == null
            && UserUtils.getLoggedUser()?.defaultPm != null) {
            setCurrentPlan()
        }
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

        mBinding.viewPlanButton.text = plan.cta

        mBinding.viewPlanFreq.text = "I charge publicly %d+ times/%s"

//        mBinding.viewPlanLimits.text =
//        Standard anytime, Basic anytime: Up to %d kWh/month

        //Standard anytime, basic anytime
        mBinding.viewPlanLimitAprox.text = "Approximately %d miles"

//        mBinding.viewPlanFlatRate =
//        Standard anytime: "Simple flat rate after %d kWh exceeded"
//        Basic anytime: "Flat rate after %d kWh exceeded"
//        unlimited anytime: "Unlimited charging 24/7"
//        unlimited offpeak: "Unlimited charging 10PM-6AM"

        //unlimited anytime: hide
        mBinding.viewPlanDcPrice.text = "DC fast: \$%.2f"
        mBinding.viewPlanAcPrice.text = "Level 2: \$%.2f"

        val currentPlanId = UserUtils.getLoggedUser()?.activeSubscription?.plan?.id
        if (currentPlanId == plan.id) {
            setCurrentPlan()
        }
    }

    private fun setCurrentPlan() {
        mBinding.viewPlanButton.isEnabled = false
        mBinding.viewPlanButton.text = "Current plan"
        mBinding.viewPlanAd.visibility = View.VISIBLE
        mBinding.viewPlanAd.text = "Current plan"
        mBinding.viewPlanAd.setTextColor(resources.getColor(R.color.evcs_secondary_800))
        mBinding.viewPlanAd.setBackgroundColor(Color.parseColor("#E7EFF6"))
    }

    fun formatIfPositive(field: Number, format: String, default: String): String {
        return if (field.toFloat() != 0f) String.format(format, field) else default
    }

    fun setListener(listener: PlanViewListener) {
        mBinding.viewPlanButton.setOnClickListener { listener.onGetPlanClicked(mPlan) }
        mBinding.viewPlanLearnMore.setOnClickListener { listener.onLearnMoreClicked(mPlan) }
    }

    interface PlanViewListener {
        fun onGetPlanClicked(plan: Plan?)
        fun onLearnMoreClicked(plan: Plan?)
    }

}