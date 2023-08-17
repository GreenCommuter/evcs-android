package org.evcs.android.util

import android.content.Context
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.view.Surface
import android.view.TextureView
import android.widget.VideoView
import androidx.annotation.RawRes
import kotlin.math.roundToInt

object VideoUtils {

    fun TextureView.playVideo(@RawRes rawVideo: Int) {
        surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, p1: Int, p2: Int) {
                playVideo(surfaceTexture, rawVideo)
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {}

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {}

        }
    }

    fun TextureView.playVideo(surfaceTexture: SurfaceTexture, @RawRes rawVideo: Int) {
        try {
            val assetFileDescriptor = resources.openRawResourceFd(rawVideo)
            val surface = Surface(surfaceTexture)
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset, assetFileDescriptor.length)
            mediaPlayer.prepareAsync()
            mediaPlayer.setSurface(surface)
            mediaPlayer.isLooping = true
            mediaPlayer.setOnPreparedListener { player ->
                scaleView(mediaPlayer.videoWidth, mediaPlayer.videoHeight)
                player?.start()
            }
            mediaPlayer.setOnErrorListener { _, _, _ -> false }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun TextureView.scaleView(videoWidth: Int, videoHeight: Int) {
        val lp = layoutParams
        val prop = width / videoWidth.toFloat()
        lp.height = (videoHeight * prop).roundToInt()
        layoutParams = lp
    }

    fun VideoView.setVideoResource(@RawRes videoRes: Int, context: Context) {
        val uri = Uri.parse("android.resource://${context.packageName}/$videoRes")
        setVideoURI(uri)
    }

    fun VideoView.startAndLoop() {
        setOnPreparedListener { mp ->
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