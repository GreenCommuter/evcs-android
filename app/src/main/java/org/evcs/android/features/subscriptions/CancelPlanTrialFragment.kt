package org.evcs.android.features.subscriptions

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import org.evcs.android.R
import org.evcs.android.databinding.FragmentCancelPlanTrialBinding
import org.evcs.android.model.Subscription
import org.evcs.android.navigation.controller.AbstractNavigationController
import org.evcs.android.util.UserUtils

class CancelPlanTrialFragment : AbstractCancelPlanFragment() {

//    private lateinit var mBinding: FragmentCancelPlanTrialBinding

    override fun getConfirmationText(): String {
        return getString(R.string.cancel_plan_trial_confirmation)
    }

    override fun onContinueClicked() {
        goToCancelPlan()
    }

    fun goToCancelPlan(navoptions: NavOptions? = null) {
        findNavController().navigate(CancelPlanTrialFragmentDirections.actionCancelPlanTrialFragmentToCancelPlanFragment(),
                navoptions)
    }

    override fun setPlan(subscription: Subscription) {
        if (!subscription.onTrialPeriod) {
            goToCancelPlan(AbstractNavigationController.replaceLastNavOptions(findNavController()))
        }
    }

    override fun getCancelText(): String {
        return getString(R.string.cancel_plan_trial_cancel)
    }

    override fun getChildLayout(parent: FrameLayout): View {
        val binding = FragmentCancelPlanTrialBinding.inflate(LayoutInflater.from(requireContext()))//, parent, true)
        return binding.root
    }

    override fun getBoldText(): String {
        val subscription = UserUtils.getLoggedUser().activeSubscription
        return getString(R.string.cancel_plan_trial_bold)
    }

}
