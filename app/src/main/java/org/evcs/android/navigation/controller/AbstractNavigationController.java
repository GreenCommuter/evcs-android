package org.evcs.android.navigation.controller;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import org.evcs.android.R;
/**
 * The navigation controller helps to simplify the navigation between fragments. It listen to
 * fragments callbacks and manages which fragment should be loaded next.
 * <p>
 * It's based on a {@link Fragment} and uses the Fragment's child {@link
 * FragmentManager}.
 */
public abstract class AbstractNavigationController {

    protected NavController mNavController;

    public AbstractNavigationController(NavController mainNavigationController) {
        mNavController = mainNavigationController;
    }

    public void startFlow() {
        navigate(getStartingHistoryBuilder());
    }

    private NavOptions.Builder getAnimations() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.animation_slide_in_left)
                .setExitAnim(R.anim.animation_slide_out_left)
                .setPopEnterAnim(R.anim.animation_slide_in_right)
                .setPopExitAnim(R.anim.animation_slide_out_right);
    }

    /**
     * Replaces the last fragment with the given key.
     */
    protected void replaceLastKey(@IdRes int id, Bundle args) {
//        NavOptions navOptions = getAnimations().setPopUpTo(mNavController.getCurrentDestination().getId(), true)
//                .build();
        mNavController.navigate(id, args, replaceLastNavOptions(mNavController));
    }

    public static NavOptions replaceLastNavOptions(NavController controller) {
        return new NavOptions.Builder().setPopUpTo(controller.getCurrentDestination().getId(), true)
                .build();
    }

    protected void navigate(@IdRes int action) {
        navigate(action, null);
    }

    protected void navigate(@IdRes int action, @Nullable Bundle args) {
        NavOptions navOptions = getAnimations().build();
        mNavController.navigate(action, args, navOptions);
    }

    /**
     * Returns true if the controller is on the initial fragment.
     *
     * @return <b>true</b> if the initial fragment is visible. <b>false</b> otherwise
     */
    public boolean isOnFirstFragment() {
        return mNavController.getPreviousBackStackEntry() == null;
    }

    protected abstract @IdRes int getStartingHistoryBuilder();

}
