package org.evcs.android.features.shared

import android.content.Context
import android.widget.ArrayAdapter

class ArrayAdapterWithPrompt<T>(context: Context, resource: Int, private val objects: List<T>) :
    ArrayAdapter<String?>(context, resource) {

    private var mPrompt = "Select One"

    init {
        add(mPrompt)
        addAll(objects.map { o -> o.toString() })
    }

    fun setPrompt(s: String) {
        mPrompt = s
    }

    fun getActualItem(position: Int): T? {
        return if (isActualItem(position)) objects[position - 1] else null
    }

    fun isActualItem(position: Int): Boolean {
        return position > 0
    }

    fun getItemLabel(position: Int): String {
        return super.getItem(position)!!
    }
}