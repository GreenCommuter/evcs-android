package org.evcs.android.features.map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

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
    public void fling (int velocityY) {}

}