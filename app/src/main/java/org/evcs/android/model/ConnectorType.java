package org.evcs.android.model;

import androidx.annotation.DrawableRes;

import org.evcs.android.R;

import java.io.Serializable;

public enum ConnectorType implements Serializable {
    TESLA("Tesla", R.drawable.ic_map_pin_selected, "Dc"),
    CHADEMO("CHAdeMO", R.drawable.ic_map_pin_selected, "Dc"),
    TYPE1("Type 1(J-1772)", R.drawable.ic_map_pin_selected, "Ac"),
    TYPE2("Type 2", R.drawable.ic_map_pin_selected, "Ac"),
    CCS1("CCS1", R.drawable.ic_map_pin_selected, "Ac"),
    CCS2("CCS2", R.drawable.ic_map_pin_selected, "Ac");

    private final String mString;
    public final @DrawableRes int mIcon;
    public final String mAc;

    ConnectorType(String printableName, @DrawableRes int icon, String ac) {
        mString = printableName;
        mIcon = icon;
        mAc = ac;
    }

    public String getPrintableName() {
        return mString;
    }
}
