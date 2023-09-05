package org.evcs.android.features.charging

import android.graphics.Color
import android.view.View
import androidx.navigation.fragment.findNavController
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.activity.ChargingActivity
import org.evcs.android.databinding.FragmentFreeTrialReminderBinding
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.FontUtils
import org.evcs.android.util.UserUtils

class FreeTrialReminderFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mBinding: FragmentFreeTrialReminderBinding

    override fun layout(): Int {
        return R.layout.fragment_free_trial_reminder
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentFreeTrialReminderBinding.bind(v)
    }

    override fun init() {}

    override fun populate() {
        super.populate()
        val sub = UserUtils.getLoggedUser().activeSubscription!!
        val text1 = arrayOf(
            "You have",
            String.format("%.0f of %dkWh", sub.remainingKwh, sub.plan.trialKwh),
            "remaining in your Free Trial"
        )
        val text2 = arrayOf(
            String.format("If this charging session exceeds %.0f kWh, you will be", sub.remainingKwh),
            "automatically billed",
            String.format("for the first [monthly] installment of $%.2f for your %s Plan.", sub.plan.price, sub.planName)
        )

        mBinding.freeTrialReminderText1.text = FontUtils.getSpannable(text1, Color.BLACK)
        mBinding.freeTrialReminderText2.text = FontUtils.getSpannable(text2, Color.BLACK)
    }

    override fun onBackPressed(): Boolean {
        (activity as ChargingActivity).cancelSession(childFragmentManager)
        return true
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.freeTrialReminderButton.setOnClickListener { findNavController().popBackStack() }
    }
}