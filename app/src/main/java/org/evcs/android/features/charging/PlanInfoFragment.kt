package org.evcs.android.features.charging

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.navigation.fragment.findNavController
import com.base.core.util.ToastUtils
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentPlanInfoBinding
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.Station
import org.evcs.android.model.SubscriptionStatus
import org.joda.time.format.DateTimeFormat

class PlanInfoFragment : ErrorFragment<PlanInfoPresenter>(), PlanInfoView,
    ChangePaymentMethodFragment.PaymentMethodChangeListener {

    private var mDialogOnBack: Boolean = false
    private var mSelectedPM: PaymentMethod? = null
    private lateinit var mBinding: FragmentPlanInfoBinding

    private val mListener = ChargingNavigationController.getInstance()

    override fun layout(): Int {
        return R.layout.fragment_plan_info
    }

    override fun createPresenter(): PlanInfoPresenter {
        return PlanInfoPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentPlanInfoBinding.bind(v)
    }

    override fun init() {}

    override fun populate() {
        presenter?.populate(requireArguments().getInt("station_id"))
    }

    override fun setListeners() {
        mBinding.planInfoCreditCardChange.setOnClickListener {
            mListener.setPaymentMethodChangeListener(this)
            mListener.goToChangePaymentMethods(presenter?.mPaymentMethods)
        }
        mBinding.planInfoApplyCoupon.setOnClickListener {  }
    }

    override fun show(station: Station, status: SubscriptionStatus?) {
        mDialogOnBack = true
        mBinding.planInfoSubscriptionName.visibility = View.VISIBLE
        mBinding.planInfoSubscriptionName.setText(status?.planName ?: "Pay as you go")
//        val kWhUsed = status

        if (status?.renewalDate != null) {
            mBinding.planInfoRenewalDate.visibility = View.VISIBLE
            val dateTimeFormatter = DateTimeFormat.forPattern("d/M/yyyy")
            mBinding.planInfoRenewalDate.setText(dateTimeFormatter.print(status.renewalDate))
        }

        if (status?.kwhUsage != null) {
            mBinding.planInfoKwhUsage.visibility = View.VISIBLE
            val text = String.format("%d/%s kWh", status.kwhUsage, status.printTotalKwh())
            mBinding.planInfoKwhUsage.setText(text)
        }

        mBinding.planInfoChargeRate.visibility = View.VISIBLE
        val pricing = station.pricing.detail
        if (pricing.priceKwh != null || pricing.thereafterPrice != null) {
            mBinding.planInfoChargeRate.setText(
                    String.format("$%.2f", pricing.priceKwh ?: pricing.thereafterPrice))
        }
        //TODO: extract strings and use actual values
        if (status == null) {
            showPlanDialog("Get 30 days free when you upgrade to the Standard Anytime plan!", false, null)
            showPaymentInfo()
            return
        }
        if (status.remainingKwh != null && status.remainingKwh <= 0) {
            val resetDate = DateTimeFormat.forPattern("d/M/yyyy").print(status.nextRemainingKwhRestoration)
            val text = String.format("You have exceeded your %d kWh monthly cap. On %s your usage will reset. You will be billed a flat rate of \$%.2f/kWh until then",
                    status.totalKwh, resetDate, pricing.thereafterPrice)
            showPlanDialog(text, true, status.accountUrl)
            showPaymentInfo()
        }
        if (!status.planCoversTime) {
            val text = String.format("It is during peak hours (6am - 10pm) \n" +
                    "You’ll be charged a flat rate of \$$.2f/kWh \n" +
                    "Charge anytime from 10pm - 6am at no cost", pricing.priceKwh)

            showPlanDialog(text, true, status.accountUrl)
            showPaymentInfo()
        }
        setUpButton()
        if (status.isSuspended) {
            showIssue(status.issueMessage)
            //TODO
        }
        if (status.issue) {
            showIssue(status.issueMessage)
        }
    }

    private fun showIssue(issueMessage: String) {
        (mBinding.planInfoAlertMessage.parent as View).visibility = View.VISIBLE
        mBinding.planInfoAlertMessage.text = issueMessage
        mBinding.planInfoButton.isEnabled = false
    }

    private fun setUpButton() {
        mBinding.planInfoButton.isEnabled = true
        mBinding.planInfoButton.setOnClickListener { ToastUtils.show("Button press") }
    }

    private fun showPlanDialog(message: String, isUpgradePlan: Boolean, accountUrl: String?) {
        (mBinding.planInfoExplorePlans.parent as View).visibility = View.VISIBLE
        mBinding.planInfoExplorePlans.text = message
        if (isUpgradePlan) {
            mBinding.planInfoExplorePlansButton.text = "Upgrade plan"
            mBinding.planInfoExplorePlansButton.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW);
                i.data = Uri.parse(accountUrl);
                startActivity(i);
            }
        }
    }

    override fun showDefaultPM(paymentMethod: PaymentMethod) {
        onPaymentMethodChanged(paymentMethod)
    }

    private fun showPaymentInfo() {
        mBinding.planInfoChargeRate.setLabel("Discounted charge rate")
        (mBinding.planInfoCreditCard.parent as View).visibility = View.VISIBLE
        mBinding.planInfoCouponCode.visibility = View.VISIBLE

        mBinding.planInfoButton.isEnabled = true
        mBinding.planInfoButton.setOnClickListener { ToastUtils.show(mSelectedPM?.id ?: "null") }

        if (mSelectedPM != null)
            mBinding.planInfoCreditCard.text =
                mSelectedPM!!.card.provider.toPrintableString() + " ending in " + mSelectedPM!!.card.last4
    }

    override fun showFree(freeChargingCode: String) {
        mDialogOnBack = true
        mBinding.planInfoFreeCharging.visibility = View.VISIBLE
        //TODO: mostrar prompt de freeChargingCode
        mBinding.planInfoButton.isEnabled = true
        setUpButton()
    }

    override fun onBackPressed(): Boolean {
        if (!mDialogOnBack) return super.onBackPressed()
        EVCSDialogFragment.Builder()
                .setTitle("Cancel charging session?")
                .addButton(getString(R.string.app_yes)) { findNavController().popBackStack() }
                .showCancel(true)
                .show(childFragmentManager)
        return true
    }

    override fun onPaymentMethodChanged(paymentMethod: PaymentMethod) {
        mSelectedPM = paymentMethod
    }

}