package org.evcs.android.network.interceptor;

import androidx.annotation.NonNull;

import com.base.networking.retrofit.interceptor.ApiRestInterceptor;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.util.UserUtils;

import okhttp3.Request;

public class SecuredRequestInterceptor extends ApiRestInterceptor {

    private static final String SESSION_TOKEN_HEADER = "Authorization";
    private static final String MOBILE_APP_NAME_HEADER = "X-Mobile-App-Name";

    @Override
    public void addHeaders(@NonNull Request.Builder requestBuilder) {
        String organizationCode = EVCSApplication.getInstance().getResources().getString(R.string.app_name);
        requestBuilder.addHeader(MOBILE_APP_NAME_HEADER, organizationCode);
        String token = UserUtils.getSessionToken();
        if (token == null) {
            return;
        }
        requestBuilder.addHeader(SESSION_TOKEN_HEADER, token);
    }
}
