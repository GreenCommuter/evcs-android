package org.evcs.android.features.charging

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import com.base.core.util.NavigationUtils
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.ChargingActivity
import org.evcs.android.databinding.FragmentPlanInfoBinding
import org.evcs.android.features.profile.plans.PlansActivity
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.Station
import org.evcs.android.model.SubscriptionStatus
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils
import org.evcs.android.util.VideoUtils.setVideoResource
import org.evcs.android.util.VideoUtils.startAndLoop
import org.evcs.android.util.ViewUtils.setParentVisibility
import org.evcs.android.util.ViewUtils.showOrHide
import org.joda.time.format.DateTimeFormat

class PlanInfoFragment : ErrorFragment<PlanInfoPresenter>(), PlanInfoView {

    private lateinit var mWalletLauncher: ActivityResultLauncher<Intent>
    private var mSelectedPM: PaymentMethod? = null
    private lateinit var mBinding: FragmentPlanInfoBinding
    private lateinit var mExplorePlansText: CharSequence

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
        mBinding.planInfoAnim.setVideoResource(R.raw.evcs_scene1, requireContext())
        mBinding.planInfoAnim.startAndLoop()
        mExplorePlansText = UserUtils.getLoggedUser().getExplorePlansText(resources)
    }

    override fun populate() {
        showProgressDialog()
        presenter?.populate(requireArguments().getString(Extras.PlanInfo.STATION_ID)!!)
    }

    override fun setListeners() {
        mBinding.planInfoCreditCard.setOnChangeClickListener {
            mWalletLauncher.launch(WalletActivity.buildIntent(requireContext(), true))
        }
        mBinding.planInfoCouponCode.setListener {  }
        mBinding.planInfoChargeWithPayg.setOnClickListener {
            //TODO: check
            goToStartCharging()
        }
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

        setProgress(status)
        mBinding.planInfoChargeRate.setParentVisibility(true)

        if (status == null) {
            showPlanDialog(mExplorePlansText, false, null)
            showPaymentInfo()
            return
        }
        setUpButton()
        setCapExceededAlert(status)
        if (!status.planCoversTime) {
            val pricing = station.pricing!!.detail
            val text = getString(R.string.plan_info_peak_hours, status.plan.startHour(), status.plan.finishHour(), pricing.priceKwh)
            showPlanDialog(text, true, status.accountUrl)
            showPaymentInfo()
        }
        if (status.isSuspended) {
            showIssue(status.issueMessage, null, getString(R.string.plan_info_payment_error_update))
            //TODO: use for plan, remove dialog
            mBinding.planInfoButton.setOnClickListener {
                mWalletLauncher.launch(WalletActivity.buildIntent(requireContext(), true))
            }
        }
        else if (status.issue) {
            showIssue(status.issueMessage, status.issueUrl, status.issueUrlTitle)
        }
    }

    private fun setProgress(status: SubscriptionStatus?) {
        if (status?.kwhUsage != null) {
            mBinding.planInfoKwhUsage.visibility = View.VISIBLE
            val text = String.format("%.0f/%s kWh", status.kwhUsage, status.printTotalKwh())
            mBinding.planInfoKwhUsage.setText(text)
            mBinding.planInfoKwhProgress.isVisible = true
            mBinding.planInfoKwhProgress.setPlan(status, false)
        }
    }

    private fun setCapExceededAlert(status: SubscriptionStatus) {
        if (!status.isUnlimited && status.remainingKwh != null && status.remainingKwh <= 0) {
            val resetDate = DateTimeFormat.forPattern(getString(R.string.app_date_format))
                    .print(status.nextRemainingKwhRestoration)
            val text = String.format(getString(R.string.plan_info_exceeded),
                    status.totalKwh, resetDate, status.pricePerKwh)
            showPlanDialog(text, true, status.accountUrl)
            showPaymentInfo()
        }
    }

    private fun showIssue(issueMessage: String, issueUrl: String?, issueUrlTitle: String) {
        mBinding.planInfoAlertMessage.setParentVisibility(true)
        mBinding.planInfoAlertMessage.text = issueMessage
        mBinding.planInfoChargeWithPayg.visibility = View.VISIBLE
        mBinding.planInfoButton.showOrHide(issueUrlTitle)
        mBinding.planInfoButton.setOnClickListener { launchUrl(issueUrl) }
    }

    private fun setUpButton() {
        mBinding.planInfoButton.isEnabled = true
        mBinding.planInfoButton.setOnClickListener { goToStartCharging() }
    }

    private fun showPlanDialog(message: CharSequence, isUpgradePlan: Boolean, accountUrl: String?) {
        mBinding.planInfoExplorePlans.setParentVisibility(true)
        mBinding.planInfoExplorePlans.text = message
        if (isUpgradePlan) {
            mBinding.planInfoExplorePlansButton.text = getString(R.string.plan_info_upgrade_plan)
            mBinding.planInfoExplorePlansButton.setOnClickListener {
                NavigationUtils.jumpTo(requireContext(), PlansActivity::class.java)
            }
        }
    }

    override fun showChargeRate(rate: String) {
        mBinding.planInfoChargeRateLoading.isVisible = false
        mBinding.planInfoChargeRate.setText(rate)
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

        mBinding.planInfoButton.isEnabled = mSelectedPM != null
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

    override fun showStationNotFound() {
        hideProgressDialog()
        if (arguments?.getBoolean(Extras.PlanInfo.FROM_QR) == true) {
            ToastUtils.show("Please try writing the numeric code for this station")
        } else {
            ToastUtils.show("Station not found")
        }
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