package org.evcs.android;

import android.content.Context;

import androidx.annotation.NonNull;

import com.base.core.BaseApplication;
import com.base.networking.retrofit.NetworkingApplication;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.rollbar.android.Rollbar;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.stripe.android.PaymentConfiguration;

import org.evcs.android.model.user.User;
import org.evcs.android.network.service.EVCSRetrofitServices;
import org.evcs.android.util.UserUtils;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class EVCSApplication extends NetworkingApplication {

    private static final String FULL_NAME_FORMAT = "%s, %s";
    private static final String NOT_LOGGED_USER_USERNAME = "User wasn't logged yet, so there isn't username";
    private static final String NOT_LOGGED_USER_ID = "User wasn't logged yet, so there isn't id";
    private static final String NOT_LOGGED_USER_EMAIL = "User wasn't logged yet, so there isn't email";

    private EVCSRetrofitServices mEvcsRetrofitServices;
    private RefWatcher mRefWatcher;

    @NonNull
    public static EVCSApplication getInstance() {
        return (EVCSApplication) BaseApplication.getInstance();
    }

    @Override
    public void onInit() {
        Rollbar.init(this, BaseConfiguration.ROLLBAR_CLIENT_ID, Configuration.ROLLBAR_ENVIRONMENT);

        User user = UserUtils.getLoggedUser();
        String id = user != null ? String.valueOf(user.getId()) : NOT_LOGGED_USER_ID;
        String email = user != null ? user.getEmail() : NOT_LOGGED_USER_EMAIL;

        Rollbar.setPersonData(id, null, email);
        Fresco.initialize(this);
        // We need to initialize the SystemService with the Application Context to avoid memory leaks
        // This is an Android issue, leaking context in the ConnectivityManager
        // For more information see: https://github.com/square/leakcanary/issues/393
        this.getSystemService(Context.CONNECTIVITY_SERVICE);

        PaymentConfiguration.init(getApplicationContext(), Configuration.STRIPE_KEY);
    }

    @Override
    public EVCSRetrofitServices getRetrofitServices() {
        if (mEvcsRetrofitServices == null) {
            mEvcsRetrofitServices = new EVCSRetrofitServices();
            mEvcsRetrofitServices.init();
        }
        return mEvcsRetrofitServices;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            mRefWatcher = LeakCanary.install(this);
        }
    }

    /**
     * Returns the LeakCanary {@link RefWatcher} for this application
     * @param context Context
     * @return RefWatcher to watch leaks
     */
    public static RefWatcher getRefWatcher(Context context) {
        return ((EVCSApplication) context.getApplicationContext()).mRefWatcher;
    }

}
