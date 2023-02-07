package org.evcs.android.features.shared

import android.content.Context
import android.widget.RelativeLayout
import org.evcs.android.util.validator.TextInputLayoutInterface
import android.widget.EditText
import org.evcs.android.R
import android.widget.TextView
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.view.View.OnFocusChangeListener
import android.text.TextWatcher
import android.text.Editable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.text.TextUtilsCompat
import androidx.core.widget.doAfterTextChanged
import com.base.core.util.KeyboardUtils
import org.evcs.android.databinding.StandardTextFieldBinding

open class StandardTextField : RelativeLayout, TextInputLayoutInterface {
    private var mErrorEnabled = false
    private lateinit var mEditText: EditText
    protected lateinit var mLayout: RelativeLayout
    protected lateinit var mLabel: TextView
    protected lateinit var mLabelEmpty: TextView
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
        mLabel.visibility = if (TextUtils.isEmpty(mLabelString)) GONE else VISIBLE
        mLabelEmpty.text = mLabelString
    }

    protected open fun init(context: Context) {
        val binding = StandardTextFieldBinding.inflate(LayoutInflater.from(context), this, true)
        mLabelEmpty = binding.standardTextFieldLabelEmpty
        mLayout = binding.standardTextFieldLayout
        mLabel = binding.standardTextFieldLabel
        mGreyBorder = resources.getDrawable(R.drawable.layout_corners_rounded_grey_border)
        mBlackBorder = resources.getDrawable(R.drawable.layout_corners_rounded_black_border)
        mRedBorder = resources.getDrawable(R.drawable.layout_corners_rounded_red_border)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is EditText) {
            addEditText(child)
            val childParams = LayoutParams(params)
            childParams.addRule(BELOW, R.id.standard_text_field_label)
            mLayout.addView(child, index, childParams)
        } else {
            super.addView(child, index, params)
        }
    }

    private fun addEditText(child: EditText) {
        mEditText = child
        child.setBackgroundResource(android.R.color.transparent)
        child.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            updateBackground(hasFocus, mErrorEnabled)
            updateLabel(hasFocus, text.length == 0)
            if (hasFocus) KeyboardUtils.showKeyboard(context, child)
        }
        child.doAfterTextChanged {
            updateLabel(child.hasFocus(), text.length == 0)
        }
        updateLabel(child.hasFocus(), text.length == 0)
        //    android:paddingRight="@dimen/spacing_small"
    }

    private fun updateBackground(hasFocus: Boolean, errorEnabled: Boolean) {
        if (hasFocus) {
            mLayout.background = mBlackBorder
        } else {
            mLayout.background = if (errorEnabled) mRedBorder else mGreyBorder
        }
    }

    private fun updateLabel(hasFocus: Boolean, isEmpty: Boolean) {
//        if (hasFocus || !isEmpty || mEditText.hint != null) {
//            mLabel.visibility = VISIBLE
//            mLabelEmpty.visibility = GONE
//        } else {
//            mLabel.visibility = INVISIBLE
//            mLabelEmpty.visibility = VISIBLE
//        }
    }

    override fun setErrorEnabled(b: Boolean) {
        mErrorEnabled = b
    }

    override fun setError(s: CharSequence?) {
//        mEditText.setText(s);
    }

    override fun getEditText(): EditText? {
        return mEditText
    }

    override fun isErrorEnabled(): Boolean {
        return mErrorEnabled
    }

    override fun getText(): Editable {
        return mEditText.text
    }

    override fun requestDelayedFocus() {
        postDelayed({ editText!!.requestFocus() }, 400)
    }

    override fun setEmptyError(error: Boolean) {
        setError(null)
        isErrorEnabled = error
    }

    override fun setEnabled(enabled: Boolean) {
        mEditText.isEnabled = enabled
    }

    //Workaround for what seems to be a bug in the support library
    fun setAllCaps() {
        editText!!.doAfterTextChanged { s ->
                if (s.toString() != s.toString().toUpperCase())
                    s!!.replace(0, s.length, s.toString().toUpperCase())
        }
    }

    fun setLabel(label : String) {
        mLabel.text = label
    }
}