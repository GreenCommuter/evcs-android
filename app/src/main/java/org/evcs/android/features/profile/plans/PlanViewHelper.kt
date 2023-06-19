package org.evcs.android.features.profile.plans

import android.content.Context
import org.evcs.android.R
import org.evcs.android.model.Plan

abstract class PlanViewHelper(val mContext: Context) {
    companion object {
        fun instance(context: Context, plan: Plan?): PlanViewHelper {
            if (plan == null)
                return PlanViewHelperPAYG(context)
            if (!plan.isUnlimited)
                return PlanViewHelperCapped(context, plan)
            if (plan.isTimeLimited)
                return PlanViewHelperTimeLimited(context, plan)
            return PlanViewHelperUnlimited(context, plan)
        }
    }
    abstract fun getPlanName() : String
    abstract fun getPlanPrice() : String
    abstract fun getPlanButton() : String
    abstract fun getPlanFreq() : String
    abstract fun getPlanLimit() : String?
    abstract fun getPlanLimitAprox() : String?
    abstract fun getFlatRate(): String?
    abstract fun getDCFastPrice(): String?
    abstract fun getLevel2Price(): String?
}

class PlanViewHelperPAYG(context: Context) : PlanViewHelper(context) {
    override fun getPlanName(): String {
        return "Pay as you go"
    }

    override fun getPlanPrice(): String {
        return  "\$0 Membership Fee"
    }

    override fun getPlanButton(): String {
        return "Get Started"
    }

    override fun getPlanFreq(): String {
        return "I charge infrequently"
    }

    override fun getPlanLimit(): String? {
        return "EVCS offers simple, flat rate pricing!"
    }

    override fun getPlanLimitAprox(): String? {
        return null
    }

    override fun getFlatRate(): String? {
        return "Simple flat rate pricing"
    }

    override fun getDCFastPrice(): String? {
        return "DC fast: \$%.2f"
    }

    override fun getLevel2Price(): String? {
        return "Level 2: \$%.2f"
    }

}

abstract class PlanViewHelperNonNull(context: Context, val mPlan: Plan) : PlanViewHelper(context) {
    override fun getPlanName(): String {
        return mPlan.name
    }

    override fun getPlanPrice(): String {
        val period = mPlan.renewalPeriod.toSmall()
        return String.format("\$%.2f/$period", mPlan.price)
    }

    override fun getPlanButton(): String {
        return mPlan.cta
    }

    override fun getPlanFreq(): String {
        return "I charge publicly %d+ times/%s"
    }
}

abstract class PlanViewHelperLimited(context: Context, plan: Plan) : PlanViewHelperNonNull(context, plan) {
    override fun getDCFastPrice(): String? {
        return String.format("DC fast: \$%.2f", mPlan.pricePerKwh)
    }

    override fun getLevel2Price(): String? {
        return String.format("Level 2: \$%.2f", mPlan.pricePerKwh)
    }
}

class PlanViewHelperCapped(context: Context, plan: Plan) : PlanViewHelperLimited(context, plan) {
    override fun getPlanLimit(): String? {
        return String.format("Up to %d kWh/%s", mPlan.kwhCap(), mPlan.renewalPeriod)
    }

    override fun getPlanLimitAprox(): String? {
        return "Approximately %d miles"
    }

    override fun getFlatRate(): String? {
        return mContext.getString(R.string.plan_view_flat_rate, mPlan.kwhCap())
    }
}

class PlanViewHelperTimeLimited(context: Context, plan: Plan) : PlanViewHelperLimited(context, plan) {
    override fun getPlanLimit(): String? {
        return mContext.getString(R.string.get_plan_flat_rate_time_limited,
                mPlan.startHour().toUpperCase(), mPlan.finishHour().toUpperCase())
    }

    override fun getPlanLimitAprox(): String? {
        return null
    }

    override fun getFlatRate(): String? {
        return mContext.getString(R.string.plan_view_flat_time_limited,
                mPlan.finishHour().toUpperCase(), mPlan.startHour().toUpperCase())
    }
}

class PlanViewHelperUnlimited(context: Context, plan: Plan) : PlanViewHelperNonNull(context, plan) {
    override fun getPlanLimit(): String? {
        return mContext.getString(R.string.plan_view_unlimited)
    }

    override fun getPlanLimitAprox(): String? {
        return null
    }

    override fun getFlatRate(): String? {
        return null
    }

    override fun getDCFastPrice(): String? {
        return null
    }

    override fun getLevel2Price(): String? {
        return null
    }
}