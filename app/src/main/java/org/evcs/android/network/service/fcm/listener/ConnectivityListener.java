package org.evcs.android.network.service.fcm.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.evcs.android.event.ConnectivityEvent;
import org.greenrobot.eventbus.EventBus;

/**
 * This will be called when the connectivity status of the device changes. We notify whether there
 * is now an active network connection
 */
public class ConnectivityListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isConnectedWifi = activeNetInfo != null && activeNetInfo.isConnected();
        boolean isConnectedData = mobNetInfo != null && mobNetInfo.isConnected();
        EventBus.getDefault().post(new ConnectivityEvent(isConnectedWifi || isConnectedData));
    }
}
