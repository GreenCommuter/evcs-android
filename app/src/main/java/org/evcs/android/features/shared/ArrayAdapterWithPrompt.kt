package org.evcs.android.features.shared

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.ColorRes
import org.evcs.android.R

class ArrayAdapterWithPrompt<T>(context: Context, resource: Int, private val objects: List<T>) :
    ArrayAdapter<String?>(context, resource) {

    private var mPrompt = "Select"
    @ColorRes private val mPromptColor : Int = R.color.evcs_gray_400

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

    fun indexOf(item: Any?): Int {
        return objects.indexOf(item as T?) + 1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        setTextColor(position, view.findViewById(R.id.dropdown_head_text))
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        setTextColor(position, view.findViewById(R.id.dropdown_item_text))
        return view
    }

    //for some reason this doesn't work with just setting view state
    private fun setTextColor(position: Int, textView: TextView) {
        if (!isActualItem(position)) textView.setTextColor(context.resources.getColor(mPromptColor))
    }
}