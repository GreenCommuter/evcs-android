package org.evcs.android.features.shared;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.base.core.fragment.BaseDialogFragment;
import com.base.core.presenter.BasePresenter;

/**
 * Not actually a singleton, but makes sure that the dialog is only opened once.
 */
public abstract class SingletonDialog<T extends BasePresenter> extends BaseDialogFragment<T> {

    private static Class<? extends SingletonDialog> isShowingDialog;
    private T mPresenter;

    //This is to prevent multiple dialogs from showing when clicking many times
    @Override
    public void show(FragmentManager fm) {
        if (!(isShowingDialog == getClass())) {
            isShowingDialog = getClass();
            super.show(fm);
        }
    }

    @Override
    public void dismiss() {
        isShowingDialog = null;
        super.dismiss();
    }

    //dismiss is not always called
    @Override
    public void onPause() {
        isShowingDialog = null;
        super.onPause();
    }

    //fix for super never creating presenter
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    @Override
    protected T getPresenter() {
        return mPresenter;
    }
}
