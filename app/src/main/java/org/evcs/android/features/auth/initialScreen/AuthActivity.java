package org.evcs.android.features.auth.initialScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import org.evcs.android.R;
import org.evcs.android.activity.AbstractSupportedVersionActivity;
import org.evcs.android.databinding.ActivityBaseNavhostBinding;
import org.evcs.android.features.auth.register.VerifyPhoneActivity;
import org.evcs.android.features.main.MainActivity;
import org.evcs.android.util.Extras;

public class AuthActivity extends AbstractSupportedVersionActivity {

    private int mId;
    private ActivityResultLauncher<Intent> mLauncher;

//    private static final float MAX_FONT_SIZE = 1.12f;

    @Override
    protected View inflate(LayoutInflater layoutInflater) {
        ActivityBaseNavhostBinding binding = ActivityBaseNavhostBinding.inflate(layoutInflater);
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        adjustFontScale(getResources().getConfiguration());
        if (getIntent().getExtras() == null) return;
        mId = getIntent().getIntExtra(Extras.Root.ID, -1);
        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> onAuthFinished(result.getResultCode()));
    }

    @Override
    public void isSupportedVersion(boolean isSupported, String versionWording) {
        if (!isSupported) {
            showNotSupportedVersion(versionWording);
        }
    }

    public void onAuthFinished() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra(Extras.Root.ID, mId);
//        intent.putExtra(Extras.Root.OPENING_KEY, true);
        startActivity(intent);
    }


    private void onAuthFinished(int resultCode) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Extras.VerifyActivity.RESULT, resultCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

//    private void adjustFontScale(android.content.res.Configuration configuration) {
//        if (configuration.fontScale > MAX_FONT_SIZE) {
//            configuration.fontScale = MAX_FONT_SIZE;
//            DisplayMetrics metrics = getResources().getDisplayMetrics();
//            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//            wm.getDefaultDisplay().getMetrics(metrics);
//            metrics.scaledDensity = configuration.fontScale * metrics.density;
//            getBaseContext().getResources().updateConfiguration(configuration, metrics);
//        }
//    }

    @Override
    public void onNetworkError() {
        //Handled by fragment
    }

    @Override
    protected int getNavGraphId() {
        return R.navigation.navigation_auth;
    }

    public void goToVerify() {
        mLauncher.launch(new Intent(this, VerifyPhoneActivity.class));
    }
}