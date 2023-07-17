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
    abstract fun getPlanFreq() : String?
    abstract fun getPlanLimit() : String?
    abstract fun getPlanLimitAprox() : String?
    abstract fun getFlatRate(): String?
    abstract fun getDCFastPrice(): String?
    abstract fun getLevel2Price(): String?

    abstract fun getFlatRateForGetPlan(): String?
    abstract fun getCongratulationsDialogSubtitle(): String?
}

class PlanViewHelperPAYG(context: Context) : PlanViewHelper(context) {
    override fun getPlanName(): String {
        return mContext.getString(R.string.pay_as_you_go_name)
    }

    override fun getPlanPrice(): String {
        return mContext.getString(R.string.pay_as_you_go_price)
    }

    override fun getPlanButton(): String {
        return mContext.getString(R.string.pay_as_you_go_button)
    }

    override fun getPlanFreq(): String {
        return mContext.getString(R.string.pay_as_you_go_freq)
    }

    override fun getPlanLimit(): String? {
        return mContext.getString(R.string.pay_as_you_go_limit)
    }

    override fun getPlanLimitAprox(): String? {
        return null
    }

    override fun getFlatRate(): String? {
        return mContext.getString(R.string.pay_as_you_go_flat)
    }

    override fun getDCFastPrice(): String? {
        return mContext.getString(R.string.pay_as_you_go_dc)
    }

    override fun getLevel2Price(): String? {
        return mContext.getString(R.string.pay_as_you_go_ac)
    }

    override fun getFlatRateForGetPlan(): String? {
        return null
    }

    override fun getCongratulationsDialogSubtitle(): String? {
        return null
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

    override fun getPlanFreq(): String? {
        return mPlan.useCase
    }
}

abstract class PlanViewHelperLimited(context: Context, plan: Plan) : PlanViewHelperNonNull(context, plan) {
    override fun getDCFastPrice(): String? {
        return mContext.getString(R.string.pay_as_you_go_dc, mPlan.pricePerKwh)
    }

    override fun getLevel2Price(): String? {
        return mContext.getString(R.string.pay_as_you_go_ac, mPlan.pricePerKwh)
    }
}

class PlanViewHelperCapped(context: Context, plan: Plan) : PlanViewHelperLimited(context, plan) {
    override fun getPlanLimit(): String? {
        return mContext.getString(R.string.plan_view_limit, mPlan.kwhCap(), mPlan.renewalPeriod)
    }

    override fun getPlanLimitAprox(): String? {
        return mContext.getString(R.string.plan_view_aprox, mPlan.milesCap())
    }

    override fun getFlatRate(): String? {
        return mContext.getString(R.string.plan_view_flat_rate, mPlan.kwhCap())
    }

    override fun getFlatRateForGetPlan(): String? {
        return mContext.getString(R.string.get_plan_flat_rate, mPlan.pricePerKwh, mPlan.kwhCap())
    }

    override fun getCongratulationsDialogSubtitle(): String {
        return mContext.getString(R.string.congratulations_dialog_subtitle_1)
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

    override fun getFlatRateForGetPlan(): String? {
        return mContext.getString(R.string.get_plan_flat_rate_time_limited,
                mPlan.startHour().toUpperCase(), mPlan.finishHour().toUpperCase())
    }

    override fun getCongratulationsDialogSubtitle(): String {
        return String.format(
                mContext.getString(R.string.congratulations_dialog_subtitle_2),
                mPlan.startHour().toUpperCase(), mPlan.finishHour().toUpperCase())
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

    override fun getFlatRateForGetPlan(): String? {
        return mContext.getString(R.string.get_plan_flat_rate_unlimited)
    }

    override fun getCongratulationsDialogSubtitle(): String {
        return mContext.getString(R.string.congratulations_dialog_subtitle_3)
    }
}