package org.evcs.android.util

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

class AnimationUtils {
    companion object {
        fun animateTranslation(v: View, distance: Float) {
            ObjectAnimator.ofFloat(v, "translationY", distance).apply {
                duration = 500
                interpolator = LinearInterpolator()
                start()
            }
        }
    }
}