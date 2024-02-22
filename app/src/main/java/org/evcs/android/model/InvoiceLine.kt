package org.evcs.android.model

import java.io.Serializable

data class InvoiceLine(
    val label: String,
    val detail: String
) : Serializable