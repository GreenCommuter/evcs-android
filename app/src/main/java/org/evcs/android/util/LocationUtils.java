package org.evcs.android.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.base.core.permission.PermissionListener;
import com.base.core.permission.PermissionManager;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.Configuration;
import org.evcs.android.R;
import org.evcs.android.features.shared.EVCSDialogFragment;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Utilities to help with location
 */
public final class LocationUtils {

    private static final double PADDING_DEGREES = 0.004;
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

    public static void addDiagonal(LatLngBounds.Builder builder, LatLng latlng) {
        LatLng northEast = new LatLng(latlng.latitude + PADDING_DEGREES, latlng.longitude + PADDING_DEGREES);
        LatLng southWest = new LatLng(latlng.latitude - PADDING_DEGREES, latlng.longitude - PADDING_DEGREES);
        builder.include(northEast);
        builder.include(southWest);
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

    public static void launchGoogleMapsWithPin(Context context, LatLng latLng, String gatecode, FragmentManager fm) {
        if (gatecode == null) {
            launchGoogleMapsWithPin(context, latLng);
            return;
        }
        TextView gatecodeView = new TextView(context);
        gatecodeView.setText("Gate code: " + gatecode);
        gatecodeView.setGravity(Gravity.CENTER);

        new EVCSDialogFragment.Builder()
                .setTitle(context.getString(R.string.gatecode_dialog_title))
                .setSubtitle(context.getString(R.string.gatecode_dialog_subtitle), Gravity.CENTER)
                .addView(gatecodeView)
                .addButton(context.getString(R.string.app_continue), fragment -> {
                    launchGoogleMapsWithPin(context, latLng);
                    fragment.dismiss();
                }, R.style.ButtonK_Blue)
                .show(fm);
    }

    public static void launchGoogleMapsWithPin(Context context, LatLng latLng) {
        Uri gmmIntentUri = Uri.parse("geo:" + latLng.latitude +"," + latLng.longitude
                + "?q=" + latLng.latitude + "," + latLng.longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }

    public static float distance(LatLng latLng, LatLng latLng2) {
        double lon1 = Math.toRadians(latLng.longitude);
        double lon2 = Math.toRadians(latLng2.longitude);
        double lat1 = Math.toRadians(latLng.latitude);
        double lat2 = Math.toRadians(latLng2.latitude);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        float earthRadius = 3956; //miles

        return (float) (c * earthRadius);
    }

    public interface LocationCallback {
        void onLocationRetrieved(Location location);
    }

}
