package org.evcs.android.network.service.fcm.listener;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.EVCSApplication;
import org.evcs.android.model.push.DeviceToken;
import org.evcs.android.network.service.DeviceTokensService;
import org.evcs.android.network.service.EVCSRetrofitServices;
import org.evcs.android.util.UserUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that listens to changes in the device token, so we can notify API if it changes. Will call
 * onTokenRefresh if it changes, which happens when the app is uninstalled and reinstalled and in
 * some other more rare cases.
 */
public class InstanceIdListenerService extends FirebaseMessagingService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onNewToken(String refreshedToken) {
        Log.d(InstanceIdListenerService.class.getSimpleName(),
                "Refreshed token: " + refreshedToken);
        if (UserUtils.getLoggedUser() != null) {
            sendRegistrationToServer(refreshedToken);
        }
    }

    /**
     * Sends the refreshed token to API
     * @param refreshedToken the new token.
     */
    private void sendRegistrationToServer(String refreshedToken) {

        DeviceToken device = new DeviceToken(refreshedToken, BaseConfiguration.DEVICE_TYPE);

        EVCSRetrofitServices retrofitServices = EVCSApplication.getInstance()
                .getRetrofitServices();
        retrofitServices.getService(DeviceTokensService.class)
                .addDeviceToken(device)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        //This method must be empty.
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //This method must be empty.
                    }
                });
    }
}
