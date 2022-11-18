package org.evcs.android.activity

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.databinding.ActivityBaseBinding
import org.evcs.android.model.Station

class StationActivity : BaseActivity2() {
    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseBinding.inflate(layoutInflater).root
    }

    override fun init() {
        val s = intent.getSerializableExtra("Stations") as ArrayList<Station>
        val a = 1
    }
}