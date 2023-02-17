package org.evcs.android.network.service.fcm.listener;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.model.push.PushNotificationData;
import org.evcs.android.util.PushNotificationUtils;
import org.evcs.android.util.UserUtils;

/**
 * This class listens for push notifications. When one is received, it calls onMessageReceived.
 */
public class FcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "FcmListenerService";
    private static final String KEY_BODY = "message_body";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATA = "data"; //use "default" to receive from AWS

    /**
     * Called when a message is received
     * @param remoteMessage the message received. remoteMessage.getData() is a bundle with all the
     *                      information we need to display the notification
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (UserUtils.getLoggedUser() == null) {
            Log.d(TAG, "There isn't an user logged");
            return;
        }

        if (remoteMessage.getData().containsKey(KEY_DATA)) {
            Log.d(TAG, "Alert: " + remoteMessage.getData().get(KEY_DATA));
        } else {
            Log.d(TAG, "The push notification doesn't have 'data' key");
            return;
        }

        handleNotification(remoteMessage);
    }

    /**
     * Get the parameters from the notification
     * @param msg The notification bundle.
     */
    private void handleNotification(RemoteMessage msg) {
        String dataString = msg.getData().get(KEY_DATA);

        PushNotificationData data = new Gson().fromJson(dataString, PushNotificationData.class);
        PushNotificationUtils.openApp(BaseConfiguration.Push.CUSTOM_PUSH_TITLE, data);
    }

}
