package org.evcs.android.features.charging

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener

object KeyboardListener {
    private lateinit var listener: OnGlobalLayoutListener

    fun attach(rootView : View, onKeyboardShown: (Boolean) -> Unit) {
        listener = OnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r);
            val heightDiff = rootView.height - (r.bottom - r.top);
            if (heightDiff > (r.bottom - r.top) / 5f) {
                onKeyboardShown.invoke(true)
            } else {
                onKeyboardShown.invoke(false)
            }
        }

        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    fun detach(rootView : View) {
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }
}