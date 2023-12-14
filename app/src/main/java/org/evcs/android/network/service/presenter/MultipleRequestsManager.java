package org.evcs.android.network.service.presenter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.networking.retrofit.callback.NetworkCallback;

import org.evcs.android.network.callback.AuthCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Presenter class which allows for parallel requests and provides a callback for when they all finish.
 */
public class MultipleRequestsManager {

    private Map<Call, NetworkCallback> mCalls = new HashMap<>();
    private Map<Call, Boolean> mFinishedCalls = new HashMap<>();
    private @NonNull ServicesPresenter mPresenter;
    private @Nullable MultipleRequestsCallback mCallback;

    public MultipleRequestsManager(@NonNull ServicesPresenter presenter) {
        this.mPresenter = presenter;
    }

    public <W> void addRequest(final Call<W> call1, final NetworkCallback<W> callback) {
        Call<W> call = call1.clone();
        NetworkCallback<W> presenterCallback = new AuthCallback<W>(mPresenter) {
            @Override
            public void onResponseSuccessful(W response) {
                callback.onResponseSuccessful(response);
                onRequestFinished(call);
            }

            @Override
            public void onResponseFailed(ResponseBody responseBody, int i) {
                callback.onResponseFailed(responseBody, i);
                onRequestFinished(call);
            }

            @Override
            public void onCallFailure(Throwable throwable) {
                callback.onCallFailure(throwable);
                onRequestFinished(call);
            }
        };
        mCalls.put(call, presenterCallback);
        mFinishedCalls.put(call, false);
    }

    private void onRequestFinished(Call call) {
        mFinishedCalls.put(call, true);
        if (!(mFinishedCalls.values()).contains(false)) {
            onAllRequestsFinished();
        }
    }

    public void fireRequests(@Nullable MultipleRequestsCallback callback) {
        for (Call call : mCalls.keySet()) {
            call.enqueue(mCalls.get(call));
        }
        mCallback = callback;
    }

    private void onAllRequestsFinished() {
        mFinishedCalls = new HashMap<>();
        mCalls = new HashMap<>();
        if (mCallback != null)
            mCallback.onAllRequestsFinished();
    }

    public interface MultipleRequestsCallback {
        void onAllRequestsFinished();
    }
}
