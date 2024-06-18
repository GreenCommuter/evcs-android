package org.evcs.android.features.profile.sessioninformation

import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.evcs.android.ui.fragment.ErrorFragment
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.FragmentReceiptBinding
import org.evcs.android.model.Charge
import org.evcs.android.util.Extras
import org.joda.time.format.DateTimeFormat

@Deprecated("")
class ReceiptFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mBinding: FragmentReceiptBinding

    override fun layout(): Int {
        return R.layout.fragment_receipt
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun init() {}

    override fun setUi(v: View) {
        mBinding = FragmentReceiptBinding.bind(v)
    }

    override fun populate() {
        val dateTimeFormatter = DateTimeFormat.forPattern(getString(R.string.app_datetime_format))

        val charge = requireArguments().getSerializable(Extras.SessionInformationActivity.CHARGE) as Charge
        if (charge.startedAt != null)
            mBinding.sessionInformationChargingSiteDate.text = dateTimeFormatter.print(charge.startedAt)
        mBinding.sessionInformationEnergy.text = charge.printKwh()
        mBinding.sessionInformationPrice.text = getString(R.string.app_price_format, charge.paymentAmount)
        mBinding.sessionInformationChargingSiteSubtitle.text = charge.locationName
//        mBinding.receiptFee.text = getString(R.string.app_price_format, 0f)
        mBinding.receiptRate.isVisible = charge.ppkwh != null
        mBinding.receiptRate.setText(String.format("$%.2f/kWh", charge.ppkwh))
        mBinding.sessionInformationPlanType.text = charge.planName
        if (charge.paymentBrand != null && charge.paymentLast4 != null) {
            mBinding.sessionInformationPaymentMethod.isVisible = true
            mBinding.sessionInformationPaymentMethod.setText(getString(
                    R.string.app_payment_method_format, charge.paymentBrand, charge.paymentLast4))
        }
    }

}