package org.evcs.android.activity;

import android.content.IntentFilter;
import android.view.View;

import androidx.annotation.CallSuper;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.features.shared.IVersionView;
import org.evcs.android.features.shared.VersionPresenter;
import org.evcs.android.network.service.fcm.listener.ConnectivityListener;
import org.evcs.android.util.VersionUtils;

/**
 * This class provides a method to go to play store to update the application.
 */
public abstract class AbstractSupportedVersionActivity extends NavGraphActivity implements IVersionView {

    private static final String CONNECTIVITY_CHANGE_INTENT = "android.net.conn.CONNECTIVITY_CHANGE";

    private VersionPresenter mVersionPresenter;
    private View mDialogFragment;
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
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
//        EventBus.getDefault().unregister(this);
        unregisterReceiver(mConnectivityListener);
        super.onPause();
    }

    public void showNotSupportedVersion(String versionWording) {
        if (versionWording == null) {
            versionWording = getString(R.string.not_supported_version_dialog_message);
        }
    }

    @Override
    public void onNetworkError() {
    }
}
