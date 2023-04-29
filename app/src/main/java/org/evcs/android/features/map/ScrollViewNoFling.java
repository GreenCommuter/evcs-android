package org.evcs.android.features.map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.OverScroller;
import android.widget.ScrollView;

import java.lang.reflect.Field;

/**
 * The problem with flings is that they don't get caught by MotionEvents, so LocationSliderFragment#snap()
 * is not called. I could deactivate flings, but they are useful when the list is too long. So I
 * want to override fling behaviour setting a minimum. Below that minimum, there's no flinging.
 * After that minimum, there is, but you cant go beyond it. Unfortunately to do this I must use
 * reflection.
 */
public class ScrollViewNoFling extends ScrollView {

    private int mMinFling;

    public ScrollViewNoFling(Context context) {
        super(context);
    }
    public ScrollViewNoFling(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ScrollViewNoFling(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void fling(int velocityY) {
        if (getScrollY() < mMinFling) return;
        if (getChildCount() > 0) {

            int height = getHeight() - getPaddingBottom() - getPaddingTop();
            int bottom = getChildAt(0).getHeight();

            getScroller().fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, mMinFling,
                    Math.max(0, bottom - height), 0, 0);

            postInvalidateOnAnimation();
        }
    }

    private OverScroller getScroller() {
        try {
            Field privateField = ScrollView.class.getDeclaredField("mScroller");
            privateField.setAccessible(true);
            return (OverScroller) privateField.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setMinFling(int minFling) {
        mMinFling = minFling;
    }
}