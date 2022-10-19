package org.evcs.android.activity;

import android.content.Context;
import android.os.Build;

import androidx.annotation.IdRes;

import org.evcs.android.R;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public abstract class BaseActivity2 extends BaseActivity {

    protected static final int CONTAINER_VIEW_ID = R.id.activity_base_content;

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
}
