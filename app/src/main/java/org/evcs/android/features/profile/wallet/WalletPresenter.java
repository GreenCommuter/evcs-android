package org.evcs.android.features.profile.wallet;

import com.base.networking.retrofit.RetrofitServices;

import org.evcs.android.network.service.presenter.ServicesPresenter;

public class WalletPresenter extends ServicesPresenter<IWalletView> {

    public WalletPresenter(IWalletView viewInstance, RetrofitServices services) {
        super(viewInstance, services);
    }

}
