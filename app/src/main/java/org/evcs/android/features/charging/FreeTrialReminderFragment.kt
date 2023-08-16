package org.evcs.android.features.charging

import android.view.View
import org.evcs.android.ui.fragment.ErrorFragment
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.FragmentFreeTrialReminderBinding

class FreeTrialReminderFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mBinding: FragmentFreeTrialReminderBinding

    override fun layout(): Int {
        return R.layout.fragment_free_trial_reminder
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentFreeTrialReminderBinding.bind(v)
    }

    override fun init() {}

    override fun populate() {
        super.populate()
        mBinding.freeTrialReminderText1.text
        mBinding.freeTrialReminderText2.text
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.freeTrialReminderButton.setOnClickListener {  }
    }
}