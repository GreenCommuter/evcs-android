package org.evcs.android.model;

import androidx.annotation.DrawableRes;

import org.evcs.android.R;

import java.io.Serializable;

public enum ConnectorType implements Serializable {
    TESLA("Tesla", R.drawable.ic_map_pin_selected, false),
    CHADEMO("CHAdeMO", R.drawable.ic_map_pin_selected, false),
    TYPE1("Type 1", R.drawable.ic_map_pin_selected, true),
    TYPE2("Type 2", R.drawable.ic_map_pin_selected, true),
    CCS1("CCS1", R.drawable.ic_map_pin_selected, false),
    CCS2("CCS2", R.drawable.ic_map_pin_selected, false);

    private final String mString;
    public final @DrawableRes int mIcon;
    public final boolean mIsAc;

    ConnectorType(String printableName, @DrawableRes int icon, boolean isAc) {
        mString = printableName;
        mIcon = icon;
        mIsAc = isAc;
    }

    public String getPrintableName() {
        return mString;
    }
}
