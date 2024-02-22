package org.evcs.android.features.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.navigation.Navigation.findNavController
import com.base.core.util.NavigationUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.evcs.android.R
import org.evcs.android.activity.AbstractSupportedVersionActivity
import org.evcs.android.databinding.ActivityBaseNavhostWithBottomNavBinding
import org.evcs.android.features.auth.register.VerifyPhoneActivity
import org.evcs.android.features.profile.plans.PlanViewHelper
import org.evcs.android.features.profile.plans.PlansActivity
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.features.shared.EVCSSliderDialogFragment
import org.evcs.android.features.shared.IVersionView
import org.evcs.android.model.Subscription
import org.evcs.android.util.Extras
import org.evcs.android.util.FontUtils
import org.evcs.android.util.PushNotificationUtils
import org.evcs.android.util.UserUtils
import org.evcs.android.util.PaymentUtils
import org.evcs.android.util.ViewUtils.setMargins

class MainActivity : AbstractSupportedVersionActivity(), IVersionView {
    var mNavigationController: MainNavigationController? = null
    private lateinit var menuView: BottomNavigationView
    private lateinit var mButton: TextView
    var isBottomOfStack = true
        private set
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result -> if (result.resultCode == RESULT_OK)
                    showSuccessDialog(R.string.success_dialog_subtitle_phone)
        }
        isBottomOfStack = intent.getBooleanExtra(Extras.MainActivity.IS_BOTTOM, true)
    }

    @CallSuper
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        createNotificationChannel()
        mNavigationController = MainNavigationController(this, true,
            findNavController(this, R.id.activity_base_content))
        mNavigationController!!.startFlow()

//        if (!intent.hasExtra(Extras.Root.VIEW_KEY)) {
//            return
//        }
    }

    override fun inflate(layoutInflater: LayoutInflater): View {
        val binding = ActivityBaseNavhostWithBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        menuView = binding.bottomNavigation
        mButton = binding.bottomNavigationButton
        return binding.root
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notifications_channel_name)
            val description = getString(R.string.notifications_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(PushNotificationUtils.CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun getNavGraphId(): Int {
        return R.navigation.navigation_graph
    }

    override fun isSupportedVersion(isSupported: Boolean, versionWording: String?) {
        if (!isSupported) {
            showNotSupportedVersion(versionWording)
        }
    }

    override fun populate() {
        super.populate()
        if (UserUtils.getLoggedUser() == null) {
            menuView.menu.clear()
            mButton.visibility = View.VISIBLE
        } else {
            menuView.inflateMenu(R.menu.drawer)
            mButton.visibility = View.GONE
        }
        menuView.selectedItemId = R.id.menu_drawer_map
        updateProfileAlert()
        if (intent.hasExtra(Extras.VerifyActivity.RESULT)) {
            onVerifyResult(intent.getIntExtra(Extras.VerifyActivity.RESULT, RESULT_CANCELED))
            intent.removeExtra(Extras.VerifyActivity.RESULT)
        }
        if (intent.hasExtra(Extras.PlanActivity.PLAN)) {
            showCongratulationsDialog(intent.getSerializableExtra(Extras.PlanActivity.PLAN) as Subscription)
            intent.removeExtra(Extras.PlanActivity.PLAN)
        }
    }

    fun onVerifyResult(verifyResult: Int) {
        updateProfileAlert()
        if (verifyResult == RESULT_OK) {
            showSuccessDialog()
        } else {
//            showAccountNotValidatedDialog()
            showSuccessDialog()
        }
    }

    override fun setListeners() {
        super.setListeners()
        mButton.setOnClickListener { NavigationUtils.jumpTo(this, PlansActivity::class.java) }
        menuView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_drawer_map -> mNavigationController!!.onMapClicked()
                R.id.menu_drawer_charging -> mNavigationController!!.goToPreCharging()
                R.id.menu_drawer_profile -> mNavigationController!!.goToProfile()
            }
            //will be updated after canceling session
            false
        }
    }

    override fun onNetworkError() {
        //Handled by fragment
    }

    fun setSelectedItem(item: Int) {
        for (i in 0 until menuView.menu.size()) {
            menuView.menu.getItem(i).isChecked = false
        }
        menuView.menu.findItem(item)?.isChecked = true
    }

    fun updateProfileAlert() {
//        showProfileAlert(UserUtils.getLoggedUser()?.activeSubscription?.issue ?: false ||
//        !(UserUtils.getLoggedUser()?.isPhoneVerified ?: true))
    }

    fun showProfileAlert(show: Boolean) {
        if (!::menuView.isInitialized) return
        if (show) {
            menuView.getOrCreateBadge(R.id.menu_drawer_profile)
        } else {
            menuView.removeBadge(R.id.menu_drawer_profile)
        }
    }

    fun showWelcomeDialog() {
        EVCSSliderDialogFragment.Builder()
            .setTitle(getString(R.string.welcome_dialog_title), R.style.Label_Large)
            .setSubtitle(getString(R.string.welcome_dialog_subtitle))
            .addButton(getString(R.string.app_close)) { fragment -> fragment.dismiss() }
            .show(supportFragmentManager)
    }

    fun showSuccessDialog(@StringRes subtitle : Int = R.string.success_dialog_subtitle) {
        val textView = TextView(this)
        textView.text = getString(R.string.success_dialog_cta)
        textView.setTextAppearance(this, R.style.Label_Medium)
        textView.gravity = Gravity.CENTER
        textView.setMargins(0, 0, 0, resources.getDimension(R.dimen.spacing_big_k).toInt())

        val button = if (UserUtils.userCanDoTrial()) R.string.app_trial_cta_default
        else R.string.plan_info_explore_plans

        var dialogBuilder = EVCSSliderDialogFragment.Builder()
            .setTitle(getString(R.string.success_dialog_title), R.style.Label_Large)
            .setSubtitle(getString(subtitle))

        if (UserUtils.getLoggedUser() == null || UserUtils.getLoggedUser().activeSubscription == null) {
            dialogBuilder = dialogBuilder.addView(textView)
            dialogBuilder.addButton(getString(button)) {
                NavigationUtils.jumpTo(this, PlansActivity::class.java)
            }
        }
        dialogBuilder.show(supportFragmentManager)
    }

    fun showCongratulationsDialog(subscription: Subscription) {
        val secondLine = if (subscription.onTrialPeriod) getString(R.string.congratulations_dialog_subtitle_3)
                         else PlanViewHelper.instance(this, subscription.plan).getCongratulationsDialogSubtitle()
        val planName = if (subscription.onTrialPeriod) "Free Trial" else subscription.planName + " Plan"
        EVCSSliderDialogFragment.Builder()
            .setTitle(getString(R.string.congratulations_dialog_title), R.style.Label_Large)
            .setSubtitle(getString(R.string.congratulations_dialog_subtitle, planName, secondLine))
            .addButton(getString(R.string.app_close)) { fragment -> fragment.dismiss() }
            .show(supportFragmentManager)
    }

    fun showAccountSuspendedDialog() {
        val subtitle = FontUtils.getSpannable(
            resources.getStringArray(R.array.account_suspended_subtitle), Color.BLACK)

        EVCSDialogFragment.Builder()
            .setTitle(getString(R.string.account_suspended_title))
            .setSubtitle(subtitle, Gravity.CENTER)
            .addButton(getString(R.string.account_suspended_button), {
                    fragment -> fragment.dismiss()
                    PaymentUtils.goToPendingPayment(this)
            },
                R.style.ButtonK_Blue)
            .showCancel(getString(R.string.app_close))
            //addButton(getString(R.string.app_close), { fragment -> fragment.dismiss() }, R.style.ButtonK_BlackOutline)
            .show(supportFragmentManager)
    }

    fun showPaymentFailureDialog() {
        EVCSDialogFragment.Builder()
            .setTitle(getString(R.string.payment_failure_title), R.style.Label_Large)
            .setSubtitle(getString(R.string.payment_failure_subtitle), Gravity.CENTER)
            .addButton(getString(R.string.payment_failure_reactivate), {
                //TODO: reactivate
            }, R.style.ButtonK_Blue)
            .addButton(getString(R.string.payment_failure_remain), { fragment ->
                fragment.dismiss()
                showRemainDialog()
            }, R.style.ButtonK_BlueOutline)
            .addButton(getString(R.string.payment_failure_explore), {
                NavigationUtils.jumpTo(this, PlansActivity::class.java)
            }, R.style.Label_Medium)
            .show(supportFragmentManager)
    }

    private fun showRemainDialog() {
        EVCSSliderDialogFragment.Builder()
            .setTitle(getString(R.string.success_dialog_title))
            .setSubtitle(getString(R.string.payment_failure_remain_dialog_subtitle), Gravity.CENTER)
            .addButton("Done") { fragment -> fragment.dismiss() }
            .show(supportFragmentManager)
    }

    //Needed because if there's a scrollview the menu can be shown above the keyboard instead of below
    fun attachKeyboardListener() {
        val view = window.decorView.rootView
        KeyboardListener.attach(view) { isShown -> menuView.isVisible = !isShown }
    }

    fun detachKeyboardListener() {
        menuView.isVisible = true
        KeyboardListener.detach(window.decorView.rootView)
    }

    override fun onResume() {
        super.onResume()
        if (UserUtils.getLoggedUser() == null) return
        if (!(UserUtils.getLoggedUser().isPhoneVerified)) {
            val intent = Intent(this, VerifyPhoneActivity::class.java)
            intent.putExtra(Extras.VerifyActivity.USE_CASE, VerifyPhoneActivity.UseCase.OUR_REQUEST)
            startForResult.launch(intent)
        }
        RefreshTokenHelper.onResume()
    }
}