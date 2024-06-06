package org.evcs.android.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.widget.VideoView
import androidx.annotation.RawRes
import kotlin.math.roundToInt

object VideoUtils {

    private fun VideoView.scaleView(videoWidth: Int, videoHeight: Int) {
        val lp = layoutParams
        //match parent sometimes fails
        lp.width = (parent as View).width
        val prop = lp.width / videoWidth.toFloat()
        lp.height = (videoHeight * prop).roundToInt()
        layoutParams = lp
    }

    fun VideoView.setVideoResource(@RawRes videoRes: Int, context: Context) {
        val uri = Uri.parse("android.resource://${context.packageName}/$videoRes")
        setVideoURI(uri)
    }

    fun VideoView.startAndLoop() {
        setOnPreparedListener { mp ->
            scaleView(mp.videoWidth, mp.videoHeight)
            start()
            mp.isLooping = true
            mp.setOnInfoListener { _, what, _ ->
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // video started; hide the placeholder.
                    this.background = ColorDrawable(Color.TRANSPARENT)
                    true
                } else false
            }
        }
    }
}