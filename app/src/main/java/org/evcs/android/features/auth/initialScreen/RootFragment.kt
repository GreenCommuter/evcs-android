package org.evcs.android.features.auth.initialScreen

import android.media.MediaPlayer
import android.view.View
import android.widget.VideoView
import androidx.navigation.fragment.findNavController
import org.evcs.android.ui.fragment.ErrorFragment
import com.base.core.presenter.BasePresenter
import com.base.core.util.NavigationUtils
import org.evcs.android.R
import org.evcs.android.databinding.FragmentRootBinding
import org.evcs.android.features.charging.setVideoResource
import org.evcs.android.features.main.MainActivity
import org.evcs.android.util.Extras

class RootFragment : ErrorFragment<BasePresenter<*>>() {
    private lateinit var mBinding: FragmentRootBinding

    override fun layout(): Int {
        return R.layout.fragment_root
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun init() {
        if ((activity as AuthActivity).mSkipRoot) {
            (activity as AuthActivity).mSkipRoot = false
            findNavController().navigate(RootFragmentDirections.actionRootFragmentToRegisterFragment())
        }
    }

    override fun setUi(v: View) {
        mBinding = FragmentRootBinding.bind(v)
    }

    override fun populate() {
        val videoView = mBinding.fragmentRootVideo
        videoView.setVideoResource(R.raw.evcs_loop, requireContext())
        mBinding.fragmentRootVideo.setOnPreparedListener { mediaPlayer ->
            mBinding.fragmentRootVideo.start()
            mediaPlayer.isLooping = true
            mBinding.fragmentRootVideo.scale(mediaPlayer.getVideoRatio())
        }
    }

    fun MediaPlayer.getVideoRatio(): Float {
        return videoWidth / videoHeight.toFloat()
    }

    fun VideoView.scale(videoRatio: Float) {
        val screenRatio = width / height.toFloat()
        val scaleX = videoRatio / screenRatio
        if (scaleX >= 1f) {
            this.scaleX = scaleX
        } else {
            this.scaleY = 1f / scaleX
        }
    }

    override fun setListeners() {
        mBinding.fragmentRootFindALocation.setOnClickListener {
            NavigationUtils.jumpTo(requireContext(), MainActivity::class.java,
                    NavigationUtils.IntentExtra(Extras.MainActivity.IS_BOTTOM, false))
        }
        mBinding.fragmentRootLogIn.setOnClickListener {
            findNavController().navigate(RootFragmentDirections.actionRootFragmentToSignInFragment())
        }
        mBinding.fragmentRootSignUp.setOnClickListener {
            findNavController().navigate(RootFragmentDirections.actionRootFragmentToRegisterFragment())
        }
    }
}