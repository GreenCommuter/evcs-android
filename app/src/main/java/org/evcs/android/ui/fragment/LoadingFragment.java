package org.evcs.android.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.core.fragment.BaseFragment;
import com.base.core.presenter.BasePresenter;
import com.base.core.util.KeyboardUtils;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.util.SpinnerUtils;

/**
 * Abstract Fragment with a Loading progress dialog. Use it on fragments which need to make network
 * request and show a dialog.
 *
 * @param <T> Presenter
 */
public abstract class LoadingFragment<T extends BasePresenter> extends BaseFragment<T> {

    private Dialog mProgressDialog;

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Progress dialog
        mProgressDialog = SpinnerUtils.getNewProgressDialog(getContext(), getProgressDialogLayout());
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        EVCSApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Override
    @CallSuper
    public void onStop() {
        hideProgressDialog();
        super.onStop();
    }

    /**
     * Show progress dialog
     */
    protected void showProgressDialog() {
        getProgressDialog().show();
        if (getView() != null) {
            KeyboardUtils.hideKeyboard(getContext(), getView());
        }
    }

    /**
     * Hide progress dialog
     */
    protected void hideProgressDialog() {
        getProgressDialog().dismiss();
    }

    /**
     * Get ProgressDialog instance
     *
     * @return Progress dialog
     */
    @NonNull
    protected final Dialog getProgressDialog() {
        return mProgressDialog;
    }

    protected @LayoutRes int getProgressDialogLayout() {
        return R.layout.spinner_layout;
    }
}
