package org.evcs.android.model.user

class UserRequestSignup(email: String, password: String, private val name: String) :
    UserRequest(email, password)