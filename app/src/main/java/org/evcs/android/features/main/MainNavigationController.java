package org.evcs.android.features.main;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;

import org.evcs.android.R;
import org.evcs.android.features.charging.ChargingNavigationController;
import org.evcs.android.navigation.controller.AbstractBaseFragmentNavigationController;
import org.evcs.android.navigation.controller.AbstractNavigationController;

public class MainNavigationController extends AbstractBaseFragmentNavigationController {

    private final static @IdRes Integer ROOT_ID = R.id.mainMapFragment;
    private static MainNavigationController mInstance;

    private final MainActivity mActivity;
    private AbstractNavigationController mCurrentController;
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

    public void goToCharging() {
        if (mIsInCharging) return;
        backToBaseFragment();
        mIsInCharging = true;
        mIsInProfile = false;
        ChargingNavigationController controller = new ChargingNavigationController(ROOT_ID, mNavController);
        controller.startFlow();
        mCurrentController = controller;
        mActivity.setSelectedItem(R.id.menu_drawer_charging);
    }

    public void goToProfile() {
        if (mIsInProfile) return;
        cancelSession(() -> {
            mIsInProfile = true;
            mIsInCharging = false;
            backToBaseFragment();
            navigate(R.id.profileFragment);
            mActivity.setSelectedItem(R.id.menu_drawer_profile);
        });
    }

    public void onMapClicked() {
        if (!mIsInCharging && !mIsInProfile) return;
        cancelSession(() -> {
            mIsInCharging = false;
            mIsInProfile = false;
            backToBaseFragment();
            mActivity.setSelectedItem(R.id.menu_drawer_map);
        });
    }

    protected void cancelSession(ChargingNavigationController.CancelSessionCallback c) {
        if (mIsInCharging) {
            ((ChargingNavigationController) mCurrentController)
                    .cancelSession(mActivity.getSupportFragmentManager(), c);
        } else {
            c.onSessionCanceled();
        }
    }

}
