package org.evcs.android.model.user

class PhoneWrapper(var phone: String)

class EmailWrapper(var email: String)

class CodeWrapper(var code: String)

class ZipCodeWrapper(var zipCode: String)

class NameWrapper(var firstName: String, var lastName: String)

class ChangePasswordWrapper(var email: String, var identifier: String, var password: String, var passwordConfirmation: String)

class BooleanWrapper(var enabled: Boolean)
