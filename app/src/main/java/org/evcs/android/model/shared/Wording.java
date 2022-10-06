package org.evcs.android.model.shared;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wording implements Serializable {

    private String key;
    @SerializedName("i18n") private String wording;

    public String getWording() {
        return wording;
    }

    public String getKey() {
        return key;
    }
}
