package org.evcs.android.network.callback;

import com.base.core.presenter.BasePresenter;
import com.base.networking.retrofit.callback.NetworkCallback;

import org.evcs.android.util.UserUtils;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Adds auth error handling to NetworkCallback. When there is an auth error, this callbacks clean the
 * shared preferences to delete the token and current user.
 * This class checks if the View is attached to call the callbacks to the presenter.
 */
public abstract class AuthCallback<T> extends NetworkCallback<T> {

    private final BasePresenter mPresenter;

    /**
     * Constructor
     * @param presenter Parent presenter to check if the View is attached
     */
    public AuthCallback(BasePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (this.isAuthError(response)) {
            this.handleAuthError(response);
        } else if (response.isSuccessful() && mPresenter.isViewAttached() && mPresenter.isViewCreated()) {
            // it uses attached and created because the view can be attached and not be created by androidBase implementation.
            this.onResponseSuccessful(response.body());
        } else if (mPresenter.isViewAttached() && mPresenter.isViewCreated()) {
            this.onResponseFailed(response.errorBody(), response.code());
        }
    }

    @Override
    protected boolean isAuthError(Response<T> response) {
        return response.code() == 401;
    }

    @Override
    protected final void handleAuthError(Response<T> response) {
        UserUtils.logout(null);
    }
}
