package org.evcs.android.activity;

import android.text.TextUtils;

import com.base.networking.retrofit.RetrofitServices;

import org.evcs.android.network.service.presenter.ServicesPresenter;
import org.evcs.android.util.UserUtils;

/**
 * This presenter checks if user is logged and you can add method if you need.
 */
public class RootActivityPresenter extends ServicesPresenter<RootActivity> {

    public RootActivityPresenter(RootActivity viewInstance, RetrofitServices services) {
        super(viewInstance, services);
        getWordings();
    }

    /**
     * This method checks if a user is logged.
     * @return true if the user is logged.
     */
    public boolean isLoggedUser() {
        return !TextUtils.isEmpty(UserUtils.getSessionToken());
    }

    public void getWordings() {
    }
}
