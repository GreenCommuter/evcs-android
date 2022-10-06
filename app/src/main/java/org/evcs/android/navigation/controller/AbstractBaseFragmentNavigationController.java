package org.evcs.android.navigation.controller;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

public abstract class AbstractBaseFragmentNavigationController extends AbstractNavigationController {

    private final @Nullable @IdRes Integer mBaseFragment;

    public AbstractBaseFragmentNavigationController(NavController mainNavigationController, @Nullable @IdRes Integer baseFragment) {
        super(mainNavigationController);
        mBaseFragment = baseFragment;
    }

    @Override
    @CallSuper
    public void startFlow() {
        backToBaseFragment();
        super.startFlow();
    }

    public boolean isOnFirstFragment() {
        if (mBaseFragment == null) {
            return super.isOnFirstFragment();
        } else {
            return mNavController.getPreviousBackStackEntry() != null
                    && mNavController.getPreviousBackStackEntry().getDestination().getId() == mBaseFragment;
        }

    }

    protected void backToBaseFragment() {
        goBackUntil(mBaseFragment == null ? 0 : mBaseFragment);
    }

    protected void goBackUntil(@IdRes int id) {
        mNavController.popBackStack(id, false);
    }

}
