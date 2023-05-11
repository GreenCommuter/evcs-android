package org.evcs.android.features.profile.plans

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.base.core.presenter.BasePresenter
import com.base.core.util.NavigationUtils
import org.evcs.android.R
import org.evcs.android.databinding.FragmentPlansTabBinding
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.model.Plan
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras

class PlansTabFragment : ErrorFragment<BasePresenter<*>>(), PlanView.PlanViewListener {

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

    override fun init() {}

    //TODO: bring from API
    override fun populate() {
        Plan.values().forEach { plan ->
            val view = PlanView(requireContext(), plan)
            view.setListener(this)
            mLayout.addView(view)
        }
    }

    override fun onGetPlanClicked(plan: Plan) {
        if (true /*tiene plan*/) {
            val paragraph1 = "This will end your monthly subscription to the %s plan"
            val paragraph2 = "Your subscription will stay active through the remainder of your last billing cycle and end on %s. After that, you will have to pay after each charge session. You will still have the same member pricing"
            val paragraph3 = "Do you want to continue changing plans?"
            val paragraph4 = "This will end your monthly %1\$s plan. It will stay active until %2\$s."
            val paragraph5 = "Your new %1\$s plan will start on %2\$s. You will be billed $%.2f per month until you cancel."
            val paragraph6 = "Your %s plan will start immediately.\n\nYou’ll receive a free %d days starting from today.\n\nAfter that, you will automatically be billed $%.2f per month until you cancel."
            EVCSDialogFragment.Builder()
                .setTitle("Are you sure you want to change plans?")
                .setSubtitle(paragraph1 + "\n\n" + paragraph2 + "\n\n" + paragraph3)
                .addButton("Continue") {
                        dialog -> dialog.dismiss()
                    gotoPlan(plan)
                }
                .showCancel(true)
                .show(childFragmentManager)
        } else {
            gotoPlan(plan)
        }
    }

    fun gotoPlan(plan: Plan) {
        NavigationUtils.jumpTo(requireContext(), GetPlanActivity::class.java,
            NavigationUtils.IntentExtra(Extras.PlanActivity.PLAN, plan))
    }

    override fun onLearnMoreClicked(plan: Plan) {
        NavigationUtils.jumpTo(requireContext(), PlanLearnMoreActivity::class.java,
            NavigationUtils.IntentExtra(Extras.PlanActivity.PLAN, plan))
    }
}