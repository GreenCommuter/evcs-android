package org.evcs.android.features.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.evcs.android.R;
import org.evcs.android.activity.AbstractSupportedVersionActivity;
import org.evcs.android.databinding.ActivityBaseNavhostWithBottomNavBinding;
import org.evcs.android.features.auth.initialScreen.AuthActivity;

import org.evcs.android.features.shared.IVersionView;
import org.evcs.android.util.Extras;
import org.evcs.android.util.PushNotificationUtils;
import org.evcs.android.util.UserUtils;

public class MainActivity extends AbstractSupportedVersionActivity implements IVersionView {

    public MainNavigationController mNavigationController;
    private BottomNavigationView mMenu;
    private ActivityResultLauncher mLoginResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                (ActivityResultCallback) result -> { populate(); });
    }

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
        @NonNull ActivityBaseNavhostWithBottomNavBinding binding =
                ActivityBaseNavhostWithBottomNavBinding.inflate(layoutInflater);
        setContentView(binding.getRoot());
        mMenu = binding.bottomNavigation;
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

    public BottomNavigationView getMenuView() {
        return mMenu;
    }

    @Override
    protected void populate() {
        super.populate();
        mMenu.getMenu().findItem(R.id.menu_drawer_profile).setTitle((UserUtils.getLoggedUser() == null) ?
                "Sign in" :
                getString(R.string.drawer_menu_profile));
        mMenu.setSelectedItemId(R.id.menu_drawer_map);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        mMenu.getMenu().findItem(R.id.menu_drawer_profile).setTitle((UserUtils.getLoggedUser() == null) ?
                "Sign in" :
                getString(R.string.drawer_menu_profile));
        mMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_drawer_map:
                        mNavigationController.onMapClicked();
                        break;
                    case R.id.menu_drawer_charging:
                        if (UserUtils.getLoggedUser() == null)
                            mLoginResult.launch(new Intent(MainActivity.this, AuthActivity.class));
                        else
                            mNavigationController.goToCharging();
                        break;
                    case R.id.menu_drawer_profile:
                        if (UserUtils.getLoggedUser() == null)
                            mLoginResult.launch(new Intent(MainActivity.this, AuthActivity.class));
                        else
                            mNavigationController.goToProfile();

                }
                return true;
            }
        });
    }

    @Override
    public void onNetworkError() {
        //Handled by fragment
    }

}
