package org.evcs.android.features.profile.wallet;

import com.base.networking.retrofit.RetrofitServices;

import org.evcs.android.model.CreditCard;
import org.evcs.android.network.callback.AuthCallback;
import org.evcs.android.network.service.presenter.ServicesPresenter;
import org.evcs.android.util.ErrorUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;

public class PaymentMethodPresenter<T extends IPaymentMethodView> extends ServicesPresenter<T> {

    private static final String TAG = "PaymentMethodPresenter";

    protected static final String LOCATION_KEY = "location";
    private AuthCallback<List<CreditCard>> mPaymentMethodUrlCallback;

    public PaymentMethodPresenter(T brainTreeFragment, RetrofitServices services) {
        super(brainTreeFragment, services);

        mPaymentMethodUrlCallback = new AuthCallback<List<CreditCard>>(this) {
            @Override
            public void onResponseSuccessful(List<CreditCard> creditCardInformations) {
                getView().onPaymentMethodsReceived(creditCardInformations);
            }

            @Override
            public void onResponseFailed(ResponseBody responseBody, int i) {
                getView().showError(ErrorUtils.getError(responseBody));
            }

            @Override
            public void onCallFailure(Throwable t) {
                runIfViewCreated(new Runnable() {
                    @Override
                    public void run() {
                        getView().onPaymentMethodsNotReceived();
                    }
                });
            }

        };
    }

    public void getCreditCards() {
        mPaymentMethodUrlCallback.onResponseSuccessful(
                new ArrayList<>(Arrays.asList(new CreditCard()))
        );
//        getService(BrainTreeService.class).getPaymentMethods().enqueue(mPaymentMethodUrlCallback);

    }

    public void makeDefaultPaymentMethod(CreditCard item) {
//        getService(BrainTreeService.class).makeDefaultPaymentMethod(item.getToken())
//                .enqueue(mPaymentMethodUrlCallback);
    }

    public void removePaymentMethod(CreditCard item) {
//        getService(BrainTreeService.class).removePaymentMethod(item.getToken())
//                .enqueue(mPaymentMethodUrlCallback);
    }

}
