package org.evcs.android.features.profile.plans

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.base.core.presenter.BasePresenter
import com.base.core.util.NavigationUtils
import org.evcs.android.BaseConfiguration
import org.evcs.android.R
import org.evcs.android.activity.NavGraphActivity
import org.evcs.android.databinding.FragmentPlanLearnMoreBinding
import org.evcs.android.features.auth.initialScreen.AuthActivity
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.model.Plan
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils
import org.evcs.android.util.ViewUtils.setParentVisibility
import org.evcs.android.util.ViewUtils.showOrHide

class PlanLearnMoreFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mWalletLauncher: ActivityResultLauncher<Intent>
    private var mPlan: Plan? = null
    private lateinit var mBinding: FragmentPlanLearnMoreBinding

    companion object {
        fun newInstance(plan: Plan?): PlanLearnMoreFragment {
            val args = Bundle()
            args.putSerializable(Extras.PlanActivity.PLAN, plan)

            val fragment = PlanLearnMoreFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_plan_learn_more
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentPlanLearnMoreBinding.bind(v)
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun init() {
        mPlan = arguments?.getSerializable(Extras.PlanActivity.PLAN) as Plan?
        mWalletLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result -> if (result.resultCode == NavGraphActivity.RESULT_OK) {
                        populate()
                      }
        }
    }

    override fun populate() {
        if (mPlan == null)
            showPayAsYouGo()
        else {
            setGetPlanButton(mPlan!!)
            val userIdQuery : String = if (UserUtils.getLoggedUser() == null) ""
            else String.format(BaseConfiguration.WebViews.LEARN_MORE_USER_ID_QUERY, UserUtils.getUserId())
            mBinding.planLearnMoreWebview.loadUrl(
                String.format(BaseConfiguration.WebViews.LEARN_MORE_URL, mPlan!!.id) + userIdQuery
            )
        }
        setPlan(mPlan)
    }

    private fun showPayAsYouGo() {
        if (UserUtils.getLoggedUser() == null) {
            //TODO
        } else if (UserUtils.getLoggedUser().defaultPm == null) {
            setPaymentButton()
        } else {
            mBinding.planLearnMoreButton.isVisible = false
            mBinding.plansLearnMoreExplore.setParentVisibility(true)
        }
    }

    private fun setPaymentButton() {
        mBinding.planLearnMoreButton.text = "Add payment method"
        mBinding.planLearnMoreButton.setOnClickListener {
            mWalletLauncher.launch(WalletActivity.buildIntent(context, true))
        }
    }

    private fun setGetPlanButton(plan: Plan) {
        mBinding.planLearnMoreButton.setOnClickListener {
            if (UserUtils.getLoggedUser() == null) {
                val param = NavigationUtils.IntentExtra(Extras.AuthActivity.SKIP_ROOT, true)
                NavigationUtils.jumpTo(requireContext(), AuthActivity::class.java, param)
            } else {
                NavigationUtils.jumpTo(
                    requireContext(), GetPlanActivity::class.java,
                    NavigationUtils.IntentExtra(Extras.PlanActivity.PLAN, plan)
                )
            }
        }
    }

    fun setPlan(plan: Plan?) {
        val helper = PlanLearnMoreHelper.instance(requireContext(), plan,
            UserUtils.getLoggedUser()?.defaultPm != null)

        mBinding.planLearnMoreToolbar.setTitle(helper.getPlanName())

        mBinding.planLearnMoreButton.text = helper.getPlanButton()

        val currentPlanId = UserUtils.getLoggedUser()?.activeSubscription?.plan?.id
        if (currentPlanId == plan?.id) {
            mBinding.planLearnMoreButton.isEnabled = false
            mBinding.planLearnMoreButton.text = getString(R.string.plan_view_current)
        }
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.plansLearnMoreExplore.setOnClickListener { NavigationUtils.jumpTo(requireContext(), PlansActivity::class.java) }
    }

}
