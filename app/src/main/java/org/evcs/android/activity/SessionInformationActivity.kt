package org.evcs.android.activity

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.EVCSApplication
import org.evcs.android.activity.location.LocationActivityPresenter
import org.evcs.android.activity.location.LocationActivityView
import org.evcs.android.databinding.ActivitySessionInformationBinding
import org.evcs.android.model.Charge
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.util.Extras
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.PeriodFormatterBuilder

class SessionInformationActivity : BaseActivity2(), LocationActivityView {

    private lateinit var mPresenter: LocationActivityPresenter
    private lateinit var mDateTimeFormatter: DateTimeFormatter
    private lateinit var mBinding: ActivitySessionInformationBinding

    override fun init() {
        mPresenter = LocationActivityPresenter(
            this, EVCSApplication.getInstance().retrofitServices)
        mPresenter.onViewCreated()
    }

    override fun inflate(layoutInflater: LayoutInflater?): View {
        mBinding = ActivitySessionInformationBinding.inflate(layoutInflater!!)
        return mBinding.root
    }

    override fun populate() {
        mDateTimeFormatter = DateTimeFormat.forPattern("MMM dd, yyyy 'at' hh:mm a")

        val charge = intent.extras!!.getSerializable(Extras.SessionInformationActivity.CHARGE) as Charge
        mPresenter.getLocation(charge.locationId)
        mBinding.sessionInformationChargingSiteDate.text = "Date: " + mDateTimeFormatter.print(charge.startedAt)
        val period = Period(0, charge.duration.toLong())
        val periodFormatter = PeriodFormatterBuilder()
            .appendHours().appendSuffix("hr ")
            .appendMinutes().appendSuffix("min")
            .toFormatter()
        mBinding.sessionInformationDuration.text = periodFormatter.print(period)
        mBinding.sessionInformationEnergy.text = String.format("%.3f kWh", charge.kwh)
        mBinding.sessionInformationPrice.text = "Total: " + String.format("$%.2f", charge.price)
        mBinding.sessionInformationId.text = "Session ID: " + charge.noodoeId
    }

    override fun setListeners() {
        mBinding.sessionInformationToolbar.setNavigationOnClickListener { finish() }
    }

    override fun showLocation(response: Location?) {
        mBinding.sessionInformationChargingSiteSubtitle.text = response!!.name
        mBinding.sessionInformationChargingSiteId.text = "Station ID: " + response.id.toString()
        mBinding.sessionInformationChargingSiteAddress.text = response.address.toString()
    }

    override fun showError(requestError: RequestError) {

    }

}
