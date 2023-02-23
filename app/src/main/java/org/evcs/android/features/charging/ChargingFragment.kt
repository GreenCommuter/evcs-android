package org.evcs.android.features.charging

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.base.core.adapter.viewpager.BaseFragmentStatePagerAdapter
import com.base.core.presenter.BasePresenter
import com.google.android.material.tabs.TabLayout
import org.evcs.android.R
import org.evcs.android.databinding.FragmentChargingBinding
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.ui.fragment.ErrorFragment

class ChargingFragment : ErrorFragment<BasePresenter<*>>() {

    val mNavigationListener = MainNavigationController.getInstance()
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager
    private lateinit var mPagerAdapter: BaseFragmentStatePagerAdapter


    fun newInstance(): ChargingFragment {
        return ChargingFragment()
    }

    override fun layout(): Int {
        return R.layout.fragment_charging
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun setUi(v: View) {
        val binding = FragmentChargingBinding.bind(v)
        mTabLayout = binding.fragmentChargingTabLayout
        mViewPager = binding.fragmentHomeViewPager
        super.setUi(v)
    }

    override fun init() {
        mPagerAdapter = BaseFragmentStatePagerAdapter(childFragmentManager)
        mViewPager.adapter = mPagerAdapter
        val chargingHistoryFragment = ChargingHistoryFragment.newInstance()//.setListener(this)
        val chargingTabFragment = ChargingTabFragment.newInstance()//.setListener(this)
        mPagerAdapter.addItem(chargingTabFragment, "Charging")
        mPagerAdapter.addItem(chargingHistoryFragment, "Charging History")
        mTabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.evcs_orange))
        mTabLayout.setTabTextColors(
            resources.getColor(R.color.evcs_dark_grey),
            resources.getColor(R.color.evcs_gray_89)
        )
        mViewPager.offscreenPageLimit = mPagerAdapter.count
        mTabLayout.setupWithViewPager(mViewPager)
//        val vanpoolTab = CustomTab(context, resources.getString(R.string.drawer_menu_vanpooling),
//            R.drawable.vanpool_selector, CustomTab.Type.MYTRIPS)
//        val carshareTab = CustomTab(context, resources.getString(R.string.drawer_menu_car_sharing),
//            R.drawable.carshare_selector, CustomTab.Type.MYTRIPS)
//        mTabLayout.getTabAt(Configuration.MyTrips.VANPOOL_TAB).setCustomView(vanpoolTab)
//        mTabLayout.getTabAt(Configuration.MyTrips.CARSHARE_TAB).setCustomView(carshareTab)
//        mViewPager.currentItem = Configuration.MyTrips.CARSHARE_TAB
    }

    override fun onBackPressed(): Boolean {
        mNavigationListener.onMapClicked()
        return true
    }

}