package org.evcs.android.features.profile.sessioninformation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.ActivitySessionInformationBinding
import org.evcs.android.model.Charge
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.view.shared.ReceiptItemView
import org.evcs.android.util.ViewUtils.setMargins
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

open class SessionInformationFragment : ErrorFragment<SessionInformationPresenter>(), ISessionInformationView {

    private lateinit var mCharge: Charge
    private lateinit var mDateFormatter: DateTimeFormatter
    private lateinit var mTimeFormatter: DateTimeFormatter
    protected lateinit var mBinding: ActivitySessionInformationBinding

    companion object {
        fun newInstance(chargeId: Int): SessionInformationFragment {
            val args = Bundle()
            args.putInt("chargeId", chargeId)
            val fragment = SessionInformationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createPresenter(): SessionInformationPresenter {
        return SessionInformationPresenter(
            this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = ActivitySessionInformationBinding.bind(v)
    }

    override fun layout(): Int {
        return R.layout.activity_session_information
    }

    override fun init() {
        mDateFormatter = DateTimeFormat.forPattern(getString(R.string.app_date_format))
        mTimeFormatter = DateTimeFormat.forPattern(getString(R.string.app_time_format))
    }

    override fun populate() {
        showProgressDialog()
        val chargeId = SessionInformationFragmentArgs.fromBundle(requireArguments()).chargeId
        presenter.getCharge(chargeId)
    }

    override fun setListeners() {
        mBinding.sessionInformationClose.setOnClickListener { requireActivity().finish() }
    }

    override fun showCharge(charge: Charge) {
        hideProgressDialog()
        mCharge = charge
        if (mCharge.startedAt != null) {
            var dateString = mDateFormatter.print(mCharge.startedAt)
            if (mCharge.startedAt.withTimeAtStartOfDay() != mCharge.completedAt.withTimeAtStartOfDay()) {
                dateString += " - " + mDateFormatter.print(mCharge.completedAt)
            }
            mBinding.sessionInformationChargingSiteDate.text = dateString
            mBinding.sessionInformationChargingSiteTime.text =
                mTimeFormatter.print(mCharge.startedAt) + " to " + mTimeFormatter.print(mCharge.completedAt)
        }
        mBinding.sessionInformationDuration.text = mCharge.printableDuration
        mBinding.sessionInformationEnergy.text = mCharge.printKwh()
        mBinding.sessionInformationPrice.text = getString(R.string.app_price_format, mCharge.paymentAmount)
        mBinding.sessionInformationId.text = mCharge.id.toString()
        mBinding.sessionInformationChargingSiteSubtitle.text = mCharge.locationName
        mBinding.sessionInformationPlanType.text = mCharge.planName
        mBinding.sessionInformationChargingSiteId.setText(mCharge.stationName)
        mBinding.sessionInformationChargingSiteId.isVisible = mCharge.stationName != null
        if (mCharge.paymentBrand != null && mCharge.paymentLast4 != null) {
            mBinding.sessionInformationPaymentMethod.isVisible = true
            mBinding.sessionInformationPaymentMethod.setText(getString(
                R.string.app_payment_method_format, mCharge.paymentBrand, mCharge.paymentLast4))
        }

        mBinding.sessionInformationChargingSiteAddress.text = mCharge.address
        if (mCharge.image != null) {
            mBinding.sessionInformationImage.isVisible = true
            mBinding.sessionInformationImage.setImageURI(mCharge.image)
        }

        mCharge.costBreakdown.forEach { invoiceline ->
            val v = ReceiptItemView(requireContext(), invoiceline.label, invoiceline.detail, invoiceline.amount)
            v.setMargins(0, 0, 0, resources.getDimension(R.dimen.spacing_medium_extra).toInt())
            mBinding.sessionInformationCostBreakdown.addView(v)
        }

        mBinding.sessionInformationMaxRate.setText(mCharge.station.printStationPower())
    }

}
