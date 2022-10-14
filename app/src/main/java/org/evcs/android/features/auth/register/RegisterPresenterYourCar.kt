package org.evcs.android.features.auth.register

import okhttp3.ResponseBody
import org.evcs.android.model.Car
import org.evcs.android.model.CarMaker
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.UserCar
import org.evcs.android.model.user.ZipCodeWrapper
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.CarsService
import org.evcs.android.network.service.EVCSRetrofitServices
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils

class RegisterPresenterYourCar(viewInstance: RegisterViewYourCar, services: EVCSRetrofitServices) :
    ServicesPresenter<RegisterViewYourCar?>(viewInstance, services) {

    private lateinit var mCarMakers: List<CarMaker>

    fun register(make: String, model: String, year: String?) {
    }

    fun register(carId: Int, year: String?) {
        getService(UserService::class.java).saveUserCar(UserCar(carId, year))
            .enqueue(object : AuthCallback<Void>(this) {
            override fun onResponseSuccessful(response: Void?) {
                TODO("Not yet implemented")
            }

            override fun onResponseFailed(responseBody: ResponseBody?, code: Int) {
                view?.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(t: Throwable?) {
                runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
            }

        })
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

    fun updateZipcode(zipCode: String) {
        getService(UserService::class.java).updateUser(UserUtils.getUserId(), ZipCodeWrapper(zipCode))
    }

}