package org.evcs.android.model.user;

import com.google.gson.annotations.SerializedName;

public class UserRequestGoogle {

    private final String email;
    @SerializedName("idToken")
    private final String idToken;
    @SerializedName("accessToken")
    private final String accessToken;

    public UserRequestGoogle(String email, String idToken, String accessToken) {
        this.email = email;
        this.idToken = idToken;
        this.accessToken = accessToken;
    }

    public class Tokens {

        private String idToken;
        private String accessToken;

        public String getIdToken() {
            return idToken;
        }

        public String getAccessToken() {
            return accessToken;
        }
    }

    public class ACCESS_TOKEN_REQUEST_FIELDS {
        public static final String URL = "https://www.googleapis.com/oauth2/v4/token";
        public static final String GRANT_TYPE = "authorization_code";
        public static final String CLIENT_ID = "624176431714-r3q0rn4ik8fhakf4ge6pi82frtnhoe0u.apps.googleusercontent.com";
        public static final String CLIENT_SECRET = "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";
    }

}
