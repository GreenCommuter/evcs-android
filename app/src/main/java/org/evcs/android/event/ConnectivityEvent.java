package org.evcs.android.event;

import org.evcs.android.network.service.fcm.listener.ConnectivityListener;

/**
 * Broadcaster: {@link ConnectivityListener}
 * Notifies when the phone is connected or disconnected. Right now we use it only at the beginning
 * of the Vanpool and Carshare flows, but any fragment that needs an active internet connection
 * could make use of this event.
 */
public class ConnectivityEvent {

    private final boolean connected;

    public ConnectivityEvent(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }
}
