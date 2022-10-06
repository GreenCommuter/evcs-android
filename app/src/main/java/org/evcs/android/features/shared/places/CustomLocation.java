package org.evcs.android.features.shared.places;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public class CustomLocation {

    private String location;
    private int drawable;

    CustomLocation(@NonNull String location, @DrawableRes int drawable) {
        this.location = location;
        this.drawable = drawable;
    }

    CustomLocation(@NonNull String location) {
        this.location = location;
    }

    @NonNull
    public String getLocation() {
        return location;
    }

    @DrawableRes
    public int getDrawable() {
        return drawable;
    }

    @Override
    public String toString() {
        return String.valueOf(location);
    }
}
