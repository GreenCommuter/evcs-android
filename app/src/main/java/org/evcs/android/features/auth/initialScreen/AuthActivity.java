package org.evcs.android.features.auth.initialScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import org.evcs.android.R;
import org.evcs.android.activity.AbstractSupportedVersionActivity;
import org.evcs.android.databinding.ActivityBaseNavhostBinding;
import org.evcs.android.features.main.MainActivity;
import org.evcs.android.features.shared.IVersionView;
import org.evcs.android.util.Extras;

public class AuthActivity extends AbstractSupportedVersionActivity implements IVersionView {

    private int mId;

    //TODO: redo design so we don't need to be so strict
    private static final float MAX_FONT_SIZE = 1.12f;

    @Override
    protected View inflate(LayoutInflater layoutInflater) {
        ActivityBaseNavhostBinding binding = ActivityBaseNavhostBinding.inflate(layoutInflater);
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustFontScale(getResources().getConfiguration());
        if (getIntent().getExtras() == null) return;
        mId = getIntent().getIntExtra(Extras.Root.ID, -1);
    }

    @Override
    public void isSupportedVersion(boolean isSupported, String versionWording) {
        if(!isSupported) {
            showNotSupportedVersion(versionWording);
        }
    }

    public void onAuthFinished() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Extras.Root.ID, mId);
        intent.putExtra(Extras.Root.OPENING_KEY, true);
        startActivity(intent);
    }

    private void adjustFontScale(android.content.res.Configuration configuration) {
        if (configuration.fontScale > MAX_FONT_SIZE) {
            configuration.fontScale = MAX_FONT_SIZE;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getBaseContext().getResources().updateConfiguration(configuration, metrics);
        }
    }

    @Override
    protected int getNavGraphId() {
        return R.navigation.navigation_auth;
    }

}