package org.evcs.android.features.charging

import android.os.Bundle
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.ui.fragment.ErrorFragment

class ChargingTabFragment : ErrorFragment<ChargingTabPresenter>(), ChargingTabView {
    companion object {
        fun newInstance(): ChargingTabFragment {
            val args = Bundle()
            val fragment = ChargingTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_charging_tab
    }

    override fun init() {
    }

    override fun createPresenter(): ChargingTabPresenter {
        return ChargingTabPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }
}
