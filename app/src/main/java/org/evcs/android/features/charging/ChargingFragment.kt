package org.evcs.android.features.charging

import android.view.View
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.ui.fragment.ErrorFragment

class ChargingFragment : ErrorFragment<BasePresenter<*>>() {

    val mNavigationListener = MainNavigationController.getInstance()

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
        super.setUi(v)
    }

    override fun init() {
    }

    override fun onBackPressed(): Boolean {
        mNavigationListener.onMapClicked()
        return true
    }

}