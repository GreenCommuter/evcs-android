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
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils.showOrHide

class PlanView : LinearLayout {

    private var mPlan: Plan? = null
    private lateinit var mBinding: ViewPlanBinding

    constructor(context: Context, plan: Plan?) : super(context) {
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

    fun setPlan(plan: Plan?) {
        mPlan = plan
        val helper = PlanViewHelper.instance(context, plan)
        mBinding.viewPlanName.text = helper.getPlanName()
        mBinding.viewPlanPrice.text = helper.getPlanPrice()
        mBinding.viewPlanButton.text = helper.getPlanButton()
        mBinding.viewPlanFreq.text = helper.getPlanFreq()
        mBinding.viewPlanLimitAprox.showOrHide(helper.getPlanLimitAprox());
        mBinding.viewPlanLimits.showOrHide(helper.getPlanLimit())
        mBinding.viewPlanFlatRate.showOrHide(helper.getFlatRate())
        mBinding.viewPlanDcPrice.showOrHide(helper.getDCFastPrice())
        mBinding.viewPlanAcPrice.showOrHide(helper.getLevel2Price())
        mBinding.viewPlanAd.text = plan?.banner
        mBinding.viewPlanAd.visibility = if (plan?.banner == null) INVISIBLE else VISIBLE

        val currentPlanId = UserUtils.getLoggedUser()?.activeSubscription?.plan?.id
        //Only relevant to show PAYG as not current
        val hasPaymentMethod = UserUtils.getLoggedUser()?.defaultPm != null

        if (currentPlanId == plan?.id && hasPaymentMethod) {
            setCurrentPlan()
        }
    }

    fun setPayAsYouGo() {
        setPlan(null)
    }

    private fun setCurrentPlan() {
        mBinding.viewPlanButton.isEnabled = false
        mBinding.viewPlanButton.text = context.getString(R.string.plan_view_current)
        mBinding.viewPlanAd.visibility = View.VISIBLE
        mBinding.viewPlanAd.text = context.getString(R.string.plan_view_current)
        mBinding.viewPlanAd.setTextColor(resources.getColor(R.color.evcs_secondary_800))
        mBinding.viewPlanAd.setBackgroundColor(Color.parseColor("#E7EFF6"))
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