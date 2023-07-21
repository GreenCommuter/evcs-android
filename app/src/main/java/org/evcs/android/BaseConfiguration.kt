package org.evcs.android

import java.util.*

object BaseConfiguration {
    const val SHARED_PREFERENCES = "private-shared-prefs"
    const val STATE_NAME_SEPARATOR = ","
    const val DEVICE_TYPE = "android"
    const val ROLLBAR_CLIENT_ID = "8f045077be25481c874d39b1da7e475b"
    const val REQUEST_TRIES = 12
    const val REQUEST_TRIES_DELAY = 5000
    @JvmField
    val DEFAULT_LOCALE = Locale.US
    const val BASE_URL_PLAY_STORE = "market://details?id="
    const val AUTOCOMPLETE_ADAPTER_THRESHOLD = 2;

    object Validations {
        const val PASSWORD_MIN_LENGTH = 6
        const val ZIP_CODE_REGEX = "^\\d{5}(?:[-\\s]\\d{4})?$"
        const val PHONE_REGEX = "^(\\+?1)? ?(\\([0-9]{3}\\)|[0-9]{3})[- ]?([0-9]{3})[-]?([0-9]{4})$"
        const val ALPHANUMERIC_REGEX = "^[a-zA-Z0-9-]+$"
    }

    object Avatar {
        const val IMAGE_PNG_MIME_TYPE = "image/png"
        const val IMAGE_MAX_WIDTH = 750
        const val IMAGE_MAX_HEIGHT = 750
        const val IMAGE_QUALITY = 80
    }

    object WebViews {
        const val TERMS_URL = "https://www.evcs.com/app/terms-of-use"
        const val FAQ_URL = "https://support.evcs.com/hc/en-us"
        const val REQUEST_URL = "https://support.evcs.com/hc/en-us/requests/new"
        const val REPORT_URL = "https://support.evcs.com/hc/en-us/requests/new"
        const val PLANS_URL = "https://subscriptions.evcs.com/account/%s/plans"
        const val PAY_URL = "https://subscriptions.evcs.com/account/%s/pay"
        const val LEARN_MORE_URL = "https://subscriptions.evcs.com/learn_more?plan_id=%s"
        const val LEARN_MORE_USER_ID_QUERY = "&user_id=%s"
        const val LYFT_URL = "https://evcs.com/lyft"
    }

    object IncidentPictures {
        const val IMAGE_QUALITY = 35
    }

    object Map {
        const val DEFAULT_LATITUDE = 34.0223187
        const val DEFAULT_LONGITUDE = -118.3104457
        const val DEFAULT_ZOOM = 9f
    }

    object ChargingHistory {
        const val ITEMS_PER_PAGE = 20
        const val ITEMS_VISIBLE_THRESHOLD = 10
    }

    object UserConditions {
        const val MIN_AGE_LIMIT = 18
    }

    object EVCSInformation {
        const val PHONE_NUMBER = "+1 866-300-3827"
    }

    object Location {
        const val LOCATION_INTERVAL_IN_MILIS = 3000L
        const val LOCATION_FASTEST_INTERVAL_IN_MILIS = 1000L
        const val LOCATION_MAX_WAIT_TIME_IN_MILIS = 10000L
        const val DISTANCE_CHANGE_THRESHOLD_IN_METERS: Long = 5
    }

    object Help {
        const val PHONE_REGEX =
            "(\\+?1)? ?(\\([0-9]{3}\\)|[0-9]{3})[-– ]?([0-9]{3})[-– ]?([0-9]{4})"
    }

    object Push {
        const val CUSTOM_PUSH_TITLE = "EVCS"
        const val ARGUMENT_REGEX = "\\[\"?(.*?)\"?\\]"
    }

    object DatePicker {
        const val HALFHOUR = 30
    }
}