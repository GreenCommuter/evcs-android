package org.evcs.android

import com.stripe.android.googlepaylauncher.GooglePayEnvironment

object Configuration {
    const val API_ENDPOINT = "https://api.evcs.com"
    const val ROLLBAR_ENVIRONMENT = "production"
    const val STRIPE_KEY = "pk_live_hwqCra4WxPKCHuwIvt6IDyZi00IvUSycl6"
    const val CLIENT_SECRET_ENV = "subscriptions"
    val GOOGLE_PAY_ENV = GooglePayEnvironment.Production
}
