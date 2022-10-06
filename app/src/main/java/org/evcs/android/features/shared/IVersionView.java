package org.evcs.android.features.shared;

/**
 * This interface provide a callback to know
 * if the application version is supported.
 */
public interface IVersionView {

    /**
     * Callback if the application version is older.
     */
    void isSupportedVersion(boolean isSupported, String versionWording);

    void onNetworkError();

}
