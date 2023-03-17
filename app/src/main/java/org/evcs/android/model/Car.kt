package org.evcs.android.model

class Car {

    var id = 0
    var make: String? = null
    var model: String? = null
    var imageUrl: String? = null

    override fun toString(): String {
        return model!!
    }
}