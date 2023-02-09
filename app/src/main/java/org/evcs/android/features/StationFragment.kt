package org.evcs.android.features

import android.view.View
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.ActivityStationsBinding
import org.evcs.android.model.PricingDetail
import org.evcs.android.model.Station
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.view.shared.StationView
import org.evcs.android.util.Extras

class StationFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mBinding: ActivityStationsBinding

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = ActivityStationsBinding.bind(v)
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun layout(): Int {
        return R.layout.activity_stations
    }

    override fun init() {
        val stations = requireActivity().intent.getSerializableExtra(Extras.StationsActivity.STATIONS)
                as ArrayList<Station>

        showPricing(stations.first().pricing!!.detail)

        stations.forEach { station ->
            val v = StationView(requireContext(), station)
            mBinding.activityStationsStations.addView(v)
        }
    }

    private fun showPricing(pricingDetail: PricingDetail) {
        mBinding.activityStationsSessionFee.text = pricingDetail.printPriceKwh()

        if (pricingDetail.printConnectionFee() != null) {
            mBinding.activityStationsConnectionFee.visibility = View.VISIBLE
            mBinding.activityStationsConnectionFeeText.visibility = View.VISIBLE
            mBinding.activityStationsConnectionFee.text = pricingDetail.printConnectionFee()
        }

        if (pricingDetail.printBufferTime() != null) {
            mBinding.activityStationsGracePeriod.visibility = View.VISIBLE
            mBinding.activityStationsGracePeriodClarification.visibility = View.VISIBLE
            mBinding.activityStationsGracePeriod.text = "(Grace period: " + pricingDetail.printBufferTime() + ")"
        }

        if (pricingDetail.printOccupancyFee() != null) {
            mBinding.activityStationsOccupancySeparator.visibility = View.VISIBLE
            mBinding.activityStationsOccupancyFeeText.visibility = View.VISIBLE
            mBinding.activityStationsOccupancyFee.visibility = View.VISIBLE
            mBinding.activityStationsOccupancyFee.text = pricingDetail.printOccupancyFee()
        }

    }
}