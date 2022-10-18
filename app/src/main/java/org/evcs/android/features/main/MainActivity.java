package org.evcs.android.features.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import org.evcs.android.R;
import org.evcs.android.activity.AbstractSupportedVersionActivity;
import org.evcs.android.databinding.ActivityBaseNavhostBinding;
import org.evcs.android.features.shared.IVersionView;
import org.evcs.android.util.Extras;
import org.evcs.android.util.PushNotificationUtils;

public class MainActivity extends AbstractSupportedVersionActivity implements IVersionView {

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

    @Override
    protected View inflate(LayoutInflater layoutInflater) {
        @NonNull ActivityBaseNavhostBinding binding = ActivityBaseNavhostBinding.inflate(layoutInflater);
        setContentView(binding.getRoot());
        return binding.getRoot();
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

}
