package org.evcs.android.features.auth.register

import android.os.Bundle
import org.evcs.android.R

class FragmentCellPhoneOldUser : AbstractChangePhoneFragment() {

    /**
     * Returns a new RegisterFragmentCellPhone instance.
     *
     * @return new instance.
     */
    fun newInstance(): FragmentCellPhoneOldUser {
        val args = Bundle()
        val fragment = FragmentCellPhoneOldUser()
        fragment.arguments = args
        return fragment
    }

    override fun getTitle(): String? {
        return "Our request"
    }

    override fun getSubtitle() : String? {
        return getString(R.string.fragment_register_cell_phone_subtitle)
    }

    override fun userCanExit(): Boolean {
        return false
    }


}