package org.evcs.android.features.auth.register

import okhttp3.ResponseBody
import org.evcs.android.model.Car
import org.evcs.android.model.CarMaker
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.UserCar
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.CarsService
import org.evcs.android.network.service.EVCSRetrofitServices
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils

open class CarSelectionPresenter<V : CarSelectionView>(viewInstance: V, services: EVCSRetrofitServices) :
    ServicesPresenter<V>(viewInstance, services) {

    private lateinit var mCarMakers: List<CarMaker>

    fun register(carId: Int, year: String?) {
        getService(UserService::class.java).saveUserCar(UserCar(carId, year))
            .enqueue(object : AuthCallback<UserCar>(this) {
            override fun onResponseSuccessful(response: UserCar) {
                response.carId = carId
                val user = UserUtils.getLoggedUser()
                user.userCar = response
                UserUtils.saveUser(user)
                view?.onCarsAdded(response!!)
            }

            override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
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
        val make = mCarMakers.firstOrNull { maker -> maker.make == manufacturer }
        return make?.cars ?: ArrayList()
    }

}