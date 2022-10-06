package org.evcs.android.network.service.presenter;


import com.base.core.presenter.BasePresenter;
import com.base.networking.retrofit.RetrofitServices;

/**
 * Presenter class which allows to retrieve retrofit services.
 *
 * @param <T> view interface
 */
public abstract class ServicesPresenter<T> extends BasePresenter<T> {

    private final RetrofitServices mServices;

    public ServicesPresenter(T viewInstance, RetrofitServices services) {
        super(viewInstance);
        mServices = services;
    }

    /**
     * Returns an instance of a service.
     *
     * @param serviceClazz target {@link Class}
     * @param <Service> actual class of the service
     *
     * @return instance of {@link Service} service.
     */
    protected <Service> Service getService(Class<Service> serviceClazz) {
        return mServices.getService(serviceClazz);
    }

}
