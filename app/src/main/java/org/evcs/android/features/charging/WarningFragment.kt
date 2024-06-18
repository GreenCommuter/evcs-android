package org.evcs.android.features.charging

import android.view.View
import androidx.navigation.fragment.findNavController
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.FragmentOverLimitWarningBinding
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import java.util.ArrayList

/**
 * Shows a warning that needs to be accepted before charging
 */
abstract class WarningFragment<T> : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mBinding: FragmentOverLimitWarningBinding
    private val mListener = ChargingNavigationController.getInstance()

    override fun layout(): Int {
        return R.layout.fragment_over_limit_warning
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentOverLimitWarningBinding.bind(v)
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun init() {}

    override fun populate() {
        mBinding.overLimitWarningCheckbox.setDescription(getString(R.string.over_limit_warning_checkbox))
        mBinding.overLimitWarningTitle.text = getTitle()
        mBinding.overLimitWarningDescription.text = getDescription()
    }

    abstract fun getTitle(): CharSequence

    abstract fun getDescription(): CharSequence

    override fun setListeners() {
        mBinding.overLimitWarningCheckbox.setOnCheckedClickListener { _, isChecked ->
            mBinding.overLimitWarningContinue.isEnabled = isChecked
        }
        mBinding.overLimitWarningCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        mBinding.overLimitWarningContinue.setOnClickListener {
            PlanInfoFragment.showStartChargingDialog(requireContext(), childFragmentManager) { goToStartCharging() }
        }
    }

    private fun goToStartCharging() {
        mListener.goToStartCharging(
            requireArguments().getInt(Extras.StartCharging.STATION_ID),
            requireArguments().getString(Extras.StartCharging.PM_ID),
            requireArguments().getSerializable(Extras.StartCharging.COUPONS) as ArrayList<String>?
        )
    }
}
