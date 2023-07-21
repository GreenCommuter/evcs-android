package org.evcs.android.features.profile.sessioninformation

import androidx.core.view.isVisible
import org.evcs.android.model.Charge
import org.evcs.android.util.Extras

class ChargingSessionInformationFragment : SessionInformationFragment() {

    override fun init() {
        super.init()
        showProgressDialog()
        progressDialog.setCancelable(true)
        presenter?.getChargeFromSession(
                requireArguments().getInt(Extras.SessionInformationActivity.CHARGE_ID))
    }

    override fun populate() {
        mBinding.sessionInformationClose.isVisible = true
        mBinding.sessionInformationReceipt.isVisible = false
        mBinding.sessionInformationHelp.isVisible = false
    }

    override fun setListeners() {
        mBinding.sessionInformationClose.setOnClickListener { requireActivity().finish() }
    }

    override fun onBackPressed(): Boolean {
        requireActivity().finish()
        return true
    }
}
