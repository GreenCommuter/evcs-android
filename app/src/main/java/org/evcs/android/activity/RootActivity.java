package org.evcs.android.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import org.evcs.android.EVCSApplication;
import org.evcs.android.features.account.ChangePasswordFragment;
import org.evcs.android.features.main.MainActivity;
import org.evcs.android.util.Extras;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class RootActivity extends AppCompatActivity {

    private final RootActivityPresenter mRootActivityPresenter = new RootActivityPresenter(this,
            EVCSApplication.getInstance().getRetrofitServices());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        Intent newIntent = buildIntent();

        newIntent.setFlags(FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newIntent);
        finish();
    }

    private Intent buildIntent() {
        Intent newIntent = new Intent(this, MainActivity.class);

        if (getIntent().getData() != null &&
                Extras.ForgotPassword.PATH.equals(getIntent().getData().getPath())) {
            newIntent = getPasswordIntent();
        }
        return newIntent;
    }

    private Intent getPasswordIntent() {
        Intent newIntent = SimpleToolbarActivity.getIntent(this, ChangePasswordFragment.class, "Change password");
        newIntent.putExtra(Extras.ForgotPassword.EMAIL, getIntent().getData().getQueryParameter(Extras.ForgotPassword.EMAIL));
        newIntent.putExtra(Extras.ForgotPassword.ID, getIntent().getData().getQueryParameter(Extras.ForgotPassword.ID));
        return newIntent;
    }

}
