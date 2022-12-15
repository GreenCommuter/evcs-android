package org.evcs.android.features.profile.wallet;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;

import org.evcs.android.R;
import org.evcs.android.navigation.controller.AbstractBaseFragmentNavigationController;

public class WalletNavigationController extends AbstractBaseFragmentNavigationController implements
        WalletFragment.IPaymentMethodsFragmentListener,
        AddCreditCardFragment.IBrainTreeListener {

    private static WalletNavigationController mInstance;

    public WalletNavigationController(NavController mainController) {
        super(mainController, mainController.getCurrentBackStackEntry().getDestination().getId());
        mInstance = this;
    }

    public static WalletNavigationController getInstance() {
        return mInstance;
    }

    @Override
    public @IdRes int getStartingHistoryBuilder() {
        return R.id.walletFragment;
    }

    @Override
    public void onAddCreditCardFragmentFinished() {
        mNavController.popBackStack();
    }

}
