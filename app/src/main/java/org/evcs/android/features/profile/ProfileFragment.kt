package org.evcs.android.features.profile

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.base.core.permission.PermissionListener
import com.base.core.permission.PermissionManager
import com.base.core.util.NavigationUtils
import org.evcs.android.BaseConfiguration
import org.evcs.android.BuildConfig
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.features.profile.notifications.NotificationsActivity
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
import org.evcs.android.util.ViewUtils.setVisibility

class ProfileFragment : ErrorFragment<ProfilePresenter>(), ProfileView {

    private val CALL_PERMISSION = "android.permission.CALL_PHONE"
    private val TERMS_URL = "https://www.evcs.com/terms-of-use"
    private val FAQ_URL = "www.evcs.com/ev-drivers/faqs"
    private val REQUEST_URL = "support.evcs.com/hc/en-us/requests/new"

    private lateinit var mLauncher: ActivityResultLauncher<Intent>
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
        mLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            presenter.refreshUser()
        }
    }

    override fun onUserRefreshed(response: User) {
        setUser(response)
    }

    private fun setUser(user: User) {
        mBinding.profileName.text = user.name
        val subscription = user.activeSubscription

        (mBinding.profileExplorePlans.parent as View).isVisible = subscription == null

        mBinding.profileMenuSubscriptionPlan.isVisible = subscription != null
        mBinding.profilePlanProgress.isVisible = subscription!= null
        (mBinding.profileIssueButton.parent as View).isVisible = subscription?.issue ?: false
        mBinding.profileIssueButton.isVisible = subscription?.isSuspended?:false

        mBinding.profileExplorePlansText.text =
                FontUtils.getSpannable(resources.getStringArray(R.array.profile_explore_plans_text), Color.BLACK)
        if (subscription == null) {
            if (false /*user is verified*/) {
                mBinding.profilePlanName.text = "Account not activated"
            } else {
                mBinding.profilePlanName.text = getString(R.string.plan_info_pay_as_you_go) + " membership"
            }
        } else {
            mBinding.profilePlanName.text = subscription.planName
            mBinding.profilePlanProgress.setPlan(subscription)
            if (subscription.issue) {
                mBinding.profileIssueMessage.text = subscription.issueMessage
            }
        }
        mBinding.profileMenuVersion.text =
                getString(R.string.profile_version, BuildConfig.VERSION_NAME)
    }

    override fun populate() {
        setUser(UserUtils.getLoggedUser())
    }

    override fun setListeners() {
        mBinding.profileMenuAccount.setOnClickListener { goToActivity(AccountActivity::class.java) }
        mBinding.profileMenuPaymentMethods.setOnClickListener { goToActivity(WalletActivity::class.java) }
        mBinding.profileMenuSubscriptionPlan.setOnClickListener { mLauncher.launch(Intent(context, SubscriptionActivity::class.java)) }
        mBinding.profileMenuChargingHistory.setOnClickListener { findNavController().navigate(R.id.chargingHistoryFragment) }
        mBinding.profileMenuEvcsTermsAndConditions.setOnClickListener { goToWebView(TERMS_URL) }
        mBinding.profileMenuCallCustomerCare.setOnClickListener { goToCallUs() }
        mBinding.profileMenuPayments.setOnClickListener { findNavController().navigate(R.id.paymentHistoryFragment) }
        mBinding.profileMenuSignOut.setOnClickListener { UserUtils.logout(null) }
        mBinding.profileMenuVehicleInfo.setOnClickListener { goToActivity(VehicleInformationActivity::class.java) }

        mBinding.profileMenuShowPlans.setOnClickListener { findNavController().navigate(R.id.plansFragment) }
        mBinding.profileExplorePlans.setOnClickListener { findNavController().navigate(R.id.plansFragment) }
        mBinding.profileIssueButton.setOnClickListener { goToActivity(WalletActivity::class.java) }
        mBinding.profileMenuNotifications.setOnClickListener { goToActivity(NotificationsActivity::class.java) }
        mBinding.profileMenuFaq.setOnClickListener { goToWebView(FAQ_URL) }
        mBinding.profileMenuRequest.setOnClickListener { goToWebView(REQUEST_URL) }
        super.setListeners()
    }

    private fun <T : FragmentActivity> goToActivity(activity: Class<T>) {
        NavigationUtils.jumpTo(requireContext(), activity)
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