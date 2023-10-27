package org.evcs.android.features.shared.places;

import android.text.SpannableString;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public class CustomLocation {

    private SpannableString location;
    private int drawable;

    CustomLocation(@NonNull SpannableString location, @DrawableRes int drawable) {
        this.location = location;
        this.drawable = drawable;
    }

    CustomLocation(@NonNull String location, @DrawableRes int drawable) {
        this.location = new SpannableString(location);
        this.drawable = drawable;
    }

    CustomLocation(@NonNull SpannableString location) {
        this.location = location;
    }

    CustomLocation(@NonNull String location) {
        this.location = new SpannableString(location);
    }

    @NonNull
    public SpannableString getLocation() {
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
