package org.evcs.android.util

import android.app.Activity
import android.content.Context
import android.widget.TextView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.URLSpan
import org.evcs.android.util.ViewUtils.URLSpanNoUnderline
import android.text.style.UnderlineSpan
import android.text.TextPaint
import android.os.Parcelable.Creator
import android.os.Parcel
import android.view.View
import androidx.core.view.ViewCompat

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
    fun setViewVisibility(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Returns the width of the screen in pixels
     * @param context the context of the view
     */
    fun getWindowWidth(context: Context): Float {
        return (context as Activity).windowManager.defaultDisplay.width.toFloat()
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
        scrollView.addOnLayoutChangeListener { view, i, i1, i2, i3, i4, i5, i6, i7 ->
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