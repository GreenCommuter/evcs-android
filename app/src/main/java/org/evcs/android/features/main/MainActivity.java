package org.evcs.android.features.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import org.evcs.android.R;
import org.evcs.android.features.shared.IVersionView;
import org.evcs.android.ui.drawer.AbstractDrawerActivity;
import org.evcs.android.util.Extras;
import org.evcs.android.util.PushNotificationUtils;
import org.evcs.android.util.VersionUtils;

public class MainActivity extends AbstractDrawerActivity implements IVersionView {

    public MainNavigationController mNavigationController;

    @Override
    @CallSuper
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        createNotificationChannel();

        mNavigationController = new MainNavigationController(this,
                true, Navigation.findNavController(this, R.id.activity_base_content));
        mNavigationController.startFlow();

        if (!getIntent().hasExtra(Extras.Root.VIEW_KEY)) {
            return;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = getString(R.string.notifications_channel_name);
            String description = getString(R.string.notifications_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(PushNotificationUtils.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected int getNavGraphId() {
        return R.navigation.navigation_graph;
    }

    @Override
    public void isSupportedVersion(boolean isSupported, String versionWording) {

    }

    @Override
    public void onNetworkError() {
        //Handled by fragment
    }

    @Override
    protected void populateHeader(View headerView) {

    }
}
