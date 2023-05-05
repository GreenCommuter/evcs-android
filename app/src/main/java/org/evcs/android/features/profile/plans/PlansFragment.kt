package org.evcs.android.features.profile.plans

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.base.core.adapter.viewpager.BaseFragmentStatePagerAdapter
import com.base.core.presenter.BasePresenter
import com.google.android.material.tabs.TabLayout
import org.evcs.android.R
import org.evcs.android.databinding.FragmentPlansBinding
import org.evcs.android.features.map.setStatusBarColor
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.view.shared.EVCSToolbar2

class PlansFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mToolbar: EVCSToolbar2
    private lateinit var mStandardMileageFragment: PlansTabFragment
    private var mHighMileageFragment: PlansTabFragment? = null
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private lateinit var mPagerAdapter: BaseFragmentStatePagerAdapter

    override fun layout(): Int {
        return R.layout.fragment_plans
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentPlansBinding.bind(v)
        mTabLayout = binding.fragmentPlansTabLayout
        mViewPager = binding.fragmentPlansViewPager
        mToolbar = binding.fragmentPlansToolbar2
    }

    override fun init() {
        val tabStandard = "Standard Mileage"
        val tabHigh = "High Mileage"

        mPagerAdapter = BaseFragmentStatePagerAdapter(childFragmentManager)
        if (mHighMileageFragment == null) {
            mHighMileageFragment =
                PlansTabFragment.newInstance()//.setListener(mFragmentListener)
//                    .setParent(this)
            mStandardMileageFragment =
                PlansTabFragment.newInstance()//.setListener(mFragmentListener)
//            mCorporateTripsFragment.setParent(this)
        }
        mPagerAdapter = BaseFragmentStatePagerAdapter(childFragmentManager)
        mPagerAdapter.addItem(mStandardMileageFragment, tabStandard)
        mPagerAdapter.addItem(mHighMileageFragment!!, tabHigh)

        mTabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.evcs_secondary_700))
        mViewPager.adapter = mPagerAdapter
        mViewPager.offscreenPageLimit = mPagerAdapter.count
        mTabLayout.setupWithViewPager(mViewPager)
        mTabLayout.getTabAt(0)?.customView = getTab(tabStandard)
        mTabLayout.getTabAt(1)?.customView = getTab(tabHigh)
        mViewPager.currentItem = 0

        setStatusBarColor(Color.TRANSPARENT)
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

}