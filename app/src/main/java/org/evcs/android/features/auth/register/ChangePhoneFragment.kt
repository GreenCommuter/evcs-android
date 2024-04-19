package org.evcs.android.features.auth.register

import android.os.Bundle
import org.evcs.android.R

class ChangePhoneFragment : AbstractChangePhoneFragment() {

    /**
     * Returns a new RegisterFragmentCellPhone instance.
     *
     * @return new instance.
     */
    fun newInstance(): ChangePhoneFragment {
        val args = Bundle()
        val fragment = ChangePhoneFragment()
        fragment.arguments = args
        return fragment
    }

    override fun getTitle(): String? {
        return null
    }

    override fun getSubtitle() : String? {
        return null
    }

    override fun userCanExit(): Boolean {
        return true
    }

    override fun getButtonText(): CharSequence {
        return getString(R.string.app_save)
    }

}