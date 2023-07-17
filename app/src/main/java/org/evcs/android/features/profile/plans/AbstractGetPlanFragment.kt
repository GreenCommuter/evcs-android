package org.evcs.android.features.profile.plans

import android.content.Context
import android.content.Intent
import android.text.*
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentGetPlanBinding
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.Plan
import org.evcs.android.model.SubscriptionStatus
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.StringUtils
import org.evcs.android.util.ViewUtils.setVisibility
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

abstract class AbstractGetPlanFragment : ErrorFragment<GetPlanPresenter>(), GetPlanView {

    private lateinit var mLauncher: ActivityResultLauncher<Intent>
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
        mLauncher = WalletActivity.getDefaultLauncher(this) { pm ->
            mBinding.getPlanPaymentInfo.setPaymentMethod(pm)
            mBinding.bottomNavigationButton.isEnabled = true
        }
    }

    override fun populate() {
        val dateFormatter = DateTimeFormat.forPattern("M/d/yyyy")
        mBinding.getPlanToolbar.setTitle(getToolbarTitle())
        mBinding.getPlanPlan.text = mPlan.name
        mBinding.getPlanFreeTrial.setVisibility(getTrialLabel() != null)
        mBinding.getPlanFreeTrial.setLabel(getTrialLabel() ?: "")

        //TODO: el payg no tiene monthly rate y esconde el check, muestra el start date
        mBinding.getPlanMonthlyRate.setLabel(getMonthlyLabel(dateFormatter))
        mBinding.getPlanMonthlyRate.setText(String.format("\$%.2f per %s", mPlan.price, mPlan.renewalPeriod))
        mBinding.getPlanFlatRate.text = PlanViewHelper.instance(requireContext(), mPlan).getFlatRateForGetPlan()
        mBinding.getPlanCostLayout.setVisibility(showCostLayout())
        val period = StringUtils.capitalize(mPlan.renewalPeriod.toAdverb())
        val startingDate = dateFormatter.print(mPlan.startingDate())
        mBinding.getPlanMonthlyCostTitle.text = getString(R.string.get_plan_price, period, startingDate)
        mBinding.getPlanSubtotal.text = getString(R.string.app_price_format, mPlan.price)
        //TODO: tax
        mBinding.getPlanTotal.text = getString(R.string.app_price_format, mPlan.price)
        mBinding.bottomNavigationButton.text = getButtonText()

        mBinding.getPlanTandc.text = getTandCText()
        mBinding.getPlanTandc.setVisibility(getTandCText() != null)
        mBinding.getPlanTandc.movementMethod = LinkMovementMethod.getInstance()
        mBinding.getPlanTodayLayout.setVisibility(showToday())
        val pm = PaymentMethod.getDefaultFromSharedPrefs()
        mBinding.bottomNavigationButton.isEnabled = pm != null
        mBinding.getPlanPaymentInfo.setPaymentMethod(pm)
        mBinding.getPlanPaymentCouponCode.setVisibility(showCouponCode())
        mBinding.getPlanPreviousPlanActiveUntil.setVisibility(getActiveUntil() != null)
        mBinding.getPlanPreviousPlanActiveUntil.setText(dateFormatter.print(getActiveUntil()))
        mBinding.getPlanStarted.setVisibility(showPlanStarted())
        mBinding.getPlanNextBillingDate.setVisibility(getNextBillingDate() != null)
        mBinding.getPlanNextBillingDate.setText(dateFormatter.print(getNextBillingDate()))
    }

    protected open fun getTandCText(): CharSequence? {
        return requireContext().getText(R.string.get_plan_tandc, getButtonText())
    }

    // Needed to have both placeholders and html links
    fun Context.getText(@StringRes id: Int, vararg args: Any): CharSequence {
        val escapedArgs = args.map {
            if (it is String) TextUtils.htmlEncode(it) else it
        }.toTypedArray()
        return Html.fromHtml(String.format(Html.toHtml(SpannedString(getText(id))), *escapedArgs))
    }

    protected open fun getMonthlyLabel(dateFormatter: DateTimeFormatter): String {
        return getString(R.string.get_plan_monthly_label, dateFormatter.print(mPlan.startingDate()))
    }

    protected open fun getNextBillingDate(): DateTime? {
        return null
    }

    protected open fun showPlanStarted(): Boolean {
        return false
    }

    protected open fun showCostLayout(): Boolean {
        return true
    }

    protected open fun getToolbarTitle(): String {
        return mPlan.name
    }

    abstract fun getActiveUntil(): DateTime?

    abstract fun showCouponCode(): Boolean

    abstract fun getTrialLabel(): String?

    abstract fun getButtonText(): String

    abstract fun showToday(): Boolean

    override fun setListeners() {
        super.setListeners()
        mBinding.bottomNavigationButton.setOnClickListener { getBottomNavigationListener() }
        mBinding.getPlanPaymentInfo.setOnChangeClickListener {
            val intent = WalletActivity.buildIntent(context, true)
            mLauncher.launch(intent)
        }
        mBinding.getPlanPaymentCouponCode.setListener {
            //TODO: apply
        }
    }

    protected open fun getBottomNavigationListener() {
        showProgressDialog()
        presenter.subscribe(mPlan, PaymentMethod.getDefaultFromSharedPrefs()!!.id!!)
    }

    override fun onSubscriptionSuccess(response: SubscriptionStatus) {
//        ToastUtils.show("Subscription success")
        (requireActivity() as GetPlanActivity).onPlanSubscribed(response)
    }
}