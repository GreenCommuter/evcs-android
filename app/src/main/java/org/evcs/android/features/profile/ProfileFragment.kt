package org.evcs.android.features.profile

import android.view.View
import com.base.core.presenter.BasePresenter
import com.base.core.util.NavigationUtils
import org.evcs.android.BuildConfig
import org.evcs.android.R
import org.evcs.android.activity.ChargingHistoryActivity
import org.evcs.android.activity.account.AccountActivity
import org.evcs.android.activity.subscription.SubscriptionActivity
import org.evcs.android.databinding.FragmentProfileBinding
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.UserUtils

class ProfileFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mBinding: FragmentProfileBinding

    val mNavigationListener = MainNavigationController.getInstance()

    override fun layout(): Int {
        return R.layout.fragment_profile
    }

    override fun setUi(v: View?) {
        mBinding = FragmentProfileBinding.bind(v!!)
        super.setUi(v)
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter<Any?>(this)
    }

    override fun init() {
        mBinding.profileMenuVersion.text = "Version " + BuildConfig.VERSION_NAME
    }

    override fun setListeners() {
        mBinding.profileMenuAccount.setOnClickListener { NavigationUtils.jumpTo(requireContext(), AccountActivity::class.java) }
        mBinding.profileMenuPayments.setOnClickListener { NavigationUtils.jumpTo(requireContext(), WalletActivity::class.java) }
        mBinding.profileMenuSubscriptionPlan.setOnClickListener { NavigationUtils.jumpTo(requireContext(), SubscriptionActivity::class.java) }
        mBinding.profileMenuChargingHistory.setOnClickListener { NavigationUtils.jumpTo(requireContext(), ChargingHistoryActivity::class.java) }
        mBinding.profileMenuEvcsTermsAndConditions.setOnClickListener {  }
        mBinding.profileMenuCallCustomerCare.setOnClickListener {  }
        mBinding.profileMenuFeedback.setOnClickListener {  }
        mBinding.profileMenuSignOut.setOnClickListener { UserUtils.logout(null) }
        super.setListeners()
    }

    override fun onBackPressed(): Boolean {
        mNavigationListener.onMapClicked()
        return true
    }
}