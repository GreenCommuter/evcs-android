package org.evcs.android.features.main

import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import com.base.core.util.NavigationUtils
import com.base.core.util.NavigationUtils.IntentExtra
import com.rollbar.android.Rollbar
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.AbstractSupportedVersionActivity
import org.evcs.android.features.auth.register.VerifyPhoneActivity
import org.evcs.android.features.profile.plans.GetPlanActivity
import org.evcs.android.features.profile.plans.PlanViewHelper
import org.evcs.android.features.profile.plans.PlansActivity
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.features.shared.EVCSSliderDialogFragment
import org.evcs.android.model.Subscription
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.FontUtils
import org.evcs.android.util.PaymentUtils
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils.setMargins

/**
 * This performs all the checks that have to be made when the app restarts.
 */
class InitialDialogsFragment : ErrorFragment<InitialDialogsPresenter>(), InitialDialogsView {

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    companion object {
        fun newInstance(): InitialDialogsFragment {
            return InitialDialogsFragment()
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_base
    }

    override fun createPresenter(): InitialDialogsPresenter {
        return InitialDialogsPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result -> if (result.resultCode == AbstractSupportedVersionActivity.RESULT_OK)
            showSuccessDialog(R.string.success_dialog_subtitle_phone)
        }
    }

    override fun populate() {
        super.populate()
        val intent = requireActivity().intent
        if (intent.hasExtra(Extras.VerifyActivity.RESULT)) {
            onVerifyResult(intent.getIntExtra(Extras.VerifyActivity.RESULT,
                AbstractSupportedVersionActivity.RESULT_CANCELED
            ))
            intent.removeExtra(Extras.VerifyActivity.RESULT)
        }
        if (intent.hasExtra(Extras.PlanActivity.PLAN)) {
            showCongratulationsDialog(intent.getSerializableExtra(Extras.PlanActivity.PLAN) as Subscription)
            intent.removeExtra(Extras.PlanActivity.PLAN)
        }
        if (intent.getBooleanExtra(Extras.Root.OPENING_KEY, false)) {
            intent.removeExtra(Extras.Root.OPENING_KEY)
            onAppStarted()
        }
    }

    fun onAppStarted() {
        presenter.checkPaymentIssues()
    }

    override fun onResume() {
        super.onResume()
        if (UserUtils.getLoggedUser() == null) return
        if (!(UserUtils.getLoggedUser().isPhoneVerified)) {
            val intent = Intent(requireContext(), VerifyPhoneActivity::class.java)
            intent.putExtra(Extras.VerifyActivity.USE_CASE, VerifyPhoneActivity.UseCase.OUR_REQUEST)
            startForResult.launch(intent)
        }
        //presenter.checkTokenExpiration()
    }

    fun showWelcomeDialog() {
        EVCSSliderDialogFragment.Builder()
            .setTitle(getString(R.string.welcome_dialog_title), R.style.Label_Large)
            .setSubtitle(getString(R.string.welcome_dialog_subtitle))
            .addButton(getString(R.string.app_close)) { fragment -> fragment.dismiss() }
            .show(childFragmentManager)
    }

    fun showSuccessDialog(@StringRes subtitle : Int = R.string.success_dialog_subtitle) {
        val textView = TextView(requireContext())
        textView.text = getString(R.string.success_dialog_cta)
        textView.setTextAppearance(requireContext(), R.style.Label_Medium)
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
                NavigationUtils.jumpTo(requireContext(), PlansActivity::class.java)
            }
        }
        dialogBuilder.show(childFragmentManager)
    }

    fun showCongratulationsDialog(subscription: Subscription) {
        val secondLine = if (subscription.onTrialPeriod) getString(R.string.congratulations_dialog_subtitle_3)
        else PlanViewHelper.instance(requireContext(), subscription.plan).getCongratulationsDialogSubtitle()
        val planName = if (subscription.onTrialPeriod) "Free Trial" else subscription.planName + " Plan"
        EVCSSliderDialogFragment.Builder()
            .setTitle(getString(R.string.congratulations_dialog_title), R.style.Label_Large)
            .setSubtitle(getString(R.string.congratulations_dialog_subtitle, planName, secondLine))
            .addButton(getString(R.string.app_close)) { fragment -> fragment.dismiss() }
            .show(childFragmentManager)
    }

    fun showAccountSuspendedDialog() {
        val subtitle = FontUtils.getSpannable(
            resources.getStringArray(R.array.account_suspended_subtitle), Color.BLACK)

        EVCSDialogFragment.Builder()
            .setTitle(getString(R.string.account_suspended_title))
            .setSubtitle(subtitle, Gravity.CENTER)
            .addButton(getString(R.string.account_suspended_button), {
                    fragment -> fragment.dismiss()
                PaymentUtils.goToPendingPayment(requireContext())
            },
                R.style.ButtonK_Blue)
            .showCancel(getString(R.string.app_close))
            //addButton(getString(R.string.app_close), { fragment -> fragment.dismiss() }, R.style.ButtonK_BlackOutline)
            .show(childFragmentManager)
    }

    fun onPendingCancelation(previousSubscription: Subscription) {
        try {
            showPaymentFailureDialog(previousSubscription)
        } catch (e : Exception) {
            Rollbar.instance().error(e)
        }
    }

    override fun onPaymentIssuesResponse(response: InitialDialogsPresenter.PaymentIssue,
        previousSubscription: Subscription?) {
            if (response == InitialDialogsPresenter.PaymentIssue.PENDING_CANCELATION) {
                onPendingCancelation(previousSubscription!!)
            } else if (response == InitialDialogsPresenter.PaymentIssue.ACCOUNT_SUSPENDED) {
                showAccountSuspendedDialog()
            }
    }

    override fun onConfirmCancelation() {
        showRemainDialog()
    }

    fun showPaymentFailureDialog(previousSubscription: Subscription) {
        EVCSDialogFragment.Builder()
            .setTitle(getString(R.string.payment_failure_title), R.style.Label_Large)
            .setSubtitle(getString(R.string.payment_failure_subtitle), Gravity.CENTER)
            .setCancelable(false)
            .addButton(getString(R.string.payment_failure_reactivate), {
                val intentExtra = IntentExtra(Extras.PlanActivity.PLAN, previousSubscription.plan)
                NavigationUtils.jumpTo(requireContext(), GetPlanActivity::class.java, intentExtra)
            }, R.style.ButtonK_Blue)
            .addButton(getString(R.string.payment_failure_remain), { fragment ->
                fragment.dismiss()
                presenter?.confirmCancelation(previousSubscription.id)
            }, R.style.ButtonK_BlueOutline)
            .addButton(getString(R.string.payment_failure_explore), {
                NavigationUtils.jumpTo(requireContext(), PlansActivity::class.java)
            }, R.style.Label_Medium)
            .show(childFragmentManager)
    }

    private fun showRemainDialog() {
        EVCSSliderDialogFragment.Builder()
            .setTitle(getString(R.string.success_dialog_title))
            .setSubtitle(getString(R.string.payment_failure_remain_dialog_subtitle), Gravity.CENTER)
            .addButton("Done") { fragment -> fragment.dismiss() }
            .show(childFragmentManager)
    }

    fun onVerifyResult(verifyResult: Int) {
        //updateProfileAlert()
        if (verifyResult == AbstractSupportedVersionActivity.RESULT_OK) {
            showSuccessDialog()
        } else {
//            showAccountNotValidatedDialog()
            showSuccessDialog()
        }
    }

}
