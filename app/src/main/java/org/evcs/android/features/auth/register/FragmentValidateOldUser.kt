package org.evcs.android.features.auth.register

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.FragmentValidateOldUserBinding
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.UserUtils

class FragmentValidateOldUser : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mBinding: FragmentValidateOldUserBinding

    /**
     * Returns a new RegisterFragmentCellPhone instance.
     *
     * @return new instance.
     */
    fun newInstance(): FragmentValidateOldUser {
        val args = Bundle()
        val fragment = FragmentValidateOldUser()
        fragment.arguments = args
        return fragment
    }

    override fun layout(): Int {
        return R.layout.fragment_validate_old_user
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentValidateOldUserBinding.bind(v)
    }

    override fun init() {
    }

    override fun setListeners() {
        mBinding.fragmentValidateOldUserButton.setOnClickListener { onButtonClick() }
        mBinding.fragmentValidateOldUserLogout.setOnClickListener { UserUtils.logout(null) }
    }

    private fun onButtonClick() {
        findNavController().navigate(
            FragmentValidateOldUserDirections.actionRegisterFragmentYourCarToRegisterFragmentCellPhone()
        )
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}