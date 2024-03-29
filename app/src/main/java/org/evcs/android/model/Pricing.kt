package org.evcs.android.model

import java.io.Serializable

class Pricing : Serializable {
    var desc: String? = null

    /*        "pricing": {
         "desc": "by energy",
                 "detail": {
             "price_kwh": "0.49",
                     "initial_fee": "0.0",
                     "occupancy_flat_fee": null,
                     "buffer_time": null
         }
     }
         "desc": "basic",
             "detail": {
         "first_hour": "4",
                 "first_hour_price": "30",
                 "thereafter_price": "4",
                 "regular_minimum_cost": "0"*/
    //        "pricing": {
    //        "desc": "by energy",
    //        "detail": {
    //        "price_kwh": "0.49",
    //        "initial_fee": "0.0",
    //        "occupancy_flat_fee": null,
    //        "buffer_time": null
    //        }
    lateinit var detail: PricingDetail
}