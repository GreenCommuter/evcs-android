package org.evcs.android.features.auth.register

import org.evcs.android.model.CarMaker
import org.evcs.android.ui.view.shared.IErrorView

interface RegisterViewYourCar : IErrorView {
    fun showCarMakers(carMakers: List<CarMaker>)
    fun onCarsAdded()
    fun onZipCodeUpdated()
}