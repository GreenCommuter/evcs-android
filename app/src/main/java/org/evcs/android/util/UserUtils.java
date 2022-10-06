package org.evcs.android.util;

import android.text.TextUtils;

import com.base.core.util.NavigationUtils;
import com.base.networking.retrofit.serializer.BaseGsonBuilder;
import com.google.gson.Gson;

import org.evcs.android.EVCSApplication;
import org.evcs.android.features.auth.initialScreen.AuthActivity;
import org.evcs.android.model.user.AuthUser;
import org.evcs.android.model.user.User;
import org.evcs.android.network.callback.AuthCallback;
import org.evcs.android.network.serializer.DateTimeDeserializer;
import org.joda.time.DateTime;

public final class UserUtils {
    private static final String USER_SESSION_PREF = "UserSessionToken";
    private static final String USER_PREF = "UserKey";
    public static final String USER_EMAIL_PREF = "UserEmail";

    // Cache data
    private static String sToken;
    private static User sUser;

    /**
     * Empty Constructor
     */
    private UserUtils() {}

    /**
     * Saves the user, the email and the token in shared preferences. The email is saved separately
     * to prevent it from being cleared on logout
     *
     * @param authUser User with token and email to save
     */
    public static void saveAuthUser(AuthUser authUser) {
        sToken = authUser.getSessionToken();
        StorageUtils.storeInSharedPreferences(USER_SESSION_PREF, authUser.getSessionToken());
        StorageUtils.storeInSharedPreferences(USER_EMAIL_PREF, authUser.getEmail());
        saveUser(authUser);
    }

    /**
     * Saves the current user in shared preferences
     *
     * @param user User
     */
    public static void saveUser(User user) {
        sUser = user;
        Gson gson = BaseGsonBuilder.getBaseGsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeDeserializer()).create();
        StorageUtils.storeInSharedPreferences(USER_PREF, gson.toJson(user));
        StorageUtils.storeInSharedPreferences(USER_EMAIL_PREF, user.getEmail());
    }

    /**
     * Returns the user's session token.
     *
     * @return Session token
     */
    public static String getSessionToken() {
        if (sToken == null) {
            sToken = StorageUtils.getStringFromSharedPreferences(USER_SESSION_PREF, null);
        }
        return sToken;
    }

    public static String getUserEmail() {
        return StorageUtils.getStringFromSharedPreferences(USER_EMAIL_PREF, null);
    }

    /**
     * Get the current user.
     *
     * @return current user
     */
    public static User getLoggedUser() {
        if (sUser == null) {
            Gson gson = BaseGsonBuilder.getBaseGsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeDeserializer()).create();
            String userJson = StorageUtils.getStringFromSharedPreferences(USER_PREF, "");

            if (!TextUtils.isEmpty(userJson)) {
                sUser = gson.fromJson(userJson, User.class);
            }
        }

        return sUser;
    }

    /**
     * Removes the current user from shared preferences.
     */
    public static void logout(final AuthCallback<Void> authCallback) {
    }

    private static void clearKeys() {
        sUser = null;
        sToken = null;
        StorageUtils.clearKey(USER_PREF);
        StorageUtils.clearKey(USER_SESSION_PREF);

        // Jump to main screen
        NavigationUtils.jumpToClearingTask(EVCSApplication.getInstance(), AuthActivity.class);
    }

    /**
     * Returns the current user's ID.
     *
     * @return User id
     */
    public static int getUserId() {
        return getLoggedUser() == null ? 0 : getLoggedUser().getId() ;
    }

}

