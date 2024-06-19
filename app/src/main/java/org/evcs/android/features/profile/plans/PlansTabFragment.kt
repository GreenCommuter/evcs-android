package org.evcs.android.features.profile.plans

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.base.core.presenter.BasePresenter
import com.base.core.util.NavigationUtils
import org.evcs.android.R
import org.evcs.android.activity.NavGraphActivity
import org.evcs.android.databinding.FragmentPlansTabBinding
import org.evcs.android.databinding.ViewGoToMonthlyPlansBinding
import org.evcs.android.features.auth.initialScreen.AuthActivity
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.model.Plan
import org.evcs.android.model.Subscription
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils.setMargins
import org.joda.time.format.DateTimeFormat

class PlansTabFragment : ErrorFragment<BasePresenter<*>>(), PlanView.PlanViewListener {

    private var mPayAsYouGoView: PlanView? = null
    private lateinit var mPlanLauncher: ActivityResultLauncher<Intent>
    private lateinit var mWalletLauncher: ActivityResultLauncher<Intent>
    private lateinit var mLayout: LinearLayout

    companion object {
        fun newInstance(): PlansTabFragment {
            val args = Bundle()

            val fragment = PlansTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_plans_tab
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mLayout = FragmentPlansTabBinding.bind(v).fragmentPlansTabList
    }

    override fun init() {
        mPlanLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result -> if (result.resultCode == Activity.RESULT_OK) {
//                finish(result.data!!.getSerializableExtra(Extras.PlanActivity.PLAN) as Subscription)
            }
        }
        mWalletLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result -> if (result.resultCode == Activity.RESULT_OK) mPayAsYouGoView?.setPayAsYouGo()
        }
    }

    private fun finish() {
        if (activity is NavGraphActivity)
            findNavController().popBackStack()
        else
            requireActivity().finish()
    }

    override fun onGetPlanClicked(plan: Plan?) {
        //TODO: Check
        if (UserUtils.getLoggedUser() == null) {
            val param = NavigationUtils.IntentExtra(Extras.AuthActivity.SKIP_ROOT, true)
            NavigationUtils.jumpTo(requireContext(), AuthActivity::class.java, param)
        } else if (plan == null) {
            mWalletLauncher.launch(Intent(requireContext(), WalletActivity::class.java))
        } else if (plan.blocked) {
            showBlockedPlanDialog()
        } else if (UserUtils.getLoggedUser()?.hasAnySubscription ?: false) {
            showChangePlanDialog(UserUtils.getLoggedUser().activeSubscription!!, plan)
        } else {
            gotoPlan(plan)
        }
    }

    private fun showBlockedPlanDialog() {
        EVCSDialogFragment.Builder()
            .setTitle(getString(R.string.over_limit_warning_title))
            .setSubtitle("You are no longer eligible to subscribe to this plan.", Gravity.CENTER)
            .addButton("Done", { fragment -> fragment.dismiss() }, R.style.ButtonK_Blue)
            .show(childFragmentManager)
    }

    private fun showChangePlanDialog(activeSubscription: Subscription, plan: Plan) {
        val formatter = DateTimeFormat.forPattern("MMM d, yyyy")

        val isUpgrade = activeSubscription.plan.price < plan.price

//        val paragraph1 = "This will end your %1\$s subscription to the %2\$s plan."
//        val paragraph2 = "Your subscription will stay active through the remainder of your last billing cycle and end on %s. After that, you will have to pay after each charge session. You will still have the same member pricing."
        val paragraph3 = getString(R.string.plans_tab_dialog_subtitle_3)
        val paragraph4 = String.format(getString(R.string.plans_tab_dialog_subtitle_4),
            activeSubscription.plan.renewalPeriod.toAdverb(),
            activeSubscription.plan.name,
            formatter.print(activeSubscription.renewalDate))
        val paragraph8 = String.format(getString(R.string.plans_tab_dialog_subtitle_8),
            plan.price,
            plan.renewalPeriod.toString())
        val paragraph5 = String.format(getString(R.string.plans_tab_dialog_subtitle_5),
            plan.name,
            formatter.print(activeSubscription.renewalDate.plusDays(1))) + " " + paragraph8
        val paragraph7 = String.format(getString(R.string.plans_tab_dialog_subtitle_7), plan.name)
//        val paragraph6 = "$paragraph7\n\nYou’ll receive a free %d days starting from today.\n\nAfter that, you will automatically be billed $%.2f per month until you cancel."

        val subtitle = if (isUpgrade) paragraph7 + "\n\n" + paragraph8 + "\n\n" + paragraph3
                       else paragraph4 + "\n\n" + paragraph5 + "\n\n" + paragraph3

        EVCSDialogFragment.Builder()
            .setTitle(getString(R.string.plans_tab_dialog_title))
            .setSubtitle(subtitle)
            .addButton(getString(R.string.app_continue), { dialog ->
                dialog.dismiss()
                gotoPlan(plan)
            }, R.style.ButtonK_Blue)
            .showCancel(true)
            .show(childFragmentManager)
    }

    fun gotoPlan(plan: Plan) {
        val intent = Intent(context, GetPlanActivity::class.java)
        intent.putExtra(Extras.PlanActivity.PLAN, plan)
        mPlanLauncher.launch(intent)
    }

    override fun onLearnMoreClicked(plan: Plan?) {
        val intent = Intent(context, PlanLearnMoreActivity::class.java)
        if (plan != null) intent.putExtra(Extras.PlanActivity.PLAN, plan)
        requireContext().startActivity(intent)
    }

    fun showPlans(response: List<Plan>) {
        response.forEach { plan ->
            if (context == null) return
            val view = PlanView(requireContext(), plan)
            view.setListener(this)
            mLayout.addView(view)
        }
    }

    fun addGoToMonthlyView() {
        val goToMonthlyPlansView = ViewGoToMonthlyPlansBinding.inflate(LayoutInflater.from(context)).root
        goToMonthlyPlansView.layoutParams = LinearLayout.LayoutParams(-1, -2)
        val margin = resources.getDimension(R.dimen.padding_ariel_standard).toInt()
        goToMonthlyPlansView.setMargins(margin, margin, margin, margin)

        goToMonthlyPlansView.setOnClickListener {
            if (activity is NavGraphActivity) {
                findNavController().navigate(PlansFragmentDirections.actionPlansFragmentSelf(false))
            } else {
                val intent = Intent(requireContext(), PlansActivity::class.java)
                intent.putExtra(Extras.PlanActivity.IS_CORPORATE, false)
                startActivity(intent)
            }
        }
        mLayout.addView(goToMonthlyPlansView)
    }

    fun addPayAsYouGo() {
        mPayAsYouGoView = PlanView(requireContext(), null as Plan?)
        mPayAsYouGoView!!.setListener(this)
        mLayout.addView(mPayAsYouGoView)
    }
}