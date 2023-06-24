package org.evcs.android.features.profile.plans

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils.setMargins

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
        } else if (UserUtils.getLoggedUser()?.hasAnySubscription ?: false) {
            val paragraph1 = "This will end your %1\$s subscription to the %2\$s plan."
            val paragraph2 = "Your subscription will stay active through the remainder of your last billing cycle and end on %s. After that, you will have to pay after each charge session. You will still have the same member pricing."
            val paragraph3 = "Do you want to continue changing plans?"
            val paragraph4 = "This will end your %1\$ %2\$s plan. It will stay active until %3\$s."
            val paragraph5 = "Your new %1\$s plan will start on %2\$s. You will be billed $%.2f per month until you cancel."
            val paragraph6 = "Your %s plan will start immediately.\n\nYou’ll receive a free %d days starting from today.\n\nAfter that, you will automatically be billed $%.2f per month until you cancel."
            EVCSDialogFragment.Builder()
                .setTitle("Are you sure you want to change plans?")
                .setSubtitle(paragraph1 + "\n\n" + paragraph2 + "\n\n" + paragraph3)
                .addButton(getString(R.string.app_continue), {
                        dialog -> dialog.dismiss()
                        gotoPlan(plan, true)
                    }, R.drawable.layout_corners_rounded_blue)
                .showCancel(true)
                .show(childFragmentManager)
        } else {
            gotoPlan(plan, false)
        }
    }

    fun gotoPlan(plan: Plan, hasPlan: Boolean) {
        val intent = Intent(context, GetPlanActivity::class.java)
        intent.putExtra(Extras.PlanActivity.PLAN, plan)
        intent.putExtra(Extras.PlanActivity.HAS_PLAN, hasPlan)
        mPlanLauncher.launch(intent)
    }

    override fun onLearnMoreClicked(plan: Plan?) {
        val intent = Intent(context, PlanLearnMoreActivity::class.java)
        if (plan != null) intent.putExtra(Extras.PlanActivity.PLAN, plan)
        requireContext().startActivity(intent)
    }

    fun showPlans(response: List<Plan>) {
        response.forEach { plan ->
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