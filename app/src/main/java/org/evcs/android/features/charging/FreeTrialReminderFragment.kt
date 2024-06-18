package org.evcs.android.features.charging

import android.graphics.Color
import android.text.TextUtils
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.activity.ChargingActivity
import org.evcs.android.util.FontUtils
import org.evcs.android.util.UserUtils

class FreeTrialReminderFragment : WarningFragment<BasePresenter<*>>() {

    override fun onBackPressed(): Boolean {
        (activity as ChargingActivity).cancelSession(childFragmentManager)
        return true
    }

    override fun getTitle(): CharSequence {
        return getString(R.string.free_trial_reminder_title)
    }

    override fun getDescription(): CharSequence {
        val sub = UserUtils.getLoggedUser().activeSubscription!!
        val text1 = arrayOf(
            getString(R.string.free_trial_reminder_description_1),
            getString(R.string.free_trial_reminder_description_2, sub.remainingKwh, sub.plan.trialKwh),
            getString(R.string.free_trial_reminder_description_3)
        )
        val text2 = arrayOf(
            getString(R.string.free_trial_reminder_description_4, sub.remainingKwh),
            getString(R.string.free_trial_reminder_description_5),
            getString(R.string.free_trial_reminder_description_6, sub.plan.renewalPeriod.toAdverb(), sub.plan.price, sub.planName)
        )

        return TextUtils.concat(FontUtils.getSpannable(text1, Color.BLACK), "\n\n",
            FontUtils.getSpannable(text2, Color.BLACK))
    }
}