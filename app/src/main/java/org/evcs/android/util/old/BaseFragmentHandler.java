//package org.evcs.android.util.wolmo;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import ar.com.wolox.wolmo.core.R.string;
//import ar.com.wolox.wolmo.core.fragment.IWolmoFragment;
//import ar.com.wolox.wolmo.core.presenter.BasePresenter;
//import ar.com.wolox.wolmo.core.util.ToastUtils;
//
//class BaseFragmentHandler<T extends BasePresenter> {
//    private static final String TAG = "WolmoFragmentHandler";
//    private final Fragment mFragment;
//    private final IWolmoFragment<T> mBaseFragment;
//    private boolean mCreated;
//    private T mPresenter;
//    private boolean mMenuVisible;
//    private boolean mVisible;
//
//    BaseFragmentHandler(@NonNull IWolmoFragment<T> baseFragment) {
//        if (!(baseFragment instanceof Fragment)) {
//            throw new IllegalArgumentException("WolmoFragment should be a Fragment instance");
//        } else {
//            this.mFragment = (Fragment)baseFragment;
//            this.mBaseFragment = baseFragment;
//        }
//    }
//
//    void onCreate(@Nullable Bundle savedInstanceState) {
//        if (this.mBaseFragment.handleArguments(this.mFragment.getArguments())) {
//            this.mPresenter = this.mBaseFragment.createPresenter();
//        } else {
//            Log.e("BaseFragmentHandler", this.mFragment.getClass().getSimpleName() + " - The fragment's handleArguments returned false.");
//            ToastUtils.show(string.unknown_error);
//            this.mFragment.getActivity().finish();
//        }
//
//    }
//
//    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(this.mBaseFragment.layout(), container, false);
//        this.mBaseFragment.setUi(v);
//        this.mBaseFragment.init();
//        this.mBaseFragment.populate();
//        this.mBaseFragment.setListeners();
//        this.mCreated = true;
//        return v;
//    }
//
//    void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        if (this.getPresenter() != null) {
//            this.getPresenter().onViewCreated();
//        }
//
//    }
//
//    @Nullable
//    T getPresenter() {
//        return this.mPresenter;
//    }
//
//    private void onVisibilityChanged() {
//        if (this.mCreated) {
//            if (this.mFragment.isResumed() && this.mMenuVisible && !this.mVisible) {
//                this.mBaseFragment.onVisible();
//                this.mVisible = true;
//            } else if ((!this.mMenuVisible || !this.mFragment.isResumed()) && this.mVisible) {
//                this.mBaseFragment.onHide();
//                this.mVisible = false;
//            }
//
//        }
//    }
//
//    void onResume() {
//        this.onVisibilityChanged();
//    }
//
//    void onPause() {
//        this.onVisibilityChanged();
//    }
//
//    void setMenuVisibility(boolean visible) {
//        this.mMenuVisible = visible;
//        this.onVisibilityChanged();
//    }
//
//    void onDestroyView() {
//        if (this.getPresenter() != null) {
//            this.getPresenter().onViewDestroyed();
//        }
//    }
//
//    void onDestroy() {
//        if (this.getPresenter() != null) {
//            this.getPresenter().detachView();
//        }
//
//    }
//}
