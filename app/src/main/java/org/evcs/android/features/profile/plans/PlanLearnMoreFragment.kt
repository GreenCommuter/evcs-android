package org.evcs.android.features.profile.plans

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.base.core.presenter.BasePresenter
import com.base.core.util.NavigationUtils
import org.evcs.android.R
import org.evcs.android.databinding.FragmentPlanLearnMoreBinding
import org.evcs.android.model.Plan
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils.setParentVisibility

class PlanLearnMoreFragment : ErrorFragment<BasePresenter<*>>() {

    private var mPlan: Plan? = null
    private lateinit var mBinding: FragmentPlanLearnMoreBinding

    companion object {
        fun newInstance(plan: Plan?): PlanLearnMoreFragment {
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
        mPlan = arguments?.getSerializable(Extras.PlanActivity.PLAN) as Plan?
    }

    override fun populate() {
        if (mPlan == null) {
            showPayAsYouGo()
        } else {
            setPlan(mPlan!!)
        }
    }

    private fun showPayAsYouGo() {
        //TODO: all views
        mBinding.planLearnMoreToolbar.setTitle("Pay as you go")

        if (UserUtils.getLoggedUser() == null) {
            //TODO
        } else if (UserUtils.getLoggedUser().defaultPm == null) {
            setPaymentButton()
        } else {
            mBinding.planLearnMoreButton.isVisible = false
            mBinding.plansLearnMoreExplore.setParentVisibility(true)
        }
    }

    private fun setPaymentButton() {
        mBinding.planLearnMoreButton.text = "Add payment method"
        mBinding.planLearnMoreButton.setOnClickListener {
            //TODO
        }
    }

    private fun setGetPlanButton(plan: Plan) {
        mBinding.planLearnMoreButton.setOnClickListener {
            NavigationUtils.jumpTo(requireContext(), GetPlanActivity::class.java,
                NavigationUtils.IntentExtra(Extras.PlanActivity.PLAN, plan))
        }
    }

    fun setPlan(plan: Plan) {
        mBinding.planLearnMoreToolbar.setTitle(plan.name)
        //if plan and user both have free days
//        mBinding.planLearnMoreDaysFree.text = String.format("${mPlan.trialDays} Days Free!*", mPlan.trialDays)
        //if plan doesn't:
        //mBinding.planLearnMoreDaysFree.text = "Simple Flat Rate Member Pricing"
        //if user doesn't:
        //mBinding.planLearnMoreDaysFree.setVisibility(View.GONE)
        //show price in title.large
        mBinding.planLearnMorePrice.text = String.format("\$%.2f/%s", plan.price, plan.renewalPeriod)
        mBinding.planLearnMorePrice.setTextAppearance(requireContext(), R.style.Title_Large)

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

        if (/*mPlan.trialDays > 0*/ false) {
            //unlimited anytime / unlimited offpeak
            val unlimited = "Unlimited Anytime or"
            mBinding.planLearnMoreSidenote.text = String.format(
                "*%d days free not available for users who have already tried %s Standard Anytime",
                plan.trialDays, unlimited)
        } else {
            //Pay as you go, no payment: *Up to 40% off for member pricing on Level 2 and DC Fast compared to non-members
            mBinding.planLearnMoreSidenote.visibility = View.GONE
        }

        //standard anytime w/trial: "Get %d days free"
        //Basic anytime w/trial: "Get %d days free charging"
        mBinding.planLearnMoreButton.text = "Get Started"

        val currentPlanId = UserUtils.getLoggedUser().activeSubscription?.plan?.id
        if (currentPlanId == plan.id) {
            mBinding.planLearnMoreButton.isEnabled = false
            mBinding.planLearnMoreButton.text = "Current plan"
        }
        setGetPlanButton(plan)
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.plansLearnMoreExplore.setOnClickListener { NavigationUtils.jumpTo(requireContext(), PlansActivity::class.java) }
        mBinding.planLearnMoreToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
    }

}
