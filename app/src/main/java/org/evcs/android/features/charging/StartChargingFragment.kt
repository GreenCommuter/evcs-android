package org.evcs.android.features.charging

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.widget.VideoView
import androidx.annotation.RawRes
import com.base.core.util.NavigationUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.ChargingActivity
import org.evcs.android.activity.ContactSupportActivity
import org.evcs.android.databinding.FragmentStartChargingBinding
import org.evcs.android.features.shared.EVCSDialogFragment
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
        return StartChargingPresenter(
                this, EVCSApplication.getInstance().retrofitServices,
                requireArguments().getInt(Extras.StartCharging.STATION_ID),
                requireArguments().getString(Extras.StartCharging.PM_ID),
                requireArguments().getSerializable(Extras.StartCharging.COUPONS) as ArrayList<String>?,
        )
    }

    override fun init() {
        startCharging()
    }

    override fun setListeners() {
        mBinding.startChargingCancel.setOnClickListener { onBackPressed() }
    }

    override fun setUi(v: View) {
        mBinding = FragmentStartChargingBinding.bind(v)
        super.setUi(v)
    }

    private fun startCharging() {
        presenter.startSession()
        mBinding.startChargingImage.setVideoResource(R.raw.evcs_scene1, requireContext())
    }

    fun VideoView.setVideoResource(@RawRes videoRes: Int, context: Context) {
        val uri = Uri.parse("android.resource://${context.packageName}/$videoRes")
        setVideoURI(uri)
        setOnPreparedListener { mp ->
            start()
            mp.isLooping = true
            mp.setOnInfoListener { _, what, _ ->
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // video started; hide the placeholder.
                    mBinding.startChargingImage.background = ColorDrawable(Color.TRANSPARENT)
                    true
                } else false
            }
        }
    }

    //TODO: show message
    override fun showErrorDialog(requestError: RequestError) {
//        super.showError(requestError)
        mBinding.startChargingImage.stopPlayback()
        EVCSDialogFragment.Builder()
                .setTitle(getString(R.string.start_charging_error_title))
                .setSubtitle(getString(R.string.start_charging_error_subtitle))
                .addButton(getString(R.string.start_charging_error_retry), {
                    fragment -> fragment.dismiss()
                    startCharging()
                }, R.drawable.layout_corners_rounded_blue)
                .addButton(getString(R.string.start_charging_error_cancel), {
//                    fragment -> fragment.dismiss()
                    requireActivity().finish()
                }, R.drawable.layout_corners_rounded_blue_outline, R.color.button_text_color_selector_blue_outline)
                .showCancel(getString(R.string.start_charging_error_support))
                .withCancelOnClickListener {
                    requireActivity().finish()
                    NavigationUtils.jumpTo(requireContext(), ContactSupportActivity::class.java)
                }
                .setCancelable(false)
                .show(childFragmentManager)
    }

    override fun onSessionStarted() {
        mListener.onChargingStarted()
    }

    override fun onBackPressed(): Boolean {
        (activity as ChargingActivity).cancelSession(childFragmentManager)
        return true
    }

}