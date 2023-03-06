package org.evcs.android.features.charging

import android.view.View
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.R
import org.evcs.android.EVCSApplication
import org.evcs.android.databinding.FragmentChargingInProgressBinding
import org.evcs.android.model.Charge

class ChargingInProgressFragment : ErrorFragment<ChargingInProgressPresenter?>(),
    ChargingInProgressView {

    private lateinit var mCharge: Charge
    private lateinit var mBinding: FragmentChargingInProgressBinding

    override fun layout(): Int {
        return R.layout.fragment_charging_in_progress
    }

    override fun createPresenter(): ChargingInProgressPresenter {
        return ChargingInProgressPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentChargingInProgressBinding.bind(v)
    }

    override fun init() {}

    override fun populate() {
        mCharge = Charge();
        mBinding.chargingInProgressEnergy.text = String.format("%.3f kWh", 0.0f)
        mBinding.chargingInProgressSessionTime.text = mCharge.printableDuration
        mBinding.chargingInProgressStatus.text = "Charging"
        mBinding.chargingInProgressSiteId.text = "Station ID: " + mCharge.locationId.toString()
    }
}