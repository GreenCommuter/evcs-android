package org.evcs.android

import com.stripe.android.googlepaylauncher.GooglePayEnvironment

object Configuration {
    const val API_ENDPOINT = "https://api-dev.evcs.com/"
//    private-anon-acfb595595-evcs5.apiary-mock.com
    const val ROLLBAR_ENVIRONMENT = "develop"
    const val STRIPE_KEY = "pk_test_O2R06nl2klnSwHtEf8XrySfK00S9cDKqTl"
    const val CLIENT_SECRET_ENV = "dev"
    val GOOGLE_PAY_ENV = GooglePayEnvironment.Test
}