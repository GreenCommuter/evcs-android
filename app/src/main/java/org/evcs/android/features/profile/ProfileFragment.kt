package org.evcs.android.features.profile

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.navigation.fragment.findNavController
import com.base.core.permission.PermissionListener
import com.base.core.permission.PermissionManager
import com.base.core.presenter.BasePresenter
import com.base.core.util.NavigationUtils
import org.evcs.android.BaseConfiguration
import org.evcs.android.BuildConfig
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.account.AccountActivity
import org.evcs.android.activity.subscription.SubscriptionActivity
import org.evcs.android.activity.account.VehicleInformationActivity
import org.evcs.android.databinding.FragmentProfileBinding
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.model.user.User
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.FontUtils
import org.evcs.android.util.StorageUtils
import org.evcs.android.util.UserUtils

class ProfileFragment : ErrorFragment<ProfilePresenter>(), ProfileView {

    private val CALL_PERMISSION = "android.permission.CALL_PHONE"

    private lateinit var mBinding: FragmentProfileBinding

    val mNavigationListener = MainNavigationController.getInstance()

    override fun layout(): Int {
        return R.layout.fragment_profile
    }

    override fun setUi(v: View) {
        mBinding = FragmentProfileBinding.bind(v)
        super.setUi(v)
    }

    override fun createPresenter(): ProfilePresenter {
        return ProfilePresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {
        presenter.refreshUser()
        presenter.refreshDefaultPaymentMethod()
    }

    override fun onUserRefreshed(response: User) {
        setUser(response)
    }

    private fun setUser(user: User) {
        mBinding.profileName.text = user.name
        val subscription = user.activeSubscription
        if (subscription == null) {
            if (false /*user is verified*/) {
                mBinding.profilePlanName.text = "Account not activated"
            } else {
                mBinding.profilePlanName.text = getString(R.string.plan_info_pay_as_you_go) + " membership"
                mBinding.profileExplorePlansText.text =
                        FontUtils.getSpannable(resources.getStringArray(R.array.profile_explore_plans_text), Color.BLACK)
                (mBinding.profileExplorePlans.parent as View).visibility = View.VISIBLE
            }
            mBinding.profileMenuSubscriptionPlan.visibility = View.GONE
        } else {
            mBinding.profilePlanName.text = subscription.planName
            mBinding.profilePlanProgress.visibility = View.VISIBLE
            mBinding.profilePlanProgress.setPlan(subscription)
            if (subscription.issue) {
                (mBinding.profileIssueButton.parent as View).visibility = View.VISIBLE
                mBinding.profileIssueMessage.text = subscription.issueMessage
                if (subscription.isSuspended) {
                    mBinding.profileIssueButton.visibility = View.VISIBLE
                }
            }
        }
        mBinding.profileMenuVersion.text =
                String.format(getString(R.string.profile_version), BuildConfig.VERSION_NAME)
    }

    override fun populate() {
        setUser(UserUtils.getLoggedUser())
    }

    override fun setListeners() {
        mBinding.profileMenuAccount.setOnClickListener { NavigationUtils.jumpTo(requireContext(), AccountActivity::class.java) }
        mBinding.profileMenuPaymentMethods.setOnClickListener { NavigationUtils.jumpTo(requireContext(), WalletActivity::class.java) }
        mBinding.profileMenuSubscriptionPlan.setOnClickListener { NavigationUtils.jumpTo(requireContext(), SubscriptionActivity::class.java) }
        mBinding.profileMenuChargingHistory.setOnClickListener { findNavController().navigate(R.id.chargingHistoryFragment) }
        mBinding.profileMenuEvcsTermsAndConditions.setOnClickListener { goToWebView("https://www.evcs.com/terms-of-use") }
        mBinding.profileMenuCallCustomerCare.setOnClickListener { goToCallUs() }
        mBinding.profileMenuFeedback.setOnClickListener {  }
        mBinding.profileMenuSignOut.setOnClickListener { UserUtils.logout(null) }
        mBinding.profileMenuVehicleInfo.setOnClickListener { NavigationUtils.jumpTo(requireContext(), VehicleInformationActivity::class.java) }

        mBinding.profileMenuShowPlans.setOnClickListener { findNavController().navigate(R.id.plansFragment) }
        mBinding.profileExplorePlans.setOnClickListener { findNavController().navigate(R.id.plansFragment) }
        mBinding.profileIssueButton.setOnClickListener {  }
        super.setListeners()
    }

    private fun goToWebView(url: String) {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToWebViewFragment(url))
    }

    fun goToCallUs() {
        goToPhone(StorageUtils.getStringFromSharedPreferences(
            Extras.Configuration.PHONE_NUMBER,
            BaseConfiguration.EVCSInformation.PHONE_NUMBER))
    }

    fun goToPhone(phone: String) {
        PermissionManager.getInstance().requestPermission(this, object : PermissionListener() {
            override fun onPermissionsGranted() {
                super.onPermissionsGranted()
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + phone)
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