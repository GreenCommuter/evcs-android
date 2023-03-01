package org.evcs.android.model

import java.io.Serializable

class PricingDetail : Serializable {
    var priceKwh: Float? = null
    private val initialFee: Float? = null
    private val occupancyFlatFee: Float? = null
    private val bufferTime: Int? = null

    //    int firstHour;
    //    int firstHourPrice;
    var thereafterPrice: Int? = null

    //    int regularMinimumCost;
    var showFreeChargingCode = false
    var freeChargingCode: String? = null

    //TODO: chequear que pasa si es null
    fun printPriceKwh(): String {
        return String.format("%.2f USD/kWh", priceKwh)
    }

    fun printConnectionFee(): String? {
        if (initialFee == null) return null
        return if (initialFee == 0f) "Free" else initialFee.toString()
    }

    fun printOccupancyFee(): String? {
        return if (occupancyFlatFee == null) null else String.format("%.2f USD/charge", occupancyFlatFee)
    }

    fun printBufferTime(): String? {
        return if (bufferTime == null) null else String.format("%d mins", bufferTime)
    }
}