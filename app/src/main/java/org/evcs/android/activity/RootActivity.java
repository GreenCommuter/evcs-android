package org.evcs.android.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.evcs.android.EVCSApplication;
import org.evcs.android.features.auth.initialScreen.AuthActivity;
import org.evcs.android.features.main.MainActivity;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class RootActivity extends AppCompatActivity {

    private final RootActivityPresenter mRootActivityPresenter = new RootActivityPresenter(this,
            EVCSApplication.getInstance().getRetrofitServices());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return newIntent;
    }

}
