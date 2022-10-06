package org.evcs.android.util;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;

public final class StaticMapUtils {

    private static final int STATIC_MAP_HEIGHT_WIDTH_ZOOM = 500;
    private static final String STATIC_MAP_URL_FORMAT = "https://maps.google.com/maps/api/staticmap?center=%f,%f&zoom=15&size=%dx%d&sensor=false&markers=color:red%%7C%f,%f&key=%s";

    private StaticMapUtils() {}

    public static String getStaticMapUri(@NonNull LatLng latLng) {
        return String.format(STATIC_MAP_URL_FORMAT, latLng.latitude,
                latLng.longitude, STATIC_MAP_HEIGHT_WIDTH_ZOOM,
                STATIC_MAP_HEIGHT_WIDTH_ZOOM, latLng.latitude, latLng.longitude,
                EVCSApplication.getInstance().getResources().getString(R.string.google_api_key));
    }
}
