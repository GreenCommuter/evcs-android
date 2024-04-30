package org.evcs.android.features.auth.register

import android.os.Bundle
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.util.UserUtils

class RegisterFragmentCellPhone : AbstractChangePhoneFragment() {

    /**
     * Returns a new RegisterFragmentCellPhone instance.
     *
     * @return new instance.
     */
    fun newInstance(): RegisterFragmentCellPhone {
        val args = Bundle()
        val fragment = RegisterFragmentCellPhone()
        fragment.arguments = args
        return fragment
    }

    override fun createPresenter(): RegisterPresenterCellphone<RegisterViewCellphone> {
        return RegisterPresenterCellphone(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun getTitle(): String? {
        return getString(R.string.fragment_register_cell_phone_title, UserUtils.getLoggedUser().firstName)
    }

    override fun getSubtitle() : String? {
        return getString(R.string.fragment_register_cell_phone_subtitle)
    }

    override fun userCanExit(): Boolean {
        return false
    }

}