package org.evcs.android.features.subscriptions

import android.view.View
import android.widget.FrameLayout
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.FragmentCancelPlanBaseBinding
import org.evcs.android.model.Subscription
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.UserUtils

abstract class AbstractCancelPlanFragment : ErrorFragment<BasePresenter<*>>() {
    private lateinit var mBinding: FragmentCancelPlanBaseBinding

    override fun layout(): Int {
        return R.layout.fragment_cancel_plan_base
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun init() {
        mBinding.cancelPlanChildLayout.addView(getChildLayout(mBinding.cancelPlanChildLayout))
    }

    override fun setUi(v: View) {
        mBinding = FragmentCancelPlanBaseBinding.bind(v)
    }

    override fun populate() {
        setPlan(UserUtils.getLoggedUser().activeSubscription!!)
    }

    abstract fun setPlan(subscription: Subscription)

    override fun setListeners() {
        super.setListeners()
        mBinding.cancelPlanImage.setImageURI(UserUtils.getLoggedUser().activeSubscription?.plan?.iconUrl)
        mBinding.cancelPlanBold.text = getBoldText()

        mBinding.cancelPlanContinue.setOnClickListener { onContinueClicked() }
        mBinding.cancelPlanCancel.setOnClickListener { requireActivity().finish() }
        mBinding.cancelPlanCancel.text = getCancelText()
        mBinding.cancelPlanTrialToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        mBinding.cancelPlanConfirmation.text = getConfirmationText()
    }

    abstract fun getConfirmationText(): String

    abstract fun onContinueClicked()

    abstract fun getCancelText(): String

    abstract fun getChildLayout(parent: FrameLayout): View

    abstract fun getBoldText(): String

}