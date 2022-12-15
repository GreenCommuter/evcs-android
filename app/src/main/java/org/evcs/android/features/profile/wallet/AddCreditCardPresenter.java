package org.evcs.android.features.profile.wallet;

import com.base.networking.retrofit.RetrofitServices;

import org.evcs.android.network.service.presenter.ServicesPresenter;

public class AddCreditCardPresenter<T extends IBrainTreeView> extends ServicesPresenter<T> {

    private static final String TAG = "AddCreditCardPresenter";
    private static final String TOKEN_KEY = "token";

    protected static final String LOCATION_KEY = "location";

    private String mTokenUrl;

    public AddCreditCardPresenter(T brainTreeFragment, RetrofitServices services) {
        super(brainTreeFragment, services);
    }

    /**
     * Requests from API the url where the token will be found.
     */
    public void getTokenEndpoint() {
//        getService(BrainTreeService.class).getTokenEndpoint().enqueue(new AuthCallback<Void>(this) {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                super.onResponse(call, response);
//                Headers headers = response.headers();
//                mTokenUrl = headers.get(LOCATION_KEY);
//                if (isViewCreated()) {
//                    if (mTokenUrl == null) {
//                        getView().showError(ErrorUtils.getError(response.errorBody()));
//                    } else {
//                        getToken();
//                    }
//                }
//            }
//
//            @Override
//            public void onResponseSuccessful(Void aVoid) {
//                // This method must be empty.
//            }
//
//            @Override
//            public void onResponseFailed(ResponseBody responseBody, int i) {
//                getView().showError(ErrorUtils.getError(responseBody));
//            }
//
//            @Override
//            public void onCallFailure(Throwable throwable) {
//                Log.e(TAG, throwable.getMessage(), throwable);
//                runIfViewCreated(new Runnable() {
//                    @Override
//                    public void run() {
//                        getView().showError(RequestError.getNetworkError());
//                    }
//                });
//            }
//        });
    }

    /**
     * Queries the url to check for the BrainTree token. If it is not available, we will receive a
     * string instead of a JSON, which is why I have to use Object and do a weird casting. If it was
     * not available, we have to retry.
     */
    protected void getToken() {
//        new PollingManager(this).poll(getService(BrainTreeService.class).getToken(mTokenUrl), new PollingManager.PollingCallback() {
//            @Override
//            public void onResponseSuccessful(Response response) {
//                getView().onTokenResponse((String) ((LinkedTreeMap) response.body()).get(TOKEN_KEY));
//            }
//
//            @Override
//            public void onResponseFailed(ResponseBody responseBody, int i) {
//                getView().onTokenError();
//            }
//
//            @Override
//            public void onCallFailure() {
//                runIfViewCreated(new Runnable() {
//                    @Override
//                    public void run() {
//                        getView().onTokenError();
//                    }
//                });
//            }
//
//            @Override
//            public boolean shouldRetry(Response response) {
//                return response.code() == NetworkCodes.ACCEPTED;
//            }
//        });
    }

    public void getTokenAndMakeDefaultPaymentMethod(final String item) {
//        getService(BrainTreeService.class).getPaymentMethods()
//                .enqueue(new AuthCallback<List<CreditCard>>(this) {
//                    @Override
//                    public void onResponseSuccessful(List<CreditCard> creditCardInformations) {
//                        Collections.reverse(creditCardInformations);
//                        for (CreditCard cci : creditCardInformations) {
//                            if (cci.last4.equals(item.substring(item.length() - 4))) {
//                                makeDefaultPaymentMethod(cci.getToken());
//                                return;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onResponseFailed(ResponseBody responseBody, int i) {
//                        getView().showError(ErrorUtils.getError(responseBody));
//                    }
//
//                    @Override
//                    public void onCallFailure(Throwable throwable) {
//                        getView().showError(RequestError.getNetworkError());
//                    }
//                });
    }

    public void makeDefaultPaymentMethod(final String item) {
//        getService(BrainTreeService.class).makeDefaultPaymentMethod(item)
//                .enqueue(new AuthCallback<List<CreditCard>>(this) {
//                    @Override
//                    public void onResponseSuccessful(List<CreditCard> creditCardInformations) {
//                        getView().onMakeDefaultFinished();
//                    }
//
//                    @Override
//                    public void onResponseFailed(ResponseBody responseBody, int i) {
//                        getView().showError(ErrorUtils.getError(responseBody));
//                    }
//
//                    @Override
//                    public void onCallFailure(Throwable throwable) {
//                        getView().showError(RequestError.getNetworkError());
//                    }
//                });
    }
}
