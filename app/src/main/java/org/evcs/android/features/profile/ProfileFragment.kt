package org.evcs.android.features.profile

import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.base.core.util.NavigationUtils
import com.base.core.util.NavigationUtils.IntentExtra
import org.evcs.android.BuildConfig
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.ContactSupportActivity
import org.evcs.android.activity.account.AccountActivity
import org.evcs.android.activity.account.VehicleInformationActivity
import org.evcs.android.activity.subscription.SubscriptionActivity
import org.evcs.android.databinding.FragmentProfileBinding
import org.evcs.android.features.main.MainActivity
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.features.profile.notifications.NotificationsActivity
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.model.Subscription
import org.evcs.android.model.user.User
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.FontUtils
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils.setParentVisibility
import org.evcs.android.util.ViewUtils.showOrHide

class ProfileFragment : ErrorFragment<ProfilePresenter>(), ProfileView {

    private val TERMS_URL = "https://www.evcs.com/terms-of-use"
    private val FAQ_URL = "https://www.evcs.com/ev-drivers/faqs"
    private val REQUEST_URL = "https://support.evcs.com/hc/en-us/requests/new"

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
        showProgressDialog()
        presenter.refreshUser()
        presenter.refreshDefaultPaymentMethod()
        mLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            showProgressDialog()
            presenter.refreshUser()
        }
    }

    override fun onUserRefreshed(response: User) {
        hideProgressDialog()
        setUser(response)
    }

    private fun setUser(user: User) {
        mBinding.profileName.text = user.name
//        if (!user.isPhoneVerified) {
//            setSubscription(null)
//            setUnverifiedUser()
//            return
//        } else {
            setSubscription(user.activeSubscription)
//        }
    }

    private fun setSubscription(subscription: Subscription?) {
        mBinding.profileExplorePlans.setParentVisibility(subscription == null)

        mBinding.profileMenuSubscriptionPlan.isVisible = true//subscription != null
        mBinding.profilePlanProgress.isVisible = subscription != null
        mBinding.profileIssueButton.setParentVisibility(subscription?.issue ?: false)
        mBinding.profileIssueButton.isVisible = subscription?.isSuspended ?: false
        mBinding.profileIssueButton.setOnClickListener { goToActivity(WalletActivity::class.java) }

        mBinding.profileExplorePlansText.text =
                FontUtils.getSpannable(resources.getStringArray(R.array.profile_explore_plans_text), Color.BLACK)
        if (subscription == null) {
            mBinding.profilePlanName.text = getString(R.string.plan_info_pay_as_you_go) + " membership"
        } else {
            mBinding.profilePlanName.text = subscription.planName
            mBinding.profilePlanProgress.setPlan(subscription)
            val issueText = if (subscription.issue) subscription.issueMessage else null
            val buttonText = if (subscription.isSuspended) "Update payment method" else null
            setIssue(issueText, buttonText) { goToActivity(WalletActivity::class.java) }
        }
        mBinding.profileMenuVersion.text =
                getString(R.string.profile_version, BuildConfig.VERSION_NAME)
        (activity as MainActivity).updateProfileAlert()
    }

    private fun setUnverifiedUser() {
        mBinding.profilePlanName.text = "Account not activated"
        mBinding.profileExplorePlans.setParentVisibility(false)
    }

    //null = hide
    private fun setIssue(issueText: String?, buttonText: String?, buttonListener: View.OnClickListener) {
        mBinding.profileIssueButton.setParentVisibility(issueText != null)
        mBinding.profileIssueMessage.text = issueText
        mBinding.profileIssueButton.showOrHide(buttonText)
        mBinding.profileIssueButton.setOnClickListener(buttonListener)
    }

    override fun populate() {
        setUser(UserUtils.getLoggedUser())
    }

    override fun setListeners() {
        mBinding.profileMenuAccount.setOnClickListener { goToActivityAndRefresh(AccountActivity::class.java) }
        mBinding.profileMenuPaymentMethods.setOnClickListener { goToActivity(WalletActivity::class.java) }
        mBinding.profileMenuSubscriptionPlan.setOnClickListener { goToActivityAndRefresh(SubscriptionActivity::class.java) }
        mBinding.profileMenuChargingHistory.setOnClickListener { findNavController().navigate(R.id.chargingHistoryFragment) }
        mBinding.profileMenuEvcsTermsAndConditions.setOnClickListener { goToWebView(TERMS_URL) }
        mBinding.profileMenuCallCustomerCare.setOnClickListener {
            val extra = IntentExtra(Extras.ContactSupportActivity.SHOW_ADDRESS, true)
            NavigationUtils.jumpTo(requireContext(), ContactSupportActivity::class.java, extra)
        }
        mBinding.profileMenuPayments.setOnClickListener { findNavController().navigate(R.id.paymentHistoryFragment) }
        mBinding.profileMenuSignOut.setOnClickListener { UserUtils.logout(null) }
        mBinding.profileMenuVehicleInfo.setOnClickListener { goToActivity(VehicleInformationActivity::class.java) }

        mBinding.profileMenuShowPlans.setOnClickListener { findNavController().navigate(R.id.plansFragment) }
        mBinding.profileExplorePlans.setOnClickListener { findNavController().navigate(R.id.plansFragment) }
        mBinding.profileMenuNotifications.setOnClickListener { goToActivityAndRefresh(NotificationsActivity::class.java) }
        mBinding.profileMenuFaq.setOnClickListener { goToWebView(FAQ_URL) }
        mBinding.profileMenuRequest.setOnClickListener { goToWebView(REQUEST_URL) }
        super.setListeners()
    }

    private fun <T : FragmentActivity> goToActivity(activity: Class<T>) {
        NavigationUtils.jumpTo(requireContext(), activity)
    }

    private fun <T : FragmentActivity> goToActivityAndRefresh(activity: Class<T>) {
        mLauncher.launch(Intent(requireContext(), activity))
    }

    private fun goToWebView(url: String) {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToWebViewFragment(url))
    }

    override fun onBackPressed(): Boolean {
        try {
            mNavigationListener.onMapClicked()
        } catch (npe : NullPointerException) {
            NavigationUtils.jumpToClearingTask(requireContext(), MainActivity::class.java)
        }
        return true
    }

    override fun showProgressDialog() {
        mBinding.profileLoading.isVisible = true
    }

    override fun hideProgressDialog() {
        mBinding.profileLoading.isVisible = false
    }
}