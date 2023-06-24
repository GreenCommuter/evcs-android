package org.evcs.android.util

import android.app.Activity
import android.content.Context
import android.widget.TextView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.text.TextPaint
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible

/**
 * Utilities for Android views
 */
object ViewUtils {
    /**
     * Determines and sets a view visibility depending on a flag.
     *
     * @param view Target [View].
     * @param show If true the visibility will be [View.VISIBLE], else [View.GONE].
     */
    fun View.setVisibility(show: Boolean) {
        this.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun View.setParentVisibility(show: Boolean) {
        (this.parent as View).isVisible = show
    }

    fun TextView.showOrHide(text: String?) {
        isVisible = text != null
        setText(text)
    }

    /**
     * Returns the width of the screen in pixels
     * @param context the context of the view
     */
    fun getWindowWidth(context: Context): Float {
        return (context as Activity).windowManager.defaultDisplay.width.toFloat()
    }

    fun View.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        val lp = ViewGroup.MarginLayoutParams(layoutParams ?: ViewGroup.LayoutParams(-2, -2))
        lp.setMargins(left, top, right, bottom)
        layoutParams = lp
    }

    fun Context.getStatusBarHeight(): Int {
        val resId =
                resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) resources.getDimensionPixelSize(resId) else 0
    }

    /**
     * Workaround to keep the adjust resize behaviour without ruining the toolbar with fitsSystemWindows
     */
    fun setAdjustResize(top: View) {
        ViewCompat.setOnApplyWindowInsetsListener(top) { v, insets ->
            v.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            insets.consumeSystemWindowInsets()
        }
    }

    /**
     * Removes the underlines for a [TextView] with auto generated URL links for email or phones.
     *
     * @param textView TextView to remove underlines from
     */
    fun stripUnderlines(textView: TextView) {
        val s: Spannable = SpannableString(textView.text)
        val spans = s.getSpans(0, s.length, URLSpan::class.java)
        for (span in spans) {
            val start = s.getSpanStart(span)
            val end = s.getSpanEnd(span)
            s.removeSpan(span)
            val span2 = URLSpanNoUnderline(span.url)
            s.setSpan(span2, start, end, 0)
        }
        textView.text = s
    }

    fun addUnderlines(textView: TextView) {
        val content = SpannableString(textView.text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        textView.text = content
    }

    fun showIfTheresScrolling(shadow: View, scrollView: View) {
        scrollView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val canScroll = scrollView.canScrollVertically(1) || scrollView.canScrollVertically(-1)
            shadow.visibility = if (canScroll) View.VISIBLE else View.GONE
        }
    }

    /**
     * Span to remove underline
     */
    class URLSpanNoUnderline(url: String?) : URLSpan(url) {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }

        /*object {
            private val CREATOR: Creator<URLSpanNoUnderline> =
                object : Creator<URLSpanNoUnderline> {
                    override fun createFromParcel(source: Parcel): URLSpanNoUnderline {
                        return URLSpanNoUnderline(source.readString())
                    }

                    override fun newArray(size: Int): Array<URLSpanNoUnderline?> {
                        return arrayOfNulls(size)
                    }
                }
        }*/
    }
}