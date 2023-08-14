package org.evcs.android.model.user

import org.evcs.android.model.Car

data class UserCar(var carId: Int, val year: String? = null) {

    var id: Int = 0
    var make: String? = null
    var model: String? = null

    override fun toString(): String {
        return (if (year != null) "$year " else "") + make + " " + model + " "
    }

    fun toCar(): Car {
        return Car(carId, make, model)
    }
}