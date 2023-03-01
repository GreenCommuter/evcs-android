package org.evcs.android.model.push

/**
 * Class that represents a unique token that identifies a device. We send this to API so they know
 * where to send the notifications for each user. The device type is "android" or "ios", while
 * the token is a string.
 */
data class DeviceToken(private val deviceToken: String?, private val deviceType: String)