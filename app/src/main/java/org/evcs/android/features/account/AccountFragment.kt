package org.evcs.android.features.account

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.base.core.presenter.BasePresenter
import com.base.core.util.NavigationUtils.jumpTo
import org.evcs.android.R
import org.evcs.android.activity.SimpleToolbarActivity
import org.evcs.android.databinding.ActivityAccountBinding
import org.evcs.android.features.profile.ChangeCarFragment
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.UserUtils

class AccountFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mChangeUserResult: ActivityResultLauncher<Intent>
    private lateinit var mBinding: ActivityAccountBinding

    override fun init() {
        mChangeUserResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            populate()
        }
    }

    override fun layout(): Int {
        return R.layout.activity_account
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = ActivityAccountBinding.bind(v)
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun populate() {
        super.populate()
        val user = UserUtils.getLoggedUser()
        mBinding.fragmentAccountEmail.setText(user.email)
        mBinding.fragmentAccountPhone.setText(user.phone)
        mBinding.fragmentAccountCar.setText((user.userCar ?: "").toString())
        mBinding.fragmentAccountZipcode.setText(user.zipCode)
        mBinding.fragmentAccountName.setText(UserUtils.getLoggedUser().name)
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.fragmentAccountName.setOnClickListener {
            val intent = SimpleToolbarActivity.getIntent(requireContext(), ChangeNameFragment::class.java, "Edit your name")
            mChangeUserResult.launch(intent)
        }
        mBinding.fragmentAccountChangePassword.setOnClickListener {
            val intent = SimpleToolbarActivity.getIntent(requireContext(), ChangePasswordFragment::class.java, "Change password")
            mChangeUserResult.launch(intent)
        }
        mBinding.fragmentAccountCar.setOnClickListener {
            val intent = SimpleToolbarActivity.getIntent(requireContext(), ChangeCarFragment::class.java, "Edit your car")
            mChangeUserResult.launch(intent)
        }
        mBinding.fragmentAccountZipcode.setOnClickListener {
            val intent = SimpleToolbarActivity.getIntent(requireContext(), ZipCodeFragment::class.java, "Edit Zipcode")
            mChangeUserResult.launch(intent)
        }
        //TODO: add dialog
        mBinding.fragmentAccountSignOut.setOnClickListener { UserUtils.logout(null) }
        mBinding.fragmentAccountDelete.setOnClickListener { jumpTo(requireContext(), DeleteAccountActivity::class.java)  }
    }
}