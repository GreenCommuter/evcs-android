package org.evcs.android.model

class Car(var id: Int, var make: String?, var model: String?) {

    var imageUrl: String? = null

    override fun toString(): String {
        return model!!
    }

    override fun equals(other: Any?): Boolean {
        return other is Car && other.id == id
    }
}