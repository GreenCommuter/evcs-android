package org.evcs.android

import java.util.*

object BaseConfiguration {
    const val SHARED_PREFERENCES = "private-shared-prefs"
    const val STATE_NAME_SEPARATOR = ","
    const val DEVICE_TYPE = "android"
    const val ROLLBAR_CLIENT_ID = "46c2f505dec044f9855223be8549a559"
    const val REQUEST_TRIES = 10
    const val REQUEST_TRIES_DELAY = 1000
    @JvmField
    val DEFAULT_LOCALE = Locale.US
    const val BASE_URL_PLAY_STORE = "market://details?id="

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

    object License {
        const val IMAGE_MAX_WIDTH = 750
        const val IMAGE_MAX_HEIGHT = 750
        const val IMAGE_QUALITY = 50
    }

    object IncidentPictures {
        const val IMAGE_QUALITY = 35
    }

    object FontPaths {
        const val PROXIMA_NOVA_BOLD = "fonts/ProximaNova-Bold.otf"
        const val SANSATION_REGULAR = "fonts/Sansation-Regular.ttf"
        const val ROBOTO_BOLD = "fonts/Roboto-Bold.ttf"
    }

    object Map {
        const val DEFAULT_LATITUDE = 34.0223187
        const val DEFAULT_LONGITUDE = -118.3104457
        const val DEFAULT_ZOOM = 10f
    }

    object ChargingHistory {
        const val ITEMS_PER_PAGE = 15
        const val ITEMS_VISIBLE_THRESHOLD = 15
    }

    object UserConditions {
        const val MIN_AGE_LIMIT = 18
    }

    object EVCSInformation {
        const val PHONE_NUMBER = "+1 844-474-3342"
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