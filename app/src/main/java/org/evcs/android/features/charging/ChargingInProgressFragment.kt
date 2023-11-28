package org.evcs.android.features.charging

import android.os.Handler
import android.view.View
import androidx.core.view.isVisible
import com.base.core.util.ToastUtils
import com.rollbar.android.Rollbar
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentChargingInProgressBinding
import org.evcs.android.model.Location
import org.evcs.android.model.Session
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.VideoUtils.setVideoResource
import org.evcs.android.util.VideoUtils.startAndLoop
import org.evcs.android.util.ViewUtils.showOrHide
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class ChargingInProgressFragment : ErrorFragment<ChargingInProgressPresenter>(),
    ChargingInProgressView {

    private val REFRESH_INTERVAL_SECONDS = 30

    private lateinit var formatter: DateTimeFormatter
    private lateinit var mSession: Session
    private lateinit var mBinding: FragmentChargingInProgressBinding
    private var mLastUpdate: DateTime? = null
    private var mDuration: Long? = null

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

    override fun init() {
        mBinding.chargingInProgressAnim.setVideoResource(R.raw.evcs_scene3, requireContext())
        mBinding.chargingInProgressAnim.startAndLoop()
    }

    override fun populate() {
        formatter = DateTimeFormat.forPattern("HH:mm:ss").withZoneUTC()

        val session = arguments?.getSerializable(Extras.StartCharging.SESSION) as Session?
        if (session == null) {
            refresh()
        } else {
            onChargeRetrieved(session)
        }
    }

    fun startTimerUpdates() {
        Handler().postDelayed(mRunnable, 1000)
    }

    val mRunnable = object : Runnable {
        override fun run() {
            setDuration(mDuration!!.plus(1000))
            if (mLastUpdate?.plusSeconds(REFRESH_INTERVAL_SECONDS)?.isBeforeNow == true) {
                presenter.getCurrentCharge()
            }
            if (isVisible) {
                Handler().postDelayed(this, 1000)
            } else {
                mDuration = null
            }
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
            presenter?.stopSession(mSession.id)
        }
    }

    override fun onChargeRetrieved(response: Session?) {
        if (response == null) {
            sessionStopped()
            return
        }
        mLastUpdate = DateTime()
        mSession = response
        if (mSession.address == null)
            Rollbar.instance().warning("Session with address null " + mSession.id.toString())
//            presenter.getStation(response.stationName)
        else {
            mBinding.chargingInProgressSiteName.text = mSession.address.toString()
            mBinding.chargingInProgressLocationLoading.isVisible = false
        }
        mBinding.chargingInProgressEnergy.text = response.printKwh()
        if (mDuration == null) {
            hideProgressDialog()
            setDuration(response.duration.toLong())
            startTimerUpdates()
        }
//        mBinding.chargingInProgressCost.setText(String.format("\$%.2f", mSession.ongoingRate?.sessionFeeValue))
        mBinding.chargingInProgressCost.setText(mSession.ongoingRate?.sessionFeeValue)
        mBinding.chargingInProgressCost.setLabel(mSession.ongoingRate?.sessionFeeLabel?:"")
        mBinding.chargingInProgressRate.setLabel((mSession.ongoingRate?.rateLabel?:"") + "*")
//        mBinding.chargingInProgressRate.setText(String.format("\$%.2f/kWh", mSession.ongoingRate?.rateValue))
        mBinding.chargingInProgressRate.setText(String.format("%s/kWh", mSession.ongoingRate?.rateValue))
        if (mSession.ongoingRate?.optionalExplanation != null) {
            mBinding.chargingInProgressRateExplanation.showOrHide("*" + mSession.ongoingRate?.optionalExplanation)
        }
        mBinding.chargingInProgressStopSession.visibility = if (response.isCharging) View.VISIBLE else View.GONE
    }

    override fun showChargeError(error: RequestError) {
        mLastUpdate = DateTime()
        super.showError(error)
    }

    override fun onLocationRetrieved(location: Location) {
        mSession.setLocation(location)
        mBinding.chargingInProgressSiteName.text = mSession.address.toString()
        mBinding.chargingInProgressLocationLoading.isVisible = false
    }

    private fun setDuration(duration: Long) {
        mDuration = duration
        mBinding.chargingInProgressSessionTime.text = formatter.print(duration)
    }

    override fun sessionStopped() {
        hideProgressDialog()
        ToastUtils.show("Session finished")
        if (::mSession.isInitialized) {
            ChargingNavigationController.getInstance().onSessionFinished(mSession)
        } else {
            onBackPressed()
        }
    }

    override fun onBackPressed(): Boolean {
        requireActivity().finish()
        return true
    }

}