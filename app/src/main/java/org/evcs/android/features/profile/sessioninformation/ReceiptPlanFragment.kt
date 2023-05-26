package org.evcs.android.features.profile.sessioninformation

import android.view.View
import androidx.navigation.fragment.findNavController
import org.evcs.android.ui.fragment.ErrorFragment
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.FragmentReceiptPlanBinding
import org.evcs.android.model.Charge
import org.evcs.android.model.Payment
import org.evcs.android.util.Extras
import org.evcs.android.util.StringUtils
import org.evcs.android.util.UserUtils
import org.joda.time.format.DateTimeFormat

class ReceiptPlanFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mPayment: Payment
    private lateinit var mBinding: FragmentReceiptPlanBinding

    override fun layout(): Int {
        return R.layout.fragment_receipt_plan
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun init() {
        mPayment = requireArguments().getSerializable(Extras.SessionInformationActivity.PAYMENT) as Payment
    }

    override fun setUi(v: View) {
        mBinding = FragmentReceiptPlanBinding.bind(v)
    }

    override fun populate() {
        val dateTimeFormatter = DateTimeFormat.forPattern(getString(R.string.app_date_format))

        //TODO: may be another subscription, i have payment.subscriptionId
        val subscription = UserUtils.getLoggedUser().activeSubscription!!
        val period = StringUtils.capitalize(subscription.plan.renewalPeriod.toAdverb())
        mBinding.sessionInformationChargingSiteSubtitle.text = "${period} Plan"
        mBinding.sessionInformationChargingSiteDate.text = dateTimeFormatter.print(mPayment.createdAt)
        mBinding.sessionInformationPlanType.text = subscription.planName
        mBinding.sessionInformationStartDate.text = dateTimeFormatter.print(subscription.activeSince)
        mBinding.sessionInformationNextInstallment.text = dateTimeFormatter.print(subscription.renewalDate)
        mBinding.sessionInformationPaymentMethod.text = getString(R.string.app_payment_method_format)
        mBinding.sessionInformationPrice.text = getString(R.string.app_price_format, mPayment.amount)
    }

    override fun setListeners() {
        mBinding.sessionInformationToolbar.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}