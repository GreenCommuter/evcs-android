package org.evcs.android.features.profile.wallet;

import org.evcs.android.model.PaymentMethod;

import java.util.List;

public interface IWalletHeaderView extends ICreditCardView {

    void onPaymentMethodsReceived(List<PaymentMethod> creditCardInformationList);

    void onPaymentMethodsNotReceived();
}
