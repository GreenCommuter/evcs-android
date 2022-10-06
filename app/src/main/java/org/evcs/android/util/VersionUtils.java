package org.evcs.android.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Utils to get the application version installed on cellphone.
 */
public final class VersionUtils {

    /**
     * Empty Constructor.
     */
    private VersionUtils() {}

    /**
     * This method gets the application version installed.
     */
    public static String getversion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("VersionUtils", e.getMessage(), e);
        }
        return info == null ? "0.0.0" : info.versionName ;
    }

}
