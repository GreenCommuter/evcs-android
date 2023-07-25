package org.evcs.android.features.map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.OverScroller;
import android.widget.ScrollView;

import java.lang.reflect.Field;

/**
 * The problem with flings is that they don't get caught by MotionEvents, so LocationSliderFragment#snap()
 * is not called.
 */
public class ScrollViewNoFling extends ScrollView {

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
    public void fling(int velocityY) {}

}