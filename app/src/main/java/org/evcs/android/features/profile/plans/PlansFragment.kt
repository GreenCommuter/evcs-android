package org.evcs.android.features.profile.plans

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.base.core.adapter.viewpager.BaseFragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentPlansBinding
import org.evcs.android.model.Plan
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.view.shared.EVCSToolbar2

class PlansFragment : ErrorFragment<PlansPresenter>(), PlansView {

    private lateinit var mToolbar: EVCSToolbar2
    private lateinit var mStandardMileageFragment: PlansTabFragment
    private var mHighMileageFragment: PlansTabFragment? = null
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private lateinit var mTabLayoutDivider: View
    private lateinit var mPagerAdapter: BaseFragmentStatePagerAdapter

    override fun layout(): Int {
        return R.layout.fragment_plans
    }

    override fun createPresenter(): PlansPresenter {
        return PlansPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentPlansBinding.bind(v)
        mToolbar = binding.fragmentPlansToolbar2
        mTabLayout = binding.fragmentPlansTabLayout
        mTabLayoutDivider = binding.fragmentPlansTabLayoutDivider
        mViewPager = binding.fragmentPlansViewPager
    }

    override fun init() {
        val tabStandard = "Standard Mileage"

        mPagerAdapter = BaseFragmentStatePagerAdapter(childFragmentManager)
        if (mHighMileageFragment == null) {
            mHighMileageFragment =
                PlansTabFragment.newInstance()//.setListener(mFragmentListener)
//                    .setParent(this)
            mStandardMileageFragment =
                PlansTabFragment.newInstance()//.setListener(mFragmentListener)
//                    .setParent(this)
        }
        mPagerAdapter = BaseFragmentStatePagerAdapter(childFragmentManager)
        mPagerAdapter.addItem(mStandardMileageFragment, tabStandard)
        mViewPager.adapter = mPagerAdapter
        showTabs()
        mTabLayout.getTabAt(0)?.customView = getTab(tabStandard)

        showProgressDialog()
        presenter.getPlans()
    }

    fun showTabs() {
        mTabLayout.visibility = View.VISIBLE
        mTabLayoutDivider.visibility = View.VISIBLE
        val tabHigh = "High Mileage"
        mPagerAdapter.addItem(mHighMileageFragment!!, tabHigh)
        mTabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.evcs_secondary_700))
        mViewPager.offscreenPageLimit = mPagerAdapter.count
        mTabLayout.setupWithViewPager(mViewPager)
        mTabLayout.getTabAt(1)?.customView = getTab(tabHigh)
        mViewPager.currentItem = 0
    }

    override fun setListeners() {
        super.setListeners()
        mToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
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

    override fun showPlans(response: ArrayList<Plan>) {
        hideProgressDialog()
        response.forEach { plan ->
//            mLayout.addView(PlanView(requireContext(), plan))
        }
    }

}