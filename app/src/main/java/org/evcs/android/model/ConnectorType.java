package org.evcs.android.model;

import androidx.annotation.DrawableRes;

import org.evcs.android.R;

import java.io.Serializable;

public enum ConnectorType implements Serializable {
    TESLA("Tesla", R.drawable.conn_tesla, "Dc"),
    CHADEMO("CHAdeMO", R.drawable.conn_chademo, "Dc"),
    TYPE1("Type 1(J-1772)", R.drawable.conn_type1_j1772, "Ac"),
    TYPE2("Type 2", R.drawable.conn_type2, "Ac"),
    CCS1("CCS1", R.drawable.conn_ccs1, "Ac"),
    CCS2("CCS2", R.drawable.conn_ccs2, "Ac");

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
