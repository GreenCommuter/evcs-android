package org.evcs.android.features.profile.plans

import android.view.View
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.ui.fragment.ErrorFragment

class PlanLearnMoreFragment : ErrorFragment<BasePresenter<*>>() {

    override fun layout(): Int {
        return R.layout.fragment_plan_learn_more
    }

    override fun setUi(v: View) {
        super.setUi(v)
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun init() {
    }

}
