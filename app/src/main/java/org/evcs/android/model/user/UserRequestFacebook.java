package org.evcs.android.model.user;

import com.google.gson.annotations.SerializedName;

public class UserRequestFacebook {

    @SerializedName("accessToken")
    private final String accessToken;

    public UserRequestFacebook(String accessToken) {
        this.accessToken = accessToken;
    }

}
