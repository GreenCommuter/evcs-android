package org.evcs.android.features.profile.plans

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.FragmentPlansTabBinding
import org.evcs.android.ui.fragment.ErrorFragment

class PlansTabFragment : ErrorFragment<BasePresenter<*>>() {

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
        //TODO: bring from API and populate
        mLayout.addView(PlanView(requireContext()))
        mLayout.addView(PlanView(requireContext()))
        mLayout.addView(PlanView(requireContext()))
    }
}