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

    override fun getTitle(): String {
        return "Almost there!"
    }

    override fun getSubtitle() : String? {
        return " "
    }

    override fun userCanExit(): Boolean {
        return false
    }

}