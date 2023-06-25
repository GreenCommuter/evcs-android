package org.evcs.android.features.charging

import android.graphics.drawable.AnimationDrawable
import android.view.View
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.ChargingActivity
import org.evcs.android.databinding.FragmentStartChargingBinding
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras

class StartChargingFragment : ErrorFragment<StartChargingPresenter>(), StartChargingView {

    private val mListener: ChargingNavigationController = ChargingNavigationController.getInstance()
    private lateinit var mBinding: FragmentStartChargingBinding

    override fun layout(): Int {
        return R.layout.fragment_start_charging
    }

    override fun createPresenter(): StartChargingPresenter {
        return StartChargingPresenter(this, EVCSApplication.getInstance().retrofitServices,
                requireArguments().getInt(Extras.StartCharging.STATION_ID),
                requireArguments().getString(Extras.StartCharging.PM_ID),
                requireArguments().getSerializable(Extras.StartCharging.COUPONS) as ArrayList<String>?,
            )
    }

    override fun init() {
//        (mBinding.startChargingImage.background as AnimationDrawable).start()
        startCharging()
    }

    override fun setUi(v: View) {
        mBinding = FragmentStartChargingBinding.bind(v)
        super.setUi(v)
    }

    override fun setListeners() {
        super.setListeners()
//        mBinding.startChargingButton.setOnClickListener { startCharging() }
    }

    private fun startCharging() {
        presenter.startSession()
//        mBinding.startChargingImage.setBackgroundResource(R.drawable.preparing)
//        (mBinding.startChargingImage.background as AnimationDrawable).start()
//        mBinding.startChargingText.text = getString(R.string.start_charging_preparing)
//        mBinding.startChargingSubtext.text = getString(R.string.start_charging_preparing_subtitle)
//        mBinding.startChargingButton.visibility = View.GONE
    }

    //TODO: show message
    override fun showError(requestError: RequestError) {
        super.showError(requestError)
        mBinding.startChargingImage.setBackgroundResource(R.drawable.not_charging)
        mBinding.startChargingText.text = ""
        mBinding.startChargingSubtext.text = ""
//        mBinding.startChargingButton.visibility = View.VISIBLE
    }

    override fun onSessionStarted() {
        mListener.onChargingStarted()
    }

    override fun onBackPressed(): Boolean {
        (activity as ChargingActivity).cancelSession(childFragmentManager)
        return true
    }
}