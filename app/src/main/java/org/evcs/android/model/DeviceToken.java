package org.evcs.android.model;

/**
 * Class that represents a unique token that identifies a device. We send this to API so they know
 * where to send the notifications for each user. The device type is "android" or "ios", while
 * the token is a string.
 */
public class DeviceToken {

    private final String deviceToken;
    private final String deviceType;

    public DeviceToken(String deviceToken, String deviceType) {
        this.deviceToken = deviceToken;
        this.deviceType = deviceType;
    }
}
