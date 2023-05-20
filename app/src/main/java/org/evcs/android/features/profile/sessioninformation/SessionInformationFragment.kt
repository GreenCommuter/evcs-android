package org.evcs.android.features.profile.sessioninformation

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.navigation.fragment.findNavController
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.ActivitySessionInformationBinding
import org.evcs.android.features.map.location.ILocationView
import org.evcs.android.features.map.location.LocationPresenter
import org.evcs.android.model.Charge
import org.evcs.android.model.Location
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class SessionInformationFragment : ErrorFragment<LocationPresenter>(), ILocationView {

    private lateinit var mCharge: Charge
    private lateinit var mDateTimeFormatter: DateTimeFormatter
    private lateinit var mBinding: ActivitySessionInformationBinding

    override fun createPresenter(): LocationPresenter {
        return LocationPresenter(
            this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = ActivitySessionInformationBinding.bind(v)
    }

    override fun layout(): Int {
        return R.layout.activity_session_information
    }

    override fun init() {}

    override fun populate() {
        mDateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy, h:mm a")

        mCharge = requireArguments().getSerializable(Extras.SessionInformationActivity.CHARGE) as Charge
        presenter.getLocation(mCharge.locationId)
        if (mCharge.startedAt != null)
            mBinding.sessionInformationChargingSiteDate.text = mDateTimeFormatter.print(mCharge.startedAt)
        mBinding.sessionInformationDuration.text = mCharge.printableDuration
        mBinding.sessionInformationEnergy.text = String.format("%.3f kWh", mCharge.kwh)
        mBinding.sessionInformationPrice.text = String.format("$%.2f", mCharge.price)
        mBinding.sessionInformationId.text = mCharge.id.toString()
        mBinding.sessionInformationChargingSiteSubtitle.text = mCharge.locationName
        mBinding.sessionInformationPlanType.text = mCharge.planName
    }

    override fun setListeners() {
        mBinding.sessionInformationToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        mBinding.sessionInformationHelp.movementMethod = LinkMovementMethod.getInstance()
        mBinding.sessionInformationReceipt.setOnClickListener {
            val args = Bundle()
            args.putSerializable(Extras.SessionInformationActivity.CHARGE, mCharge)
            findNavController().navigate(R.id.receiptFragment, args)
        }
    }

    override fun showLocation(response: Location?) {
        mBinding.sessionInformationChargingSiteId.text = response!!.id.toString()
        mBinding.sessionInformationChargingSiteAddress.text = response.address.toString()
    }

}
