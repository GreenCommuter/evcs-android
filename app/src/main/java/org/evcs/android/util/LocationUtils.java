package org.evcs.android.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.core.permission.PermissionListener;
import com.base.core.permission.PermissionManager;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.Configuration;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Utilities to help with location
 */
public final class LocationUtils {

    private static final double PADDING_DEGREES = 0.003;
    public static final int REQUEST_CHECK_SETTINGS = 10000;

    /**
     * Compares two {@link LatLng} and returns <b>true</b> if the distance between them is minor
     * to the configured threshold in {@link Configuration.Location#DISTANCE_CHANGE_THRESHOLD_IN_METERS}.
     * Otherwise the locations are considered different and returns <b>false</b>.
     * If both locations are null, the method returns <b>true</b>.
     *
     * @param start Start location
     * @param end End location
     * @return <b>true</b> if the two locations are the same.
     */
    public static boolean equalsWithThreshold(@Nullable LatLng start, @Nullable LatLng end) {
        if (start == null && end == null) {
            return true;
        } else if (start == null || end == null) {
            return false;
        }

        Location startLoc = new Location("");
        Location endLoc = new Location("");

        startLoc.setLatitude(start.latitude);
        startLoc.setLongitude(start.longitude);

        endLoc.setLatitude(end.latitude);
        endLoc.setLongitude(end.longitude);

        return equalsWithThreshold(startLoc, endLoc);
    }

    /**
     * Compares two {@link LatLng} and returns <b>true</b> if the distance between them is minor
     * to the configured threshold in {@link Configuration.Location#DISTANCE_CHANGE_THRESHOLD_IN_METERS}.
     * Otherwise the locations are considered different and returns <b>false</b>.
     *
     * @param start Start location
     * @param end End location
     * @return <b>true</b> if the two locations are the same.
     */
    public static boolean equalsWithThreshold(@NonNull Location start, @NonNull Location end) {
        return start.distanceTo(end) < BaseConfiguration.Location.DISTANCE_CHANGE_THRESHOLD_IN_METERS;
    }

    /* If there is only one result or the results are too close, we show the max posible zoom
    I can't add a zoom to the LatLngBounds, so the workaround is to add two points in a diagonal
    roughly two kilometres apart */
    public static LatLngBounds addDiagonal(LatLngBounds mapItemsBounds) {
        LatLng center = mapItemsBounds.getCenter();
        LatLng northEast = new LatLng(center.latitude + PADDING_DEGREES, center.longitude + PADDING_DEGREES);
        LatLng southWest = new LatLng(center.latitude - PADDING_DEGREES, center.longitude - PADDING_DEGREES);
        mapItemsBounds = mapItemsBounds.including(northEast);
        mapItemsBounds = mapItemsBounds.including(southWest);
        return mapItemsBounds;
    }
    
    private static void requestLocationPermission(final Activity activity, final LocationCallback callback) {
        PermissionManager.getInstance().requestPermission(activity, new PermissionListener() {
            @Override
            public void onPermissionsGranted() {
                getLastLocationWithPermission(activity, callback);
            }

            @Override
            public void onPermissionsDenied(@NonNull String[] deniedPermissions) {
                callback.onLocationRetrieved(null);
            }
        }, ACCESS_FINE_LOCATION);
    }

    @SuppressLint("MissingPermission")
    public static void getLastLocationWithPermission(Activity activity, final LocationCallback callback) {
        LocationServices.getFusedLocationProviderClient(activity).getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                callback.onLocationRetrieved(task.getResult());
            }
        });
    }

    public interface LocationCallback {
        void onLocationRetrieved(Location location);
    }

}
