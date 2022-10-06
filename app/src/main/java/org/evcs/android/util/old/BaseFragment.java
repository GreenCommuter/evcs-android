//package org.evcs.android.util.wolmo;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import androidx.annotation.CallSuper;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import ar.com.wolox.wolmo.core.fragment.IWolmoFragment;
//import ar.com.wolox.wolmo.core.permission.PermissionManager;
//import ar.com.wolox.wolmo.core.presenter.BasePresenter;
//
//public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements IWolmoFragment<T> {
//    private BaseFragmentHandler<T> mFragmentHandler = new BaseFragmentHandler(this);
//
//    public BaseFragment() {
//    }
//
//    @CallSuper
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.mFragmentHandler.onCreate(savedInstanceState);
//    }
//
//    @CallSuper
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return this.mFragmentHandler.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    @CallSuper
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        this.mFragmentHandler.onViewCreated(view, savedInstanceState);
//    }
//
//    @CallSuper
//    public void setMenuVisibility(boolean visible) {
//        super.setMenuVisibility(visible);
//        this.mFragmentHandler.setMenuVisibility(visible);
//    }
//
//    @CallSuper
//    public void onResume() {
//        super.onResume();
//        this.mFragmentHandler.onResume();
//    }
//
//    @CallSuper
//    public void onPause() {
//        super.onPause();
//        this.mFragmentHandler.onPause();
//    }
//
//    @CallSuper
//    public void onDestroyView() {
//        this.mFragmentHandler.onDestroyView();
//        super.onDestroyView();
//    }
//
//    @CallSuper
//    public void onDestroy() {
//        this.mFragmentHandler.onDestroy();
//        super.onDestroy();
//    }
//
//    @CallSuper
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionManager.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    public boolean handleArguments(@Nullable Bundle arguments) {
//        return true;
//    }
//
//    public void setUi(View v) {
//    }
//
//    public void setListeners() {
//    }
//
//    public void populate() {
//    }
//
//    public void onVisible() {
//    }
//
//    public void onHide() {
//    }
//
//    protected T getPresenter() {
//        return this.mFragmentHandler.getPresenter();
//    }
//
//    public boolean onBackPressed() {
//        return false;
//    }
//}
