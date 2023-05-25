package org.evcs.android.features.subscriptions

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import org.evcs.android.R
import org.evcs.android.databinding.FragmentCancelPlanTrialBinding
import org.evcs.android.navigation.controller.AbstractNavigationController

class CancelPlanTrialFragment : AbstractCancelPlanFragment() {

//    private lateinit var mBinding: FragmentCancelPlanTrialBinding

    override fun init() {
        super.init()
        //TODO: not on trial
        if (true) {
            goToCancelPlan(AbstractNavigationController.replaceLastNavOptions(findNavController()))
        }
    }

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

    override fun getCancelText(): String {
        return getString(R.string.cancel_plan_trial_cancel)
    }

    override fun getChildLayout(parent: FrameLayout): View {
        val binding = FragmentCancelPlanTrialBinding.inflate(LayoutInflater.from(requireContext()))//, parent, true)
        return binding.root
    }

    override fun getBoldText(): String {
        return String.format(getString(R.string.cancel_plan_trial_bold), 15)
    }

}
