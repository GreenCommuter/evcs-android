package org.evcs.android.features.profile.sessioninformation

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
    }

    override fun onBackPressed(): Boolean {
        requireActivity().finish()
        return true
    }
}
