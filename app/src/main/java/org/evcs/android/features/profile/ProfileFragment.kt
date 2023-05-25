package org.evcs.android.features.profile

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.navigation.fragment.findNavController
import com.base.core.permission.PermissionListener
import com.base.core.permission.PermissionManager
import com.base.core.presenter.BasePresenter
import com.base.core.util.NavigationUtils
import org.evcs.android.BaseConfiguration
import org.evcs.android.BuildConfig
import org.evcs.android.R
import org.evcs.android.activity.ChargingHistoryActivity
import org.evcs.android.activity.account.AccountActivity
import org.evcs.android.activity.subscription.SubscriptionActivity
import org.evcs.android.databinding.FragmentProfileBinding
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.StorageUtils
import org.evcs.android.util.UserUtils

class ProfileFragment : ErrorFragment<BasePresenter<*>>() {

    private val CALL_PERMISSION = "android.permission.CALL_PHONE"

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
        mBinding.profileMenuEvcsTermsAndConditions.setOnClickListener { goToWebView("https://www.evcs.com/terms-of-use") }
        mBinding.profileMenuCallCustomerCare.setOnClickListener { goToCallUs() }
        mBinding.profileMenuFeedback.setOnClickListener {  }
        mBinding.profileMenuSignOut.setOnClickListener { UserUtils.logout(null) }
        super.setListeners()
    }

    private fun goToWebView(url: String) {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToWebViewFragment(url))
    }

    fun goToCallUs() {
        PermissionManager.getInstance().requestPermission(this, object : PermissionListener() {
            override fun onPermissionsGranted() {
                super.onPermissionsGranted()
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(
                    "tel:" + StorageUtils.getStringFromSharedPreferences(
                        Extras.Configuration.PHONE_NUMBER,
                        BaseConfiguration.EVCSInformation.PHONE_NUMBER
                    )
                )
                startActivity(intent)
            }

            override fun onPermissionsDenied(deniedPermissions: Array<String?>) {
                super.onPermissionsDenied(deniedPermissions)
            }
        }, CALL_PERMISSION)
    }


    override fun onBackPressed(): Boolean {
        mNavigationListener.onMapClicked()
        return true
    }
}