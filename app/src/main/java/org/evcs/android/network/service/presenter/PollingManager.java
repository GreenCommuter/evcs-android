package org.evcs.android.network.service.presenter;

import android.os.Handler;

import androidx.annotation.Nullable;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.Configuration;
import org.evcs.android.network.callback.AuthCallback;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Presenter class which allows us to periodically retry a request while a condition is not met.
 */
public class PollingManager {

    private final ServicesPresenter mPresenter;

    public PollingManager(ServicesPresenter presenter) {
        mPresenter = presenter;
    }

    public void poll(Call call, PollingCallback callback) {
        poll(call, callback, BaseConfiguration.REQUEST_TRIES, BaseConfiguration.REQUEST_TRIES_DELAY);
    }

    public <T> void poll(final Call<T> call, final PollingCallback callback, final int tries, final int delay) {
        if (tries == 0) {
            callback.onCallFailure();
            return;
        }
        call.enqueue(new AuthCallback<T>(mPresenter) {

            private Response mResponse;

            @Override
            public void onResponse(final Call<T> call, @Nullable Response<T> response) {
                if (callback.shouldRetry(response)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            poll(call.clone(), callback, tries - 1, delay);
                        }
                    }, delay);
                } else {
                    mResponse = response;
                    super.onResponse(call, response);
                }
            }

            @Override
            public void onResponseSuccessful(T o) {
                callback.onResponseSuccessful(mResponse);
            }

            @Override
            public void onResponseFailed(ResponseBody responseBody, int i) {
                callback.onResponseFailed(responseBody, i);
            }

            @Override
            public void onCallFailure(Throwable throwable) {
                callback.onCallFailure();
            }
        });
    }

    public interface PollingCallback {

        void onResponseSuccessful(Response response);

        void onResponseFailed(ResponseBody responseBody, int responseCode);

        void onCallFailure();

        boolean shouldRetry(Response response);
    }

}
