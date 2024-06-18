package org.evcs.android.features.profile.sessioninformation

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.base.core.util.NavigationUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.features.main.InitialDialogsPresenter
import org.evcs.android.features.main.InitialDialogsView
import org.evcs.android.features.profile.plans.GetPlanActivity
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.features.shared.EVCSSliderDialogFragment
import org.evcs.android.model.Subscription
import org.evcs.android.util.Extras

class ChargingSessionInformationFragment : SessionInformationFragment(), InitialDialogsView {

    private lateinit var mInitialDialogsPresenter : InitialDialogsPresenter

    override fun createPresenter(): SessionInformationPresenter {
        mInitialDialogsPresenter = InitialDialogsPresenter(this, EVCSApplication.getInstance().retrofitServices)
        return super.createPresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mInitialDialogsPresenter.onViewCreated()
    }

    override fun init() {
        super.init()
        showProgressDialog()
        progressDialog.setCancelable(true)
        presenter?.getChargeFromSession(
                requireArguments().getInt(Extras.SessionInformationActivity.CHARGE_ID))
        mInitialDialogsPresenter.checkPendingCancelation()
    }

    override fun populate() {
    }

    override fun onBackPressed(): Boolean {
        requireActivity().finish()
        return true
    }

    override fun onPendingCancelation(previousSubscription: Subscription) {
        EVCSDialogFragment.Builder()
            .setTitle(getString(R.string.payment_failure_title))
            .setSubtitle(getString(R.string.payment_failure_subtitle_variant), Gravity.CENTER)
            .addButton(("Activate Plan"), {
                val intentExtra =
                    NavigationUtils.IntentExtra(Extras.PlanActivity.PLAN, previousSubscription.plan)
                NavigationUtils.jumpTo(requireContext(), GetPlanActivity::class.java, intentExtra)
            }, R.style.ButtonK_Blue)
            .addButton(getString(R.string.payment_failure_remain), { fragment ->
                fragment.dismiss()
                mInitialDialogsPresenter?.confirmCancelation(previousSubscription.id)
            }, R.style.ButtonK_BlueOutline)
            .setCancelable(false)
            .show(childFragmentManager)
    }

    override fun onConfirmCancelation() {
        EVCSSliderDialogFragment.Builder()
            .setTitle(getString(R.string.success_dialog_title))
            .setSubtitle(getString(R.string.payment_failure_remain_dialog_subtitle), Gravity.CENTER)
            .addButton("Done") { fragment -> fragment.dismiss() }
            .show(childFragmentManager)
    }
}
