package org.evcs.android.model.push;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Class that represents a type of push notification. Each type has a unique ID, a text, which is
 * how API will send it, and the message we should display to the user.
 */
public class PushNotificationData {

    private @Nullable String view;
    private String id;
    private String text;
    //type;

    //The string is nullable but if it is null we open choose_service
//    public @NonNull ViewKey getViewKey() {
//        return ViewKey.getViewKey(view);
//    }

    public @Nullable String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

}
