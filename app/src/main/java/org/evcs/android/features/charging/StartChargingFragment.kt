package org.evcs.android.features.charging

import android.graphics.drawable.AnimationDrawable
import android.view.View
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.R
import org.evcs.android.EVCSApplication
import org.evcs.android.databinding.FragmentStartChargingBinding

class StartChargingFragment : ErrorFragment<StartChargingPresenter>(), StartChargingView {

    private lateinit var mBinding: FragmentStartChargingBinding

    override fun layout(): Int {
        return R.layout.fragment_start_charging
    }

    override fun createPresenter(): StartChargingPresenter {
        return StartChargingPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {}

    override fun setUi(v: View) {
        mBinding = FragmentStartChargingBinding.bind(v)
        super.setUi(v)
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.startChargingButton.setOnClickListener { startCharging() }
    }

    private fun startCharging() {
//        presenter.startSession()
        mBinding.startChargingImage.setBackgroundResource(R.drawable.preparing)
        (mBinding.startChargingImage.background as AnimationDrawable).start()
        mBinding.startChargingText.text = "Preparing to start charging"
        mBinding.startChargingSubtext.text = "This should only take a couple of seconds"
        mBinding.startChargingButton.visibility = View.GONE
    }
}