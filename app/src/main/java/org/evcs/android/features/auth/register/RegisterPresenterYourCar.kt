package org.evcs.android.features.auth.register

import okhttp3.ResponseBody
import org.evcs.android.model.Car
import org.evcs.android.model.CarMaker
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.CarsService
import org.evcs.android.network.service.EVCSRetrofitServices
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class RegisterPresenterYourCar(viewInstance: RegisterViewYourCar, services: EVCSRetrofitServices) :
    ServicesPresenter<RegisterViewYourCar?>(viewInstance, services) {

    private lateinit var mCarMakers: List<CarMaker>

    fun register(zip: String, make: String, model: String, year: String) {
    }

    fun getCars() {
        getService(CarsService::class.java).getCarModels()
            .enqueue(object : AuthCallback<List<CarMaker>>(this) {
                override fun onResponseSuccessful(carMakers: List<CarMaker>) {
                    mCarMakers = carMakers;
                    view?.showCarMakers(carMakers);
                }

                override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                    view?.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(throwable: Throwable) {
                    runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
                }
            })
    }

    fun getCars(manufacturer: String) : List<Car> {
        if (!this::mCarMakers.isInitialized) return ArrayList()
        val make = mCarMakers.filter { maker -> maker.make == manufacturer }.firstOrNull()
        return make?.cars ?: ArrayList()
    }

}