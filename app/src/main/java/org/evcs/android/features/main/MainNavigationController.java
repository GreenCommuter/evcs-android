package org.evcs.android.features.main;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;

import org.evcs.android.R;
import org.evcs.android.features.serviceSelection.MainMapFragment;
import org.evcs.android.navigation.controller.AbstractNavigationController;

public class MainNavigationController extends AbstractNavigationController
        implements MainMapFragment.IMainMapListener {

    private static MainNavigationController mInstance;
    private final MainActivity mActivity;
    private AbstractNavigationController mCurrentController;
    private final boolean mHasRoot;
    private @IdRes Integer mRootId;

    public MainNavigationController(MainActivity mainActivity, boolean hasRoot, NavController navController) {
        super(navController);
        mActivity = mainActivity;
        mHasRoot = hasRoot;
        mInstance = this;
    }

    public static MainNavigationController getInstance() {
        return mInstance;
    }

    @Override
    protected @IdRes int getStartingHistoryBuilder() {
        return R.id.chooseServiceFragment;
    }

    public void startFlow() {
        if (mHasRoot) {
            mRootId = getStartingHistoryBuilder();
            replaceLastKey(getStartingHistoryBuilder(), null);
        } else {
            onCarSharingSelected(false);
        }
    }


    @Override
    public void onCarSharingSelected(boolean skipPlaceholder) {
    }

}
