package org.evcs.android.features.charging

import android.content.Intent
import android.net.Uri
import android.view.View
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentPlanInfoBinding
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.Station
import org.evcs.android.model.SubscriptionStatus
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.joda.time.format.DateTimeFormat

class PlanInfoFragment : ErrorFragment<PlanInfoPresenter>(), PlanInfoView,
    ChangePaymentMethodFragment.PaymentMethodChangeListener {

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
        showProgressDialog()
        presenter?.populate(requireArguments().getString(Extras.PlanInfo.STATION_ID)!!)
    }

    override fun setListeners() {
        mBinding.planInfoCreditCardChange.setOnClickListener {
            mListener.setPaymentMethodChangeListener(this)
            mListener.goToChangePaymentMethods(presenter?.mPaymentMethods)
        }
        mBinding.planInfoApplyCoupon.setOnClickListener {  }
        mBinding.planInfoChargeWithPayg.setOnClickListener {  }
    }

    override fun show(station: Station, status: SubscriptionStatus?) {
        hideProgressDialog()
        mListener.setActiveSession(true)
        mBinding.planInfoSubscriptionName.visibility = View.VISIBLE
        mBinding.planInfoSubscriptionName.setText(status?.planName ?: getString(R.string.plan_info_pay_as_you_go))
//        val kWhUsed = status

        if (status?.renewalDate != null) {
            mBinding.planInfoRenewalDate.visibility = View.VISIBLE
            val dateTimeFormatter = DateTimeFormat.forPattern(getString(R.string.plan_info_date_pattern))
            mBinding.planInfoRenewalDate.setText(dateTimeFormatter.print(status.renewalDate))
        }

        if (status?.kwhUsage != null) {
            mBinding.planInfoKwhUsage.visibility = View.VISIBLE
            val text = String.format("%d/%s kWh", status.kwhUsage, status.printTotalKwh())
            mBinding.planInfoKwhUsage.setText(text)
        }

        mBinding.planInfoChargeRate.visibility = View.VISIBLE
        val pricing = station.pricing!!.detail
        if (pricing.priceKwh != null || pricing.thereafterPrice != null) {
            mBinding.planInfoChargeRate.setText(
                    String.format("$%.2f", pricing.priceKwh ?: pricing.thereafterPrice))
        }
        if (status == null) {
            showPlanDialog(getString(R.string.plan_info_upgrade_plan_dialog), false, null)
            showPaymentInfo()
            return
        }
        setUpButton()
        if (status.remainingKwh != null && status.remainingKwh <= 0) {
            val resetDate = DateTimeFormat.forPattern(getString(R.string.plan_info_date_pattern))
                    .print(status.nextRemainingKwhRestoration)
            val text = String.format(getString(R.string.plan_info_exceeded),
                    status.totalKwh, resetDate, pricing.thereafterPrice)
            showPlanDialog(text, true, status.accountUrl)
            showPaymentInfo()
        }
        //TODO: use actual values
        if (!status.planCoversTime) {
            val text = String.format(getString(R.string.plan_info_peak_hours), pricing.priceKwh)

            showPlanDialog(text, true, status.accountUrl)
            showPaymentInfo()
        }
        if (status.isSuspended) {
            showIssue(status.issueMessage)
            mBinding.planInfoButton.text = "Update payment method"
            mBinding.planInfoButton.setOnClickListener { launchUrl(status.accountUrl) }
        }
        else if (status.issue) {
            showIssue(status.issueMessage)
        }
    }

    private fun showIssue(issueMessage: String) {
        (mBinding.planInfoAlertMessage.parent as View).visibility = View.VISIBLE
        mBinding.planInfoAlertMessage.text = issueMessage
        mBinding.planInfoChargeWithPayg.visibility = View.VISIBLE
    }

    private fun setUpButton() {
        mBinding.planInfoButton.isEnabled = true
        mBinding.planInfoButton.setOnClickListener { goToStartCharging() }
    }

    private fun showPlanDialog(message: String, isUpgradePlan: Boolean, accountUrl: String?) {
        (mBinding.planInfoExplorePlans.parent as View).visibility = View.VISIBLE
        mBinding.planInfoExplorePlans.text = message
        if (isUpgradePlan) {
            mBinding.planInfoExplorePlansButton.text = getString(R.string.plan_info_upgrade_plan)
            mBinding.planInfoExplorePlansButton.setOnClickListener {
                launchUrl(accountUrl)
            }
        }
    }

    private fun launchUrl(url: String?) {
        val i = Intent(Intent.ACTION_VIEW);
        i.data = Uri.parse(url);
        startActivity(i);
    }

    override fun showDefaultPM(paymentMethod: PaymentMethod) {
        onPaymentMethodChanged(paymentMethod)
    }

    private fun showPaymentInfo() {
        mBinding.planInfoChargeRate.setLabel(getString(R.string.plan_info_discounted_charge_rate))
        (mBinding.planInfoCreditCard.parent as View).visibility = View.VISIBLE
        mBinding.planInfoCouponCode.visibility = View.VISIBLE

        mBinding.planInfoButton.isEnabled = true
        mBinding.planInfoButton.setOnClickListener {
            goToStartCharging()
        }

        if (mSelectedPM != null)
            mBinding.planInfoCreditCard.text =
                mSelectedPM!!.card.brand.toPrintableString() + " ending in " + mSelectedPM!!.card.last4
    }

    override fun showFree(freeChargingCode: String) {
        hideProgressDialog()
        mListener.setActiveSession(true)
        mBinding.planInfoFreeCharging.visibility = View.VISIBLE
        //TODO: mostrar prompt de freeChargingCode
        mBinding.planInfoButton.isEnabled = true
        setUpButton()
    }

    override fun onBackPressed(): Boolean {
        mListener.cancelSession(childFragmentManager)
        return true
    }

    override fun onPaymentMethodChanged(paymentMethod: PaymentMethod) {
        mSelectedPM = paymentMethod
    }

    override fun getProgressDialogLayout(): Int {
        return R.layout.spinner_layout_black
    }

    private fun goToStartCharging() {
        mListener.goToStartCharging(presenter.getStationId(), mSelectedPM?.id, null)
    }

}