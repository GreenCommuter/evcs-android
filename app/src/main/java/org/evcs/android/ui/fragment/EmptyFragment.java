package org.evcs.android.ui.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.core.fragment.BaseFragment;
import com.base.core.presenter.BasePresenter;

import org.evcs.android.R;

/**
 * Fragment to use as a placeholder while the Navigation Controller loads user information.
 */
public class EmptyFragment extends BaseFragment {

    public static EmptyFragment newInstance() {
        return new EmptyFragment();
    }

    @Override
    public int layout() {
        return R.layout.fragment_base;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void init() {}

}
