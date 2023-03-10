package org.evcs.android.features.charging;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.features.main.MainNavigationController;
import org.evcs.android.features.shared.EVCSDialogFragment;
import org.evcs.android.model.PaymentMethod;
import org.evcs.android.model.Session;
import org.evcs.android.navigation.controller.AbstractBaseFragmentNavigationController;
import org.evcs.android.util.Extras;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ChargingNavigationController extends AbstractBaseFragmentNavigationController {

    private static ChargingNavigationController mInstance;
    private ChangePaymentMethodFragment.PaymentMethodChangeListener mPaymentMethodChangeListener;
    private boolean mIsActiveSession;

    public ChargingNavigationController(@IdRes int rootId, NavController navController) {
        super(navController, rootId);
        mInstance = this;
    }

    public static ChargingNavigationController getInstance() {
        return mInstance;
    }

    @Override
    protected @IdRes int getStartingHistoryBuilder() {
        return R.id.chargingFragment;
    }

//    public void startFlow() {
//        super.startFlow();
//    }

    public void goToPlanInfo(String id) {
        Bundle args = new Bundle();
        args.putString(Extras.PlanInfo.STATION_ID, id);
        navigate(R.id.planInfoFragment, args);
    }

    public void goToChangePaymentMethods(@Nullable ArrayList<PaymentMethod> paymentMethods) {
        Bundle args = new Bundle();
        args.putSerializable(Extras.ChangePaymentMethod.PAYMENT_METHODS, paymentMethods);
        navigate(R.id.changePaymentMethodFragment, args);
    }

    public void onPaymentMethodChanged(@NotNull PaymentMethod paymentMethod) {
        mPaymentMethodChangeListener.onPaymentMethodChanged(paymentMethod);
        mNavController.popBackStack();
    }

    public void setPaymentMethodChangeListener(@NotNull ChangePaymentMethodFragment.PaymentMethodChangeListener listener) {
        mPaymentMethodChangeListener = listener;
    }

    public void goToStartCharging(int stationId, @Nullable String pmId, @Nullable ArrayList<String> coupons) {
        Bundle args = new Bundle();
        args.putInt(Extras.StartCharging.STATION_ID, stationId);
        args.putString(Extras.StartCharging.PM_ID, pmId);
        args.putSerializable(Extras.StartCharging.COUPONS, coupons);
        replaceLastKey(R.id.startChargingFragment, args);
    }

    public void onChargingStarted() {
        replaceLastKey(R.id.chargingInProgressFragment, null);
    }

    public void cancelSession(FragmentManager fm) {
        cancelSession(fm, () -> mNavController.popBackStack());
    }

    public void cancelSession(FragmentManager fm, CancelSessionCallback c) {
        if (mIsActiveSession) {
            new EVCSDialogFragment.Builder()
                    .setTitle(EVCSApplication.getInstance().getString(R.string.plan_info_dialog_cancel))
                    .addButton(EVCSApplication.getInstance().getString(R.string.app_yes),
                            fragment -> {
                                    mIsActiveSession = false;
                                    fragment.dismiss();
                                    c.onSessionCanceled();
                            })
                    .showCancel(true)
                    .show(fm);
        } else {
            c.onSessionCanceled();
        }
    }

    // Needed to update the item in the menu
    public void finish() {
        MainNavigationController.getInstance().onMapClicked();
    }

    public void onChargingStarted(@NotNull Session response) {
        Bundle args = new Bundle();
        args.putSerializable(Extras.StartCharging.SESSION, response);
        replaceLastKey(R.id.chargingInProgressFragment, args);
    }

    public void setActiveSession() {
        mIsActiveSession = true;
    }

    public interface CancelSessionCallback {
        void onSessionCanceled();
    }
}