package org.evcs.android.model.user

class UserRequest {
    private var name: String? = null
    private val email: String
    private val password: String

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }

    constructor(name: String?, email: String, password: String) {
        this.name = name
        this.email = email
        this.password = password
    }
}