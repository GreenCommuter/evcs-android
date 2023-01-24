package org.evcs.android.features.profile.wallet;

import org.evcs.android.model.PaymentMethod;
import org.evcs.android.ui.view.shared.IErrorView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IPaymentMethodView extends IErrorView {

    void onPaymentMethodsReceived(List<PaymentMethod> creditCardInformationList);

    void onPaymentMethodsNotReceived();

    void onPaymentMethodRemoved(@NotNull PaymentMethod item);

    void onDefaultPaymentMethodSet(@NotNull PaymentMethod item);
}
