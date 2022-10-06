package org.evcs.android.features.main;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;

import org.evcs.android.R;
import org.evcs.android.features.serviceSelection.ChooseServiceFragment;
import org.evcs.android.navigation.controller.AbstractNavigationController;
import org.evcs.android.ui.drawer.AbstractDrawerActivity;

public class MainNavigationController extends AbstractNavigationController
        implements ChooseServiceFragment.IChooseServiceListener {

    private static MainNavigationController mInstance;
    private final AbstractDrawerActivity mActivity;
    private AbstractNavigationController mCurrentController;
    private final boolean mHasRoot;
    private @IdRes Integer mRootId;

    public MainNavigationController(AbstractDrawerActivity mainActivity, boolean hasRoot, NavController navController) {
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
