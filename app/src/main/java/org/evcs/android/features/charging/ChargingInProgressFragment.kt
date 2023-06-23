package org.evcs.android.features.charging

import android.view.View
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentChargingInProgressBinding
import org.evcs.android.model.Session
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.joda.time.format.DateTimeFormat

class ChargingInProgressFragment : ErrorFragment<ChargingInProgressPresenter>(),
    ChargingInProgressView {

    private var mSessionId: Int = 0
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
//        mBinding.chargingInProgressLastUpdate.setOnClickListener { refresh() }
        mBinding.chargingInProgressStopSession.setOnClickListener {
            showProgressDialog()
            presenter?.stopSession(mSessionId)
        }
    }

    override fun onChargeRetrieved(response: Session?) {
        if (response == null) {
            sessionStopped()
            return
        }
        mSessionId = response.id
        hideProgressDialog()
        mBinding.chargingInProgressEnergy.text = response.printKwh()
        val formatter = DateTimeFormat.forPattern("HH:mm:ss").withZoneUTC()
        mBinding.chargingInProgressSessionTime.text = formatter.print(response.duration.toLong())
//        mBinding.chargingInProgressStatus.text = response.status
//        mBinding.chargingInProgressSiteId.text = "Station ID: " + response.stationName
//        mBinding.chargingInProgressLastUpdate.text =
//                "Last update: " + formatter.print(response.updatedAt) + " (tap to refresh)"
//        mBinding.chargingInProgressStatusIcon
//                .startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.animation_blink))
//        mBinding.chargingInProgressStatusIcon.visibility = if (response.isCharging) View.VISIBLE else View.GONE
        mBinding.chargingInProgressStopSession.visibility = if (response.isCharging) View.VISIBLE else View.GONE
    }

    override fun sessionStopped() {
        hideProgressDialog()
        ToastUtils.show("Session finished")
        ChargingNavigationController.getInstance().onSessionFinished(mSessionId)
    }
}