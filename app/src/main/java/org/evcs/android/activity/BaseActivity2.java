package org.evcs.android.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.IdRes;

import org.evcs.android.R;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public abstract class BaseActivity2 extends BaseActivity {

    protected static final int CONTAINER_VIEW_ID = R.id.activity_base_content;
    private static final float MAX_FONT_SIZE = 1.12f;

    /**
     * Returns the container id where the activity adds fragments.
     *
     * @return Container view ID
     */
    @IdRes
    protected final int getContainerViewId() {
        return CONTAINER_VIEW_ID;
    }

    @Override
    protected void attachBaseContext(Context context) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            super.attachBaseContext(ViewPumpContextWrapper.wrap(context));
        } else {
            super.attachBaseContext(context);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(getStatusBarColor());
        adjustFontScale(getResources().getConfiguration());
    }

    protected int getStatusBarColor() {
        return Color.TRANSPARENT;
    }

    protected void setStatusBarColor(int color) {
        getWindow().setStatusBarColor(color);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void adjustFontScale(android.content.res.Configuration configuration) {
        if (configuration.fontScale > getMaxFontSize()) {
            configuration.fontScale = getMaxFontSize();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getBaseContext().getResources().updateConfiguration(configuration, metrics);
        }
    }

    //Override to change max font size
    protected float getMaxFontSize() {
        return MAX_FONT_SIZE;
    }

}
