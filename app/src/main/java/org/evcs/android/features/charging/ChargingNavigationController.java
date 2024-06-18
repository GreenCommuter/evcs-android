package org.evcs.android.features.charging;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;

import org.evcs.android.R;
import org.evcs.android.model.Session;
import org.evcs.android.navigation.controller.AbstractNavigationController;
import org.evcs.android.util.Extras;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ChargingNavigationController extends AbstractNavigationController {

    private static ChargingNavigationController mInstance;

    public ChargingNavigationController(NavController navController) {
        super(navController);
        mInstance = this;
    }

    public static ChargingNavigationController getInstance() {
        return mInstance;
    }

    @Override
    protected @IdRes int getStartingHistoryBuilder() {
        return R.id.placeholder;
    }

//    public void startFlow() {
//        super.startFlow();
//    }

    public void goToPlanInfo(String id, boolean fromQR, FragmentManager fragmentManager) {
        Bundle args = new Bundle();
        args.putString(Extras.PlanInfo.STATION_ID, id);
        args.putBoolean(Extras.PlanInfo.FROM_QR, fromQR);
        navigate(R.id.planInfoFragment, args);
    }

    public void goToStartCharging(int stationId, @Nullable String pmId, @Nullable ArrayList<String> coupons) {
        Bundle args = startChargingArgs(stationId, pmId, coupons);
        replaceLastKey(R.id.startChargingFragment, args);
    }

    public void onChargingStarted() {
        replaceLastKey(R.id.chargingInProgressFragment, null);
    }

    public void onChargingStarted(@NotNull Session response) {
        Bundle args = new Bundle();
        args.putSerializable(Extras.StartCharging.SESSION, response);
        replaceLastKey(R.id.chargingInProgressFragment, args);
    }

    public void onSessionFinished(@NonNull Session session) {
        Bundle args = new Bundle();
        args.putSerializable(Extras.SessionInformationActivity.CHARGE, session);
        args.putSerializable(Extras.SessionInformationActivity.CHARGE_ID, session.getId());
        replaceLastKey(R.id.sessionInformationFragment, args);
    }

    public void goToOverLimitWarning(int stationId, @Nullable String pmId, @Nullable ArrayList<String> coupons) {
        Bundle args = startChargingArgs(stationId, pmId, coupons);
        navigate(R.id.overLimitWarningFragment, args);
    }

    private Bundle startChargingArgs(int stationId, @Nullable String pmId, @Nullable ArrayList<String> coupons) {
        Bundle args = new Bundle();
        args.putInt(Extras.StartCharging.STATION_ID, stationId);
        args.putString(Extras.StartCharging.PM_ID, pmId);
        args.putSerializable(Extras.StartCharging.COUPONS, coupons);
        return args;
    }

    public void goToTrialReminder(int stationId, @Nullable String pmId, @Nullable ArrayList<String> coupons) {
        Bundle args = startChargingArgs(stationId, pmId, coupons);
        navigate(R.id.freeTrialReminderFragment, args);
    }
}
