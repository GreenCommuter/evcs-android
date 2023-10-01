package org.evcs.android.features.auth.initialScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.base.core.util.NavigationUtils;

import org.evcs.android.R;
import org.evcs.android.activity.AbstractSupportedVersionActivity;
import org.evcs.android.databinding.ActivityBaseNavhostBinding;
import org.evcs.android.features.auth.register.VerifyPhoneActivity;
import org.evcs.android.features.main.MainActivity;
import org.evcs.android.util.Extras;

public class AuthActivity extends AbstractSupportedVersionActivity {

//    private int mId;
    private ActivityResultLauncher<Intent> mLauncher;
    public boolean mSkipRoot;

    @Override
    protected View inflate(LayoutInflater layoutInflater) {
        ActivityBaseNavhostBinding binding = ActivityBaseNavhostBinding.inflate(layoutInflater);
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> onAuthFinished(result.getResultCode()));

//        if (getIntent().getExtras() == null) return;
//        mId = getIntent().getIntExtra(Extras.Root.ID, -1);
        mSkipRoot = getIntent().getBooleanExtra(Extras.AuthActivity.SKIP_ROOT, false);
    }

    @Override
    public void isSupportedVersion(boolean isSupported, String versionWording) {
        if (!isSupported) {
            showNotSupportedVersion(versionWording);
        }
    }

    public void onAuthFinished() {
//        intent.putExtra(Extras.Root.ID, mId);
//        intent.putExtra(Extras.Root.OPENING_KEY, true);
        NavigationUtils.jumpToClearingTask(this, MainActivity.class);
    }


    private void onAuthFinished(int resultCode) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Extras.VerifyActivity.RESULT, resultCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onNetworkError() {
        //Handled by fragment
    }

    @Override
    protected int getNavGraphId() {
        return R.navigation.navigation_auth;
    }

    public void goToVerify() {
//        Context context = EVCSApplication.getInstance().getApplicationContext();
//        Intent intent1 = new Intent(context, MainActivity.class);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        Intent intent2 = new Intent(context, VerifyPhoneActivity.class);
//        context.startActivities(new Intent[]{ intent1, intent2 });
//        NavigationUtils.IntentExtra intentExtra = new NavigationUtils.IntentExtra(Extras.VerifyActivity.RESULT, true);
        Intent intent = new Intent(this, VerifyPhoneActivity.class);
        intent.putExtra(Extras.VerifyActivity.FROM_AUTH, true);
        mLauncher.launch(intent);
    }
}