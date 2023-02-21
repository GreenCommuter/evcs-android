package org.evcs.android.features.charging;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;

import org.evcs.android.R;
import org.evcs.android.model.PaymentMethod;
import org.evcs.android.navigation.controller.AbstractBaseFragmentNavigationController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ChargingNavigationController extends AbstractBaseFragmentNavigationController {

    private static ChargingNavigationController mInstance;
    private ChangePaymentMethodFragment.PaymentMethodChangeListener mPaymentMethodChangeListener;

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

    public void goToPlanInfo(int id) {
        Bundle args = new Bundle();
        args.putInt("station_id", id);
        navigate(R.id.planInfoFragment, args);
    }

    public void goToChangePaymentMethods(@Nullable ArrayList<PaymentMethod> paymentMethods) {
        Bundle args = new Bundle();
        args.putSerializable("payment_methods", paymentMethods);
        navigate(R.id.changePaymentMethodFragment, args);
    }

    public void onPaymentMethodChanged(@NotNull PaymentMethod paymentMethod) {
        mPaymentMethodChangeListener.onPaymentMethodChanged(paymentMethod);
        mNavController.popBackStack();
    }

    public void setPaymentMethodChangeListener(@NotNull ChangePaymentMethodFragment.PaymentMethodChangeListener listener) {
        mPaymentMethodChangeListener = listener;
    }
}