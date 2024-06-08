package org.evcs.android.model

import java.io.Serializable

class InvoiceLine : Serializable {
    //label and detail may be all inside "description"
    val label: String? = null
        get() = field ?: description!!.split("-")[0]
    val detail: String? = null
        get() = field ?: description!!.split("-")[1].trim()
    private val description: String? = null
    val amount: String? = null

}