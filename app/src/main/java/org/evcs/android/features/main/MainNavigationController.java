package org.evcs.android.features.main;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;

import org.evcs.android.R;
import org.evcs.android.navigation.controller.AbstractNavigationController;

public class MainNavigationController extends AbstractNavigationController {

    private static MainNavigationController mInstance;
    private final MainActivity mActivity;
    private AbstractNavigationController mCurrentController;
    private @IdRes Integer mRootId;
    private boolean mIsInCharging;

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
        return R.id.mainMapFragment;
    }

    public void startFlow() {
        replaceLastKey(R.id.mainMapFragment, null);
    }

    public void goToCharging() {
        if (mIsInCharging) return;
        mIsInCharging = true;
        navigate(R.id.chargingFragment);
    }

    public void onMapClicked() {
        if (!mIsInCharging) return;
        mIsInCharging = false;
        mNavController.popBackStack();
        mActivity.getMenuView().setSelectedItemId(R.id.menu_drawer_map);
    }
}
