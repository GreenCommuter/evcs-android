package org.evcs.android.features.main;

import android.annotation.SuppressLint;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;

import com.base.core.util.NavigationUtils;

import org.evcs.android.R;
import org.evcs.android.navigation.controller.AbstractBaseFragmentNavigationController;

public class MainNavigationController extends AbstractBaseFragmentNavigationController {

    private final static @IdRes Integer ROOT_ID = R.id.mainMapFragment;
    private static MainNavigationController mInstance;

    private final MainActivity mActivity;
    private boolean mIsInCharging;
    private boolean mIsInProfile;

    public MainNavigationController(MainActivity mainActivity, boolean hasRoot, NavController navController) {
        super(navController, ROOT_ID);
        mActivity = mainActivity;
        mInstance = this;
    }

    public static MainNavigationController getInstance() {
        return mInstance;
    }

    @Override
    protected @IdRes int getStartingHistoryBuilder() {
        return ROOT_ID;
    }

    public void goToPreCharging() {
        if (mIsInCharging) return;
        navigate(R.id.preChargingFragment);
    }

    public void selectCharging() {
        if (mIsInCharging) return;
//        backToBaseFragment();
        mIsInCharging = true;
        mIsInProfile = false;
        mActivity.setSelectedItem(R.id.menu_drawer_charging);
    }

    public void goToProfile() {
        if (mIsInProfile) return;
//        cancelSession(() -> {
            mIsInProfile = true;
            mIsInCharging = false;
            backToBaseFragment();
            navigate(R.id.profileFragment);
            mActivity.setSelectedItem(R.id.menu_drawer_profile);
//        });
    }

    public void onMapClicked() {
        if (mNavController.findDestination(R.id.mainMapFragment) == null) {
            NavigationUtils.jumpToClearingTask(mActivity, MainActivity.class);
            return;
        }
        if (!mIsInCharging && !mIsInProfile) return;
//        cancelSession(() -> {
            mIsInCharging = false;
            mIsInProfile = false;
            mActivity.setSelectedItem(R.id.menu_drawer_map);
//        });
            backToBaseFragment();
    }

    @SuppressLint("MissingSuperCall")
    public void startFlow() {
        replaceLastKey(getStartingHistoryBuilder(), null);
        mActivity.setSelectedItem(R.id.menu_drawer_map);
    }

}
