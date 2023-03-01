package org.evcs.android.model.user

data class UserCar(private val carId: Int, private val year: String?) {

    var make: String? = null
    var model: String? = null

    override fun toString(): String {
        return (if (year != null) "$year " else "") + make + " " + model + " "
    }
}