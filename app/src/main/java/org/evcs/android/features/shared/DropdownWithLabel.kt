package org.evcs.android.features.shared

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import org.evcs.android.R
import org.evcs.android.databinding.DropdownWithLabelBinding

class DropdownWithLabel : RelativeLayout {

    private lateinit var adapter: ArrayAdapterWithPrompt<*>

    private lateinit var mDropdown: Spinner
    private lateinit var mLayout: RelativeLayout
    private lateinit var mLabel: TextView
    private lateinit var mGreyBorder: Drawable
    private lateinit var mBlackBorder: Drawable
    private lateinit var mRedBorder: Drawable
    private var mLabelString: String? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
        parseAttributes(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs, defStyleAttr) {
        init(context)
        parseAttributes(attrs, defStyleAttr)
    }

    private fun parseAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.StandardTextField, defStyleAttr, 0)
        mLabelString = typedArray.getString(R.styleable.StandardTextField_label)
        mLabel.text = mLabelString
    }

    private fun init(context: Context) {
        val binding = DropdownWithLabelBinding.inflate(LayoutInflater.from(context), this, true)
        mLayout = binding.dropdownWithLabelLayout
        mDropdown = binding.dropdownWithLabelDropdown
        mDropdown.dropDownVerticalOffset = 7
        mLabel = binding.dropdownWithLabelLabel
        mGreyBorder = resources.getDrawable(R.drawable.layout_corners_rounded_grey_border)
        mBlackBorder = resources.getDrawable(R.drawable.layout_corners_rounded_black_border)
    }

    fun <T> setItems(items: List<T>) {
        adapter = ArrayAdapterWithPrompt(context, R.layout.dropdown_head, items)
        adapter.setDropDownViewResource(R.layout.dropdown_item)
        mDropdown.adapter = adapter
    }

    fun setListener(l: AdapterView.OnItemSelectedListener?) {
        mDropdown.onItemSelectedListener = l
    }

    fun getSelectedItem(): Any? {
        return adapter.getActualItem(mDropdown.selectedItemPosition)
    }

    fun getSelectedItemLabel(): String {
        return adapter.getItemLabel(mDropdown.selectedItemPosition)
    }

    fun isItemSelected() : Boolean {
        return adapter.isActualItem(mDropdown.selectedItemPosition)
                //mDropdown.selectedItem?.toString() != "" && mDropdown.selectedItem?.toString() != PROMPT
    }

    fun selectItem(item: Any?) {
        val position = adapter.indexOf(item)
        if (position > 0) mDropdown.setSelection(position)
    }

}