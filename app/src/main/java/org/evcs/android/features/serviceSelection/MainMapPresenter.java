package org.evcs.android.features.serviceSelection;

import com.base.maps.IMapPresenter;
import com.base.networking.retrofit.RetrofitServices;

import org.evcs.android.network.service.presenter.MultipleRequestsManager;
import org.evcs.android.network.service.presenter.ServicesPresenter;

public class MainMapPresenter extends ServicesPresenter<IChooseServiceView> implements IMapPresenter {

    private static final String TAG = "ChooseServicePresenter";
    private final MultipleRequestsManager mMultipleRequestsManager;

    public MainMapPresenter(IChooseServiceView viewInstance, RetrofitServices services) {
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

    @Override
    public void onMapReady() {

    }

    @Override
    public void onMapDestroyed() {

    }
}
