package org.evcs.android.features.shared;

import com.base.networking.retrofit.RetrofitServices;

import org.evcs.android.network.service.presenter.ServicesPresenter;

/**
 * this presenter gets the current version of application.
 */
public class VersionPresenter extends ServicesPresenter<IVersionView> {

    public VersionPresenter(IVersionView viewInstance, RetrofitServices services) {
        super(viewInstance, services);
    }

    /**
     * Verify if the application version on cellphone is supported.
     * @param version application version.
     */
    public void checkSupportedVersion(String version) {
        /*getService(VersionService.class)
                .isSupportedVersion(Configuration.DEVICE_TYPE, version)
                .enqueue(new AuthCallback<SupportedVersion>(this) {
                    @Override
                    public void onResponseSuccessful(SupportedVersion supportedVersion) {
                        getView().isSupportedVersion(supportedVersion.isSupported(), supportedVersion.getWording());
                    }

                    @Override
                    public void onResponseFailed(ResponseBody responseBody, int i) {
                        getView().isSupportedVersion(true, null);
                    }

                    @Override
                    public void onCallFailure(Throwable throwable) {
                        runIfViewCreated(new Runnable() {
                            @Override
                            public void run() {
                                getView().onNetworkError();
                            }
                        });
                    }
                });*/
    }
}
