package org.evcs.android.ui.view.profile

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import org.evcs.android.R
import org.evcs.android.databinding.PlanProgressViewBinding
import org.evcs.android.model.SubscriptionStatus

class PlanProgressView : LinearLayout {
    private lateinit var mBinding: PlanProgressViewBinding

    constructor(context: Context?) : super(context) { init(context) }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(context) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun init(context: Context?) {
        mBinding = PlanProgressViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setPlan(status: SubscriptionStatus, showText: Boolean = true) {

        mBinding.activitySubscriptionsConsumption.text =
                context.getString(R.string.progress_view_text,
                        status.kwhUsage, status.printTotalKwh(), status.renewalPeriod)
        if (status.onTrialPeriod) {
            mBinding.activitySubscriptionsConsumption.text =
                context.getString(R.string.progress_view_text_trial,
                    status.kwhUsage, status.totalKwh)
        }
        mBinding.activitySubscriptionsConsumption.isVisible = showText
        if (status.isUnlimited) {
            mBinding.activitySubscriptionsProgress.isVisible = false
        } else {
            if (status.kwhUsage == null) return
            mBinding.activitySubscriptionsProgress.progress = status.kwhUsage.toInt()
            mBinding.activitySubscriptionsProgress.max = status.totalKwh
            if (status.kwhUsage >= status.totalKwh) {
                mBinding.activitySubscriptionsProgress.progressTintList =
                        ColorStateList.valueOf(context.resources.getColor(R.color.evcs_danger_700))
            }
        }
    }
}