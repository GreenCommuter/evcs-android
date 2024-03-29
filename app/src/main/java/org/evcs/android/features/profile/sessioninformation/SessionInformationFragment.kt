package org.evcs.android.features.profile.sessioninformation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.base.core.util.NavigationUtils
import com.base.core.util.NavigationUtils.IntentExtra
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.ContactSupportActivity
import org.evcs.android.databinding.ActivitySessionInformationBinding
import org.evcs.android.model.Charge
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.ViewUtils.setParentVisibility
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

open class SessionInformationFragment : ErrorFragment<SessionInformationPresenter>(), ISessionInformationView {

    private lateinit var mCharge: Charge
    private lateinit var mDateTimeFormatter: DateTimeFormatter
    protected lateinit var mBinding: ActivitySessionInformationBinding

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
        mDateTimeFormatter = DateTimeFormat.forPattern(getString(R.string.app_datetime_format))
    }

    override fun populate() {
        showProgressDialog()
        val chargeId = SessionInformationFragmentArgs.fromBundle(requireArguments()).chargeId
        presenter.getCharge(chargeId)
    }

    override fun showCharge(charge: Charge) {
        hideProgressDialog()
        mCharge = charge
        if (mCharge.startedAt != null)
            mBinding.sessionInformationChargingSiteDate.text = mDateTimeFormatter.print(mCharge.startedAt)
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
        if (mCharge.paymentAmount == 0f) {
            mBinding.sessionInformationReceipt.isVisible = false
        }
    }

    override fun setListeners() {
        //TODO: check if show address or request
        mBinding.sessionInformationHelp.setOnClickListener {
            NavigationUtils.jumpTo(requireContext(), ContactSupportActivity::class.java)
        }
        mBinding.sessionInformationReceipt.setOnClickListener {
            val args = Bundle()
            args.putSerializable(Extras.SessionInformationActivity.CHARGE, mCharge)
            findNavController().navigate(R.id.receiptFragment, args)
        }
    }

}
