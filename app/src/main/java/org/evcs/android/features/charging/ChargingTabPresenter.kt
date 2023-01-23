package org.evcs.android.features.charging

import com.base.networking.retrofit.RetrofitServices
import org.evcs.android.network.service.presenter.ServicesPresenter

class ChargingTabPresenter(viewInstance: ChargingTabView?, services: RetrofitServices?) :
    ServicesPresenter<ChargingTabView>(viewInstance, services) {

}
