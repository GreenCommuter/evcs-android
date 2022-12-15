package org.evcs.android.features.profile.wallet;

import android.view.LayoutInflater;
import android.view.View;

import org.evcs.android.R;
import org.evcs.android.activity.NavGraphActivity;
import org.evcs.android.databinding.ActivityBaseNavhostBinding;

public class WalletActivity extends NavGraphActivity {
    @Override
    protected View inflate(LayoutInflater layoutInflater) {
        return ActivityBaseNavhostBinding.inflate(layoutInflater).getRoot();
    }

    @Override
    protected void init() {

    }

    @Override
    protected int getNavGraphId() {
        return R.navigation.navigation_wallet;
    }
}
