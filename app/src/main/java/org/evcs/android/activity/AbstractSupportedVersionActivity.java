package org.evcs.android.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.CallSuper;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.event.ConnectivityEvent;
import org.evcs.android.features.shared.EVCSDialogFragment;
import org.evcs.android.features.shared.IVersionView;
import org.evcs.android.features.shared.VersionPresenter;
import org.evcs.android.network.service.fcm.listener.ConnectivityListener;
import org.evcs.android.util.NetworkUtils;
import org.evcs.android.util.VersionUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * This class provides a method to go to play store to update the application.
 */
public abstract class AbstractSupportedVersionActivity extends BaseActivity2 implements IVersionView {

    private static final String CONNECTIVITY_CHANGE_INTENT = "android.net.conn.CONNECTIVITY_CHANGE";

    private VersionPresenter mVersionPresenter;
    private EVCSDialogFragment mDialogFragment;
    private ConnectivityListener mConnectivityListener;

    @Override
    @CallSuper
    protected void init() {
        mVersionPresenter = new VersionPresenter(this,
                EVCSApplication.getInstance().getRetrofitServices());
        mVersionPresenter.onViewCreated();
        mVersionPresenter.checkSupportedVersion(VersionUtils.getversion(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnectivityListener = new ConnectivityListener();
        registerReceiver(mConnectivityListener, new IntentFilter(CONNECTIVITY_CHANGE_INTENT));
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mConnectivityListener);
        super.onPause();
    }

    @Subscribe
    public void onEvent(ConnectivityEvent connectivityEvent) {
        if (connectivityEvent.isConnected() && mDialogFragment != null) {
            mDialogFragment.dismiss();
            mVersionPresenter.checkSupportedVersion(VersionUtils.getversion(this));
        }
    }

    public void showNotSupportedVersion(String versionWording) {
        if (versionWording == null) {
            versionWording = getString(R.string.not_supported_version_dialog_message);
        }
        new EVCSDialogFragment.Builder().
                setTitle(getString(R.string.not_supported_version_dialog_title))
                .setSubtitle(versionWording)
                .addButton(getString(R.string.not_supported_version_dialog_button), fragment -> {
                            fragment.dismiss();
                            final String appPackageName = getPackageName();
                            try {
                                startActivity(
                                        new Intent(Intent.ACTION_VIEW, Uri.parse(
                                                BaseConfiguration.BASE_URL_PLAY_STORE + appPackageName)
                                        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            } catch (ActivityNotFoundException e) {
                                // this exception will be throw when the cellphone
                                // don't have Play Store installed.
                                Log.e("SupportedVersionA", e.getLocalizedMessage(), e);
                            } finally {
                                finish();
                            }
                        })
                .setCancelable(false)
                .show(getSupportFragmentManager());
    }

    @Override
    public void onNetworkError() {
        mDialogFragment = NetworkUtils.getConnectionErrorDialog(this);
        mDialogFragment.show(getSupportFragmentManager());
    }
}
