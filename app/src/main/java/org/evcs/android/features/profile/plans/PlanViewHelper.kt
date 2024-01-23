package org.evcs.android.features.profile.plans

import android.content.Context
import org.evcs.android.R
import org.evcs.android.model.Plan
import org.evcs.android.model.user.User

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
    abstract fun getPlanButton(user: User?) : String?
    abstract fun getPlanFreq() : String?
    abstract fun getPlanLimit() : String?
    abstract fun getPlanLimitAprox() : String?
    abstract fun getFlatRate(): String?
    abstract fun showLearnMore(): Boolean

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

    override fun getPlanButton(user: User?): String? {
        return if (user != null) null
               else mContext.getString(R.string.pay_as_you_go_button)
    }

    override fun getPlanFreq(): String {
        return mContext.getString(R.string.pay_as_you_go_freq)
    }

    override fun getPlanLimit(): String? {
        return null
    }

    override fun getPlanLimitAprox(): String? {
        return null
    }

    override fun getFlatRate(): String? {
        return mContext.getString(R.string.pay_as_you_go_flat)
    }

    override fun getFlatRateForGetPlan(): String? {
        return null
    }

    override fun getCongratulationsDialogSubtitle(): String? {
        return null
    }

    override fun showLearnMore(): Boolean {
        return false
    }

}

abstract class PlanViewHelperNonNull(context: Context, val mPlan: Plan) : PlanViewHelper(context) {
    override fun getPlanName(): String {
        return mPlan.name
    }

    override fun getPlanPrice(): String {
        return mPlan.priceTitle
    }

    override fun getPlanLimit(): String? {
        return mPlan.displaySubtitle
    }

    override fun getPlanLimitAprox(): String? {
        return mPlan.displayUnderSubtitle
    }

    override fun getFlatRate(): String? {
        if (mPlan.displayDetails.isEmpty()) return null
        return mPlan.displayDetails.joinToString("\n")
    }

    override fun getPlanButton(user: User?): String {
        val stringRes = if (user == null || user.canDoTrial()) R.string.app_trial_cta_default else R.string.pay_as_you_go_button
        return mContext.getString(stringRes)
    }

    override fun getPlanFreq(): String? {
        return mPlan.displayTopText
    }

    override fun showLearnMore(): Boolean {
        return true
    }
}

abstract class PlanViewHelperLimited(context: Context, plan: Plan) : PlanViewHelperNonNull(context, plan) {}

class PlanViewHelperCapped(context: Context, plan: Plan) : PlanViewHelperLimited(context, plan) {
    override fun getFlatRateForGetPlan(): String? {
        return mContext.getString(R.string.get_plan_flat_rate, mPlan.kwhCap(), mPlan.renewalPeriod.toString(), mPlan.pricePerKwh)
    }

    override fun getCongratulationsDialogSubtitle(): String {
        return mContext.getString(R.string.congratulations_dialog_subtitle_1)
    }
}

class PlanViewHelperTimeLimited(context: Context, plan: Plan) : PlanViewHelperLimited(context, plan) {
    override fun getFlatRateForGetPlan(): String? {
        return mContext.getString(R.string.get_plan_flat_rate_time_limited,
                mPlan.startHour().toUpperCase(), mPlan.finishHour().toUpperCase(), mPlan.pricePerKwh)
    }

    override fun getCongratulationsDialogSubtitle(): String {
        return String.format(
                mContext.getString(R.string.congratulations_dialog_subtitle_2),
                mPlan.startHour().toUpperCase(), mPlan.finishHour().toUpperCase())
    }
}

class PlanViewHelperUnlimited(context: Context, plan: Plan) : PlanViewHelperNonNull(context, plan) {
    override fun getFlatRateForGetPlan(): String? {
        return mContext.getString(R.string.get_plan_flat_rate_unlimited)
    }

    override fun getCongratulationsDialogSubtitle(): String {
        return mContext.getString(R.string.congratulations_dialog_subtitle_3)
    }
}