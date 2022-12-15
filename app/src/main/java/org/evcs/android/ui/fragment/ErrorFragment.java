package org.evcs.android.ui.fragment;

import androidx.annotation.NonNull;

import com.base.core.fragment.BaseFragment;
import com.base.core.presenter.BasePresenter;
import com.base.core.util.ToastUtils;

import org.evcs.android.model.shared.RequestError;
import org.evcs.android.ui.view.shared.IErrorView;
import org.evcs.android.ui.view.shared.INetworkErrorView;

/**
 * Abstract Fragment for the default handling of errors .
 *
 * @param <T> Presenter
 */
public abstract class ErrorFragment<T extends BasePresenter> extends LoadingFragment<T> implements IErrorView, INetworkErrorView {

    private boolean mNetworkErrorHandled;

    @Override
    public void showError(@NonNull RequestError requestError) {
        hideProgressDialog();
        showError(this, requestError);
    }

    //For cases when this can't be extended
    public static void showError(BaseFragment fragment, @NonNull RequestError requestError) {
        ToastUtils.show(requestError.getBody());
    }

    @Override
    public void onNetworkError() {
        if (!mNetworkErrorHandled) {
            mNetworkErrorHandled = true;
        }
    }
}
