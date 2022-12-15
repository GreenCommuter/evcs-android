package org.evcs.android.features.profile.wallet;

import org.evcs.android.model.CreditCard;
import org.evcs.android.ui.view.shared.IErrorView;

import java.util.List;

public interface IPaymentMethodView extends IErrorView {

    void onPaymentMethodsReceived(List<CreditCard> creditCardInformationList);

    void onPaymentMethodsNotReceived();
}
