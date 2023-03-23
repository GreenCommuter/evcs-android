package org.evcs.android.features.charging

import android.view.View
import android.view.animation.AnimationUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentChargingInProgressBinding
import org.evcs.android.model.Session
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.joda.time.format.DateTimeFormat

class ChargingInProgressFragment : ErrorFragment<ChargingInProgressPresenter>(),
    ChargingInProgressView {

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
        val session = arguments?.getSerializable(Extras.StartCharging.SESSION) as Session?
        if (session == null) {
            refresh()
        } else {
            onChargeRetrieved(session)
        }
    }

    fun refresh() {
        showProgressDialog()
        presenter?.getCurrentCharge()
    }

    override fun setListeners() {
        mBinding.chargingInProgressLastUpdate.setOnClickListener { refresh() }
    }

    override fun onChargeRetrieved(response: Session?) {
        if (response == null) return
        hideProgressDialog()
        mBinding.chargingInProgressEnergy.text = String.format("%.3f kWh", response.kwh)
        mBinding.chargingInProgressSessionTime.text = response.printableDuration
        mBinding.chargingInProgressStatus.text = "Charging"
        mBinding.chargingInProgressSiteId.text = "Station ID: " + response.stationName.toString()
        val formatter = DateTimeFormat.forPattern("hh:mm:ss")
        mBinding.chargingInProgressLastUpdate.text =
                "Last update: " + formatter.print(response.updatedAt) + " (tap to refresh)"
        mBinding.chargingInProgressStatusIcon
                .startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.animation_blink))
    }
}