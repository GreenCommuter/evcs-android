package org.evcs.android.features.serviceSelection;

import com.base.networking.retrofit.RetrofitServices;

import org.evcs.android.network.service.presenter.MultipleRequestsManager;
import org.evcs.android.network.service.presenter.ServicesPresenter;

public class ChooseServicePresenter extends ServicesPresenter<IChooseServiceView> {

    private static final String TAG = "ChooseServicePresenter";
    private final MultipleRequestsManager mMultipleRequestsManager;

    public ChooseServicePresenter(IChooseServiceView viewInstance, RetrofitServices services) {
        super(viewInstance, services);
        mMultipleRequestsManager = new MultipleRequestsManager(this);
    }

    /**
     * Returns the current logged user from API
     *
     * @return Current logged user
     */
    public void getLoggedUser() {

    }

}
