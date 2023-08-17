package org.evcs.android.features.profile.plans

import android.content.Context
import org.evcs.android.R
import org.evcs.android.model.Plan

abstract class PlanLearnMoreHelper(val mContext: Context) {
    companion object {
        fun instance(context: Context, plan: Plan?, hasPaymentMethod: Boolean): PlanLearnMoreHelper {
            if (plan == null)
                return PlanLearnMoreHelperPAYG(context, hasPaymentMethod)
            if (!plan.isUnlimited)
                return PlanLearnMoreHelperCapped(context, plan)
            if (plan.isTimeLimited)
                return PlanLearnMoreHelperTimeLimited(context, plan)
            return PlanLearnMoreHelperUnlimited(context, plan)
        }
    }
    abstract fun getPlanName() : String
    abstract fun getPlanPrice() : String
    abstract fun getPlanFreeDays(): String
    abstract fun getPlanButton() : String
    abstract fun getPlanLimit() : String?
    abstract fun getFlatRate(): String?
    abstract fun getPlanTimes(): String?
    abstract fun getPlanHikes(): String?
    abstract fun getPlanSideNote(): String?
}

class PlanLearnMoreHelperPAYG(context: Context, val hasPaymentMethod: Boolean) : PlanLearnMoreHelper(context) {
    override fun getPlanName(): String {
        return mContext.getString(R.string.pay_as_you_go_name)
    }

    override fun getPlanFreeDays(): String {
        return "Simple Flat Rate Member Pricing"
    }

    override fun getPlanPrice(): String {
        return "Level 2: $%1\$s/kWH\nDC Fast: $%2\$s/kWh"
    }

    override fun getPlanButton(): String {
        return "Add payment Method"
    }

    override fun getPlanLimit(): String? {
        return null
    }

    override fun getFlatRate(): String? {
        return null
    }

    override fun getPlanTimes(): String? {
        return null
    }

    override fun getPlanHikes(): String? {
        return if (hasPaymentMethod) null else "No time-of-day price hikes on charging over 200 kWh"
    }

    override fun getPlanSideNote(): String? {
        return if (hasPaymentMethod) null else "*Up to 40% off for member pricing on Level 2 and DC Fast compared to non-members"
    }

}

abstract class PlanLearnMoreHelperNonNull(context: Context, val mPlan: Plan) : PlanLearnMoreHelper(context) {
    override fun getPlanName(): String {
        return mPlan.name
    }

    override fun getPlanFreeDays(): String {
        return String.format("%d Days Free!*", mPlan.trialDays)
    }

    override fun getPlanPrice(): String {
        return String.format("\$%.2f/%s", mPlan.price, mPlan.renewalPeriod)
    }

    override fun getPlanButton(): String {
        return mContext.getString(R.string.app_trial_cta_default)
    }
}

class PlanLearnMoreHelperCapped(context: Context, plan: Plan) : PlanLearnMoreHelperNonNull(context, plan) {
    override fun getPlanLimit(): String? {
        return mContext.getString(R.string.plan_view_limit, mPlan.kwhCap(), mPlan.renewalPeriod)
    }

    override fun getFlatRate(): String? {
        return String.format("Flat rate of \$%1\$.2f/kWh on DC fast charging and \$%1\$.2f/kWh on Level 2 after cap is exceeded", mPlan.pricePerKwh)
    }

    override fun getPlanTimes(): String? {
        return "24/7 charging, no time-of-day restrictions"
    }

    override fun getPlanHikes(): String? {
        return "No time-of-day price hikes on charging over 200 kWh"
    }

    override fun getPlanSideNote(): String? {
        return if (mPlan.trialDays == 0) null else String.format(
            "*%d days free not available for users who have already tried Standard Anytime",
            mPlan.trialDays)
    }

}

abstract class PlanLearnMoreHelperNotCapped(context: Context, plan: Plan) : PlanLearnMoreHelperNonNull(context, plan) {
    override fun getPlanHikes(): String? {
        return null
    }

    override fun getPlanSideNote(): String? {
        return if (mPlan.trialDays == 0) null else String.format(
            "*%d days free not available for users who have already tried Unlimited Anytime or Standard Anytime",
            mPlan.trialDays)
    }
}

class PlanLearnMoreHelperTimeLimited(context: Context, plan: Plan) : PlanLearnMoreHelperNotCapped(context, plan) {
    override fun getPlanLimit(): String? {
        return String.format("Unlimited DC fast charging from %1\$s to %2\$s",
                mPlan.startHour(), mPlan.finishHour())
    }

    override fun getFlatRate(): String? {
        return String.format("Flat rate of $%1\$.2f/kWh on DC Fast and $%1\$.2f on Level 2 (40%% discount) from %2\$s to %3\$s",
            mPlan.pricePerKwh, mPlan.finishHour(), mPlan.startHour())
    }

    override fun getPlanTimes(): String? {
        return "Unlimited fast charging, no kWh caps"
    }
}

class PlanLearnMoreHelperUnlimited(context: Context, plan: Plan) : PlanLearnMoreHelperNotCapped(context, plan) {
    override fun getPlanLimit(): String? {
        return "Unlimited fast charging, no kWh caps"
    }

    override fun getFlatRate(): String? {
        return null
    }

    override fun getPlanTimes(): String? {
        return "24/7 charging, no time-of-day restrictions"
    }

}