package org.evcs.android.activity

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.EVCSApplication
import org.evcs.android.features.map.location.LocationPresenter
import org.evcs.android.features.map.location.LocationView
import org.evcs.android.databinding.ActivitySessionInformationBinding
import org.evcs.android.model.Charge
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.util.Extras
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class SessionInformationActivity : BaseActivity2(), LocationView {

    private lateinit var mPresenter: LocationPresenter
    private lateinit var mDateTimeFormatter: DateTimeFormatter
    private lateinit var mBinding: ActivitySessionInformationBinding

    override fun init() {
        mPresenter = LocationPresenter(
            this, EVCSApplication.getInstance().retrofitServices)
        mPresenter.onViewCreated()
    }

    override fun inflate(layoutInflater: LayoutInflater?): View {
        mBinding = ActivitySessionInformationBinding.inflate(layoutInflater!!)
        return mBinding.root
    }

    override fun populate() {
        mDateTimeFormatter = DateTimeFormat.forPattern("MMM dd, yyyy 'at' hh:mm a")

        val charge = intent.getSerializableExtra(Extras.SessionInformationActivity.CHARGE) as Charge
        mPresenter.getLocation(charge.locationId)
        if (charge.startedAt != null)
            mBinding.sessionInformationChargingSiteDate.text = "Date: " + mDateTimeFormatter.print(charge.startedAt)
        mBinding.sessionInformationDuration.text = charge.printableDuration
        mBinding.sessionInformationEnergy.text = String.format("%.3f kWh", charge.kwh)
        mBinding.sessionInformationPrice.text = "Total: " + String.format("$%.2f", charge.price)
        mBinding.sessionInformationId.text = "Session ID: " + charge.id
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
