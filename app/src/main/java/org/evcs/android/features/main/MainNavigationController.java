package org.evcs.android.features.main;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;

import org.evcs.android.R;
import org.evcs.android.features.charging.ChargingNavigationController;
import org.evcs.android.navigation.controller.AbstractNavigationController;

public class MainNavigationController extends AbstractNavigationController {

    private static MainNavigationController mInstance;
    private final MainActivity mActivity;
    private AbstractNavigationController mCurrentController;
    private @IdRes Integer mRootId = R.id.mainMapFragment;
    private boolean mIsInCharging;
    private boolean mIsInProfile;

    public MainNavigationController(MainActivity mainActivity, boolean hasRoot, NavController navController) {
        super(navController);
        mActivity = mainActivity;
        mInstance = this;
    }

    public static MainNavigationController getInstance() {
        return mInstance;
    }

    @Override
    protected @IdRes int getStartingHistoryBuilder() {
        return mRootId;
    }

    public void startFlow() {
        replaceLastKey(R.id.mainMapFragment, null);
    }

    public void goToCharging() {
        if (mIsInCharging) return;
        mIsInCharging = true;
        mIsInProfile = false;
        startFlow();
        ChargingNavigationController controller = new ChargingNavigationController(mRootId, mNavController);
        controller.startFlow();
        mCurrentController = controller;
    }

    public void goToProfile() {
        if (mIsInProfile) return;
        mIsInProfile = true;
        mIsInCharging = false;
        startFlow();
        navigate(R.id.profileFragment);
    }

    public void onMapClicked() {
        if (!mIsInCharging && !mIsInProfile) return;
        mIsInCharging = false;
        mIsInProfile = false;
        mNavController.popBackStack(R.id.mainMapFragment, false);
        mActivity.getMenuView().setSelectedItemId(R.id.menu_drawer_map);
    }

}
