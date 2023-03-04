package org.evcs.android.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.features.main.MainActivity;
import org.evcs.android.model.push.PushNotificationData;

public final class PushNotificationUtils {

    public final static String CHANNEL_ID = "gcnotifications";

    /**
     * Empty constructor.
     */
    private PushNotificationUtils() {}

    /**
     * Show the notification and open the app when it is clicked
     * @param title The text to be shown above the notification text
     * @param data a {@link PushNotificationData} with the message to show, the view to open and the
     *             id to use as parameter for the view.
     */
    public static void openApp(String title, PushNotificationData data) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//        intent.putExtra(Extras.Root.VIEW_KEY, data.getViewKey());
//        if (data.getId() != null) {
//            intent.putExtra(Extras.Root.ID, Integer.valueOf(data.getId()));
//        }

        sendNotification(intent, data.getText().hashCode(), title, data.getText());
    }

    /**
     * Called by the above method to show the notification and open the app when it is clicked
     * @param intent to choose the activity which will be opened
     * @param notificationId see {@link NotificationManager#notify(int, Notification)}
     * @param title The text to be shown above the notification text
     * @param body The notification text with no more placeholders
     */
    private static void sendNotification(Intent intent, int notificationId, String title, String body) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap largeIcon = BitmapFactory.decodeResource(getContext().getResources(),
                R.mipmap.ic_launcher_foreground);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_foreground)
                        .setLargeIcon(largeIcon)
                        .setContentText(body)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        //HIGH or MAX will be considered as heads-up notification
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(body));

        if (intent != null) {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
            stackBuilder.addNextIntentWithParentStack(intent);
            int flags = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M ?
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE :
                    PendingIntent.FLAG_UPDATE_CURRENT;
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, flags);
            notificationBuilder.setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, notificationBuilder.build());

    }

    private static Context getContext() {
        return EVCSApplication.getInstance();
    }
}
