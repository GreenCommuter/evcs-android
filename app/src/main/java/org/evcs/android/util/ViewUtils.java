package org.evcs.android.util;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utilities for Android views
 */
public class ViewUtils {

    private ViewUtils() {}

    /**
     * Determines and sets a view visibility depending on a flag.
     *
     * @param view Target {@link View}.
     * @param show If true the visibility will be {@link View#VISIBLE}, else {@link View#GONE}.
     */
    public static void setViewVisibility(@NonNull View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Returns the width of the screen in pixels
     * @param context the context of the view
     */
    public static float getWindowWidth(Context context) {
        return ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
    }

    /**
     * Removes the underlines for a {@link TextView} with auto generated URL links for email or phones.
     *
     * @param textView TextView to remove underlines from
     */
    public static void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    public static void addUnderlines(TextView textView) {
        SpannableString content = new SpannableString(textView.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }

    /**
     * Span to remove underline
     */
    private static class URLSpanNoUnderline extends URLSpan {

        private static final Parcelable.Creator<URLSpanNoUnderline> CREATOR =
            new Creator<URLSpanNoUnderline>() {
            @Override
            public URLSpanNoUnderline createFromParcel(Parcel source) {
                return new URLSpanNoUnderline(source.readString());
            }

            @Override
            public URLSpanNoUnderline[] newArray(int size) {
                return new URLSpanNoUnderline[size];
            }
        };

        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    public static void showIfTheresScrolling(@NonNull final View shadow, @NonNull final View scrollView) {
        scrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                boolean canScroll = scrollView.canScrollVertically(1) || scrollView.canScrollVertically(-1);
                shadow.setVisibility(canScroll ? View.VISIBLE : View.GONE);
            }
        });
    }
}
