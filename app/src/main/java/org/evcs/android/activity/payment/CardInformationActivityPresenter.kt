package org.evcs.android.activity.payment

import org.evcs.android.model.CreditCard
import org.evcs.android.network.service.EVCSRetrofitServices
import org.evcs.android.network.service.presenter.ServicesPresenter

class CardInformationActivityPresenter(viewInstance: CardInformationActivityView, services: EVCSRetrofitServices?) :
    ServicesPresenter<CardInformationActivityView>(viewInstance, services) {

    fun removeCard(mCard: CreditCard) {

    }

}
