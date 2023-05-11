package org.evcs.android.features.profile.plans

import android.text.method.LinkMovementMethod
import android.view.View
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentGetPlanBinding
import org.evcs.android.model.Plan
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

abstract class AbstractGetPlanFragment : ErrorFragment<GetPlanPresenter>(), GetPlanView {

    protected lateinit var mBinding: FragmentGetPlanBinding
    protected lateinit var mPlan: Plan

    override fun layout(): Int {
        return R.layout.fragment_get_plan
    }

    override fun createPresenter(): GetPlanPresenter {
        return GetPlanPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentGetPlanBinding.bind(v)
    }

    override fun init() {
        mPlan = requireArguments().getSerializable(Extras.PlanActivity.PLAN) as Plan
    }

    override fun populate() {
        val dateFormatter = DateTimeFormat.forPattern("m/d/yyyy")
        mBinding.getPlanToolbar.setTitle(mPlan.name)
        mBinding.getPlanPlan.text = mPlan.name
        mBinding.getPlanFreeTrial.visibility = if (showFreeTrial()) View.VISIBLE else View.GONE
        mBinding.getPlanFreeTrial.setLabel("%d Day Offer - New Members Only")

        //TODO: el payg no tiene monthly rate y esconte el check, muestra el start date
        mBinding.getPlanMonthlyRate.setLabel("Monthly Rate - Starting %s")
        mBinding.getPlanMonthlyRate.setText(String.format("\$%.2f per month", mPlan.monthlyPrice))
        mBinding.getPlanFlatRate.text = "Flat rate for Level 2 and DC fast \$%.2f/kWh after %d kWh exceeded"

        //Todo: puede ser weekly
        mBinding.getPlanMonthlyCostTitle.text = "Monthly Cost (Starting %s)"
        mBinding.getPlanSubtotal.text = String.format("\$%.2f", mPlan.monthlyPrice)
        mBinding.bottomNavigationButton.text = getButtonText()
        mBinding.getPlanTandc.text =
                String.format(getString(R.string.get_plan_tandc), getButtonText())
        mBinding.getPlanTandc.movementMethod = LinkMovementMethod.getInstance()
        mBinding.getPlanTodayLayout.visibility = if (showToday()) View.VISIBLE else View.GONE
        mBinding.getPlanPaymentCouponCode.visibility = if (showCouponCode()) View.VISIBLE else View.GONE
        mBinding.getPlanPreviousPlanActiveUntil.visibility = if (getActiveUntil() == null) View.VISIBLE else View.GONE
        mBinding.getPlanPreviousPlanActiveUntil.setText(dateFormatter.print(getActiveUntil()))
    }

    abstract fun getActiveUntil(): DateTime?

    abstract fun showCouponCode(): Boolean

    abstract fun showFreeTrial(): Boolean

    abstract fun getButtonText(): String

    abstract fun showToday(): Boolean

    override fun setListeners() {
        super.setListeners()
        mBinding.getPlanToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        mBinding.bottomNavigationButton.setOnClickListener {
            showProgressDialog()
            presenter.subscribe(mPlan, "")
        }
        mBinding.getPlanPaymentInfo.setOnChangeClickListener {
            //TODO: go to change
        }
    }

    override fun onSubscriptionSuccess(response: Void) {
        ToastUtils.show("Subscription success")
    }
}