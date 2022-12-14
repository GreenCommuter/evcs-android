package org.evcs.android.network.interceptor;

import androidx.annotation.NonNull;

import com.base.networking.retrofit.interceptor.ApiRestInterceptor;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.util.UserUtils;
import org.evcs.android.util.VersionUtils;

import okhttp3.Request;

public class SecuredRequestInterceptor extends ApiRestInterceptor {

    private static final String SESSION_TOKEN_HEADER = "Authorization";
//    private static final String MOBILE_APP_NAME_HEADER = "X-Mobile-App-Name";
    private static final String MOBILE_APP_VERSION_HEADER = "X-app-version";

    @Override
    public void addHeaders(@NonNull Request.Builder requestBuilder) {
//        String organizationCode = EVCSApplication.getInstance().getResources().getString(R.string.app_name);
//        requestBuilder.addHeader(MOBILE_APP_NAME_HEADER, organizationCode);
        requestBuilder.addHeader(MOBILE_APP_VERSION_HEADER, VersionUtils.getversion(
                EVCSApplication.getInstance().getApplicationContext()
        ));
        String token = UserUtils.getSessionToken();
        if (token == null) {
            return;
        }
        requestBuilder.addHeader(SESSION_TOKEN_HEADER, token);
    }
}
