package org.evcs.android.features.charging

import android.content.Intent
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.ChargingActivity
import org.evcs.android.databinding.FragmentPlanInfoBinding
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.Station
import org.evcs.android.model.SubscriptionStatus
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.ViewUtils.setParentVisibility
import org.joda.time.format.DateTimeFormat

class PlanInfoFragment : ErrorFragment<PlanInfoPresenter>(), PlanInfoView {

    private lateinit var mWalletLauncher: ActivityResultLauncher<Intent>
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

    override fun init() {
        mWalletLauncher = WalletActivity.getDefaultLauncher(this) {
            pm -> mSelectedPM = pm
            mBinding.planInfoCreditCard.setPaymentMethod(pm)
        }
    }

    override fun populate() {
        showProgressDialog()
        presenter?.populate(requireArguments().getString(Extras.PlanInfo.STATION_ID)!!)
    }

    override fun setListeners() {
        mBinding.planInfoCreditCard.setOnChangeClickListener {
            mWalletLauncher.launch(WalletActivity.buildIntent(requireContext(), true))
        }
        mBinding.planInfoApplyCoupon.setOnClickListener {  }
        mBinding.planInfoChargeWithPayg.setOnClickListener {
            //TODO: check
            goToStartCharging()
        }
        mBinding.planInfoToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        mBinding.planInfoHelp.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun show(station: Station, status: SubscriptionStatus?) {
        hideProgressDialog()
        (activity as ChargingActivity).setActiveSession()
        mBinding.planInfoSubscriptionName.visibility = View.VISIBLE
        mBinding.planInfoSubscriptionName.setText(status?.planName ?: getString(R.string.plan_info_pay_as_you_go))
//        val kWhUsed = status

        if (status?.renewalDate != null) {
            mBinding.planInfoRenewalDate.visibility = View.VISIBLE
            mBinding.planInfoRenewalDate.setText(status.renewalDate)
        }

        if (status?.kwhUsage != null) {
            mBinding.planInfoKwhUsage.visibility = View.VISIBLE
            val text = String.format("%d/%s kWh", status.kwhUsage, status.printTotalKwh())
            mBinding.planInfoKwhUsage.setText(text)
            mBinding.planInfoKwhProgress.isVisible = true
            mBinding.planInfoKwhProgress.setPlan(status, false)
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
            val resetDate = DateTimeFormat.forPattern(getString(R.string.app_date_format))
                    .print(status.nextRemainingKwhRestoration)
            val text = String.format(getString(R.string.plan_info_exceeded),
                    status.totalKwh, resetDate, pricing.thereafterPrice)
            showPlanDialog(text, true, status.accountUrl)
            showPaymentInfo()
        }
        if (!status.planCoversTime) {
            val text = getString(R.string.plan_info_peak_hours, status.plan.startHour(), status.plan.finishHour(), pricing.priceKwh)

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
        mBinding.planInfoAlertMessage.setParentVisibility(true)
        mBinding.planInfoAlertMessage.text = issueMessage
        mBinding.planInfoChargeWithPayg.visibility = View.VISIBLE
    }

    private fun setUpButton() {
        mBinding.planInfoButton.isEnabled = true
        mBinding.planInfoButton.setOnClickListener { goToStartCharging() }
    }

    private fun showPlanDialog(message: String, isUpgradePlan: Boolean, accountUrl: String?) {
        mBinding.planInfoExplorePlans.setParentVisibility(true)
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

    override fun showDefaultPM(paymentMethod: PaymentMethod?) {
        onPaymentMethodChanged(paymentMethod)
    }

    private fun showPaymentInfo() {
        mBinding.planInfoChargeRate.setLabel(getString(R.string.plan_info_discounted_charge_rate))
        mBinding.planInfoCreditCardLabel.isVisible = true
        mBinding.planInfoCreditCard.isVisible = true
        mBinding.planInfoCreditCard.setPaymentMethod(mSelectedPM)
//        mBinding.planInfoCouponCode.visibility = View.VISIBLE

        mBinding.planInfoButton.isEnabled = true
        mBinding.planInfoButton.setOnClickListener {
            goToStartCharging()
        }
    }

    override fun showFree(freeChargingCode: String) {
        hideProgressDialog()
        (activity as ChargingActivity).setActiveSession()
        mBinding.planInfoFreeCharging.visibility = View.VISIBLE
        //TODO: mostrar prompt de freeChargingCode
        mBinding.planInfoButton.isEnabled = true
        setUpButton()
    }

    override fun onBackPressed(): Boolean {
        (activity as ChargingActivity).cancelSession(childFragmentManager)
        return true
    }

    fun onPaymentMethodChanged(paymentMethod: PaymentMethod?) {
        mSelectedPM = paymentMethod
    }

    override fun getProgressDialogLayout(): Int {
        return R.layout.spinner_layout_black
    }

    private fun goToStartCharging() {
        mListener.goToStartCharging(presenter.getStationId(), mSelectedPM?.id, null)
    }

}