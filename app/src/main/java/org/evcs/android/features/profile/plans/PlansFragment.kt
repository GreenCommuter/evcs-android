package org.evcs.android.features.profile.plans

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.base.core.adapter.viewpager.BaseFragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.NavGraphActivity
import org.evcs.android.activity.WebViewActivity
import org.evcs.android.databinding.FragmentPlansBinding
import org.evcs.android.model.PlanTab
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.view.shared.EVCSToolbar2
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils

class PlansFragment : ErrorFragment<PlansPresenter>(), PlansView {

    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private lateinit var mTabLayoutDivider: View
    private lateinit var mPagerAdapter: BaseFragmentStatePagerAdapter
    private lateinit var mToolbar: EVCSToolbar2
    private var mShowCorporatePlans: Boolean = false

    companion object {
        fun newInstance(showCorporatePlans: Boolean = false): PlansFragment {
            val args = Bundle()
            args.putBoolean(Extras.PlanActivity.IS_CORPORATE, showCorporatePlans)

            val fragment = PlansFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_plans
    }

    override fun createPresenter(): PlansPresenter {
        return PlansPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentPlansBinding.bind(v)
        mTabLayout = binding.fragmentPlansTabLayout
        mTabLayoutDivider = binding.fragmentPlansTabLayoutDivider
        mViewPager = binding.fragmentPlansViewPager
        mToolbar = binding.fragmentPlansToolbar
    }

    override fun init() {
        mPagerAdapter = BaseFragmentStatePagerAdapter(childFragmentManager)
        mPagerAdapter = BaseFragmentStatePagerAdapter(childFragmentManager)
        mViewPager.adapter = mPagerAdapter

        mShowCorporatePlans = arguments?.getBoolean(Extras.PlanActivity.IS_CORPORATE, true) ?: true
                && UserUtils.getLoggedUser()?.isCorporateUser ?: false
        if (mShowCorporatePlans)
            mToolbar.setTitle("Hertz-EVCS Plans")

        showProgressDialog()
        presenter.getPlans()
    }

    fun showTabs() {
        mTabLayout.visibility = View.VISIBLE
        mTabLayoutDivider.visibility = View.VISIBLE
        mTabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.evcs_secondary_700))
        mTabLayout.setupWithViewPager(mViewPager)
    }

    private fun getTab(title: String?): TextView {
        val tab = TextView(context)
        tab.setTextAppearance(requireContext(), R.style.Title_Medium)
        tab.setTextColor(resources.getColorStateList(R.color.button_text_color_selector_tab))
        tab.gravity = Gravity.CENTER
        tab.isAllCaps = false
        tab.text = title
        return tab
    }

    override fun showPlans(response: ArrayList<PlanTab>) {
        hideProgressDialog()
        if (response.size > 1) {
            showTabs()
        }

        response.forEach { tab ->
            val fragment = PlansTabFragment.newInstance(tab.plans!!)
            mPagerAdapter.addItem(fragment, tab.title)
            mTabLayout.getTabAt(mTabLayout.tabCount - 1)?.customView = getTab(tab.title)
        }

        val standardMileageFragment = mPagerAdapter.getItem(0) as PlansTabFragment
        if (mShowCorporatePlans) {
            standardMileageFragment.addGoToMonthlyView()
        } else {
            if (UserUtils.getLoggedUser()?.activeSubscription == null) {
                standardMileageFragment.addPayAsYouGo()
            }
        }

        mViewPager.offscreenPageLimit = mPagerAdapter.count

        val currentPlanId = UserUtils.getLoggedUser()?.activeSubscription?.plan?.id
        val currentPlanTab = response.firstOrNull { tab -> tab.plans!!.map { plan -> plan.id }.contains(currentPlanId) }
        mViewPager.currentItem = response.indexOf(currentPlanTab)

    }

    override fun userHasHiddenPlan(url: String) {
        val intent = WebViewActivity.buildIntent(requireContext(), "Plans", url, "")
        startActivity(intent)
        if (activity is NavGraphActivity) findNavController().popBackStack()
        else requireActivity().finish()
    }

}