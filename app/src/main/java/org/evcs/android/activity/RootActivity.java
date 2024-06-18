package org.evcs.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import org.evcs.android.EVCSApplication;
import org.evcs.android.activity.account.ChangePasswordActivity;
import org.evcs.android.features.auth.initialScreen.AuthActivity;
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
        Intent newIntent;
        if (mRootActivityPresenter.isLoggedUser()) {
            newIntent = new Intent(this, MainActivity.class);
        } else {
            newIntent = new Intent(this, AuthActivity.class);
        }

        newIntent.putExtra(Extras.Root.OPENING_KEY, true);

        if (getIntent().getData() != null)
            Log.e("intentdatapath", getIntent().getData().getPath());
        else
            Log.e("intentdata", "null");
        if (getIntent().getData() != null &&
                Extras.ForgotPassword.PATH.equals(getIntent().getData().getPath())) {
            newIntent = getPasswordIntent();
        }
        return newIntent;
    }

    private Intent getPasswordIntent() {
        Intent newIntent = new Intent(this, ChangePasswordActivity.class);
        //This prevents bad encoding
        newIntent.putExtra(Extras.ForgotPassword.EMAIL, getIntent().getData().getQueryParameters(Extras.ForgotPassword.EMAIL).get(0));
        newIntent.putExtra(Extras.ForgotPassword.ID, getIntent().getData().getQueryParameter(Extras.ForgotPassword.ID));
        return newIntent;
    }

}
