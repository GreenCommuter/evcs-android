package org.evcs.android.features.profile.plans

import android.os.Bundle
import android.view.View
import com.base.core.presenter.BasePresenter
import com.base.core.util.NavigationUtils
import org.evcs.android.R
import org.evcs.android.databinding.FragmentPlanLearnMoreBinding
import org.evcs.android.model.Plan
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils

class PlanLearnMoreFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mPlan: Plan
    private lateinit var mBinding: FragmentPlanLearnMoreBinding

    companion object {
        fun newInstance(plan: Plan): PlanLearnMoreFragment {
            val args = Bundle()
            args.putSerializable(Extras.PlanActivity.PLAN, plan)

            val fragment = PlanLearnMoreFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_plan_learn_more
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentPlanLearnMoreBinding.bind(v)
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun init() {
        mPlan = requireArguments().getSerializable(Extras.PlanActivity.PLAN) as Plan
    }

    override fun populate() {
        super.populate()
        mBinding.planLearnMoreToolbar.setTitle(mPlan.name)
        //if plan and user both have free days
        mBinding.planLearnMoreDaysFree.text = String.format("${mPlan.trialDays} Days Free!*", mPlan.trialDays)
        //if plan doesn't:
        //mBinding.planLearnMoreDaysFree.text = "Simple Flat Rate Member Pricing"
        //if user doesn't:
        //mBinding.planLearnMoreDaysFree.setVisibility(View.GONE)
        //show price in title.large
        mBinding.planLearnMorePrice.text = String.format("\$%.2f/%s", mPlan.price, mPlan.renewalPeriod)

        //for basic anytime and standard anytime
        //unlimited anytime: Unlimited fast charging, no kWh caps
        //unlimited oofpeak: Unlimited DC fast charging from 10pm to 6am
        mBinding.planLearnMoreCap.text = "Up to %d kWh/%s"
        //for basic anytime and standard anytime
        //unlimited offpeak: Flat rate of \$%.2f/kWh on DC Fast and \$%.2f on Level 2 (40% discount) from 6am to 10pm
        mBinding.planLearnMoreFlatRate.text = "Flat rate of \$%.2f/kWh on DC fast charging and \$%.2f/kWh on Level 2 after cap is exceeded"
        //en el medio en unlimited offpeak: Unlimited fast charging, no kWh caps

        //24/7: visible en standard anytime, basic anytime, unlimited aytime
        //en unlimited offpeak: Unlimited fast charging, no kWh caps

        //hikes: visible en standard anytime, basic anytime, pay as you go no payment


        if (mPlan.trialDays > 0) {
            //unlimited anytime / unlimited offpeak
            val unlimited = "Unlimited Anytime or"
            mBinding.planLearnMoreSidenote.text = String.format(
                "*%d days free not available for users who have already tried %s Standard Anytime",
                mPlan.trialDays, unlimited)
        } else {
            //Pay as you go, no payment: *Up to 40% off for member pricing on Level 2 and DC Fast compared to non-members
            mBinding.planLearnMoreSidenote.visibility = View.GONE
        }

        //standard anytime w/trial: "Get %d days free"
        //Basic anytime w/trial: "Get %d days free charging"
        //Pay as you go, no payment: "Add payment method"
        //Pay as you go, else: hide and show planslearnmoreexplore
        mBinding.planLearnMoreButton.text = "Get Started"

        val currentPlanId = UserUtils.getLoggedUser().activeSubscription?.plan?.id
        //TODO: PAYG case (no plan)
        if (currentPlanId == mPlan.id) {
            mBinding.planLearnMoreButton.isEnabled = false
            mBinding.planLearnMoreButton.text = "Current plan"
        }

    }

    override fun setListeners() {
        super.setListeners()
        //salvo que sea add payment method
        mBinding.planLearnMoreButton.setOnClickListener {
            NavigationUtils.jumpTo(requireContext(), GetPlanActivity::class.java,
                NavigationUtils.IntentExtra(Extras.PlanActivity.PLAN, mPlan))
        }
        mBinding.plansLearnMoreExplore.setOnClickListener { NavigationUtils.jumpTo(requireContext(), PlansActivity::class.java) }
        mBinding.planLearnMoreToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
    }

}
