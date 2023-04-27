package org.evcs.android.model

import androidx.annotation.DrawableRes
import org.evcs.android.R
import java.io.Serializable

enum class ConnectorType(val printableName: String, @DrawableRes val mIcon: Int, @DrawableRes val mTextIcon: Int, val mAc: String) : Serializable {
    TESLA("Tesla", R.drawable.conn_tesla, R.drawable.conn_tesla_with_text, "Dc"),
    CHADEMO("CHAdeMO", R.drawable.conn_chademo, R.drawable.conn_chademo_with_text, "Dc"),
    TYPE1("j1772", R.drawable.conn_type1_j1772, R.drawable.conn_type1_j1772_with_text, "Ac"),
//    TYPE2("Type 2", R.drawable.conn_type2, "Ac"),
    CCS1("CCS", R.drawable.conn_ccs1, R.drawable.conn_ccs_with_text, "Ac"),
//    CCS2("CCS2", R.drawable.conn_ccs2, "Ac");
}