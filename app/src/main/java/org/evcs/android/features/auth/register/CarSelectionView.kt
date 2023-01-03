package org.evcs.android.features.auth.register

import org.evcs.android.model.CarMaker
import org.evcs.android.model.user.UserCar
import org.evcs.android.ui.view.shared.IErrorView

interface CarSelectionView : IErrorView {
    fun showCarMakers(carMakers: List<CarMaker>)
    fun onCarsAdded(car: UserCar)
}