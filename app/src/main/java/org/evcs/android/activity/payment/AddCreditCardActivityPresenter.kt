package org.evcs.android.activity.payment

import android.text.Editable
import org.evcs.android.network.service.EVCSRetrofitServices
import org.evcs.android.network.service.presenter.ServicesPresenter

class AddCreditCardActivityPresenter(viewInstance: AddCreditCardActivityView, services: EVCSRetrofitServices?) :
    ServicesPresenter<AddCreditCardActivityView>(viewInstance, services) {
    fun addCreditCard(number: Editable, expiration: Editable, cvv: Editable) {
    }

}
