package org.evcs.android.activity

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.databinding.ActivityBaseBinding

class SearchActivity : BaseActivity2() {

    override fun inflate(layoutInflater: LayoutInflater?): View {
        return ActivityBaseBinding.inflate(layoutInflater!!).root
    }

    override fun init() {
    }

}
