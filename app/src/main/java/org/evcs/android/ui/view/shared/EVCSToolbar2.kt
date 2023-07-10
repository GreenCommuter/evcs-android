package org.evcs.android.ui.view.shared

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.evcs.android.R
import org.evcs.android.databinding.ToolbarEvcsBinding
import org.evcs.android.util.ViewUtils.getStatusBarHeight
import org.evcs.android.util.ViewUtils.showOrHide

class EVCSToolbar2 : LinearLayout {

    private lateinit var mBinding: ToolbarEvcsBinding

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs, defStyleAttr) {
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mBinding = ToolbarEvcsBinding.inflate(LayoutInflater.from(context), this, true)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Toolbar)
        val title = typedArray.getString(R.styleable.Toolbar_title)
        val navigationText = typedArray.getString(R.styleable.Toolbar_navigationIcon)
        typedArray.recycle()
        populate(title, navigationText)
        setListeners()
    }

    private fun populate(title: String?, navigationText: String?) {
        mBinding.toolbarEvcsTitle.text = title
        mBinding.toolbarEvcsNavbutton.showOrHide(navigationText)
        val lp = LayoutParams(mBinding.root.layoutParams)
        lp.setMargins(0, context.getStatusBarHeight(), 0, 0)
        mBinding.root.layoutParams = lp
    }

    private fun setListeners() {
        mBinding.toolbarEvcsNavbutton.setOnClickListener {
            if (context is Activity) (context as Activity).onBackPressed()
        }
    }

    fun setNavigationText(text: String) {
        mBinding.toolbarEvcsNavbutton.showOrHide(text)
    }

    fun setNavigationOnClickListener(function: () -> Unit) {
        mBinding.toolbarEvcsNavbutton.setOnClickListener { function.invoke() }
    }

    fun setTitle(title: String) {
        mBinding.toolbarEvcsTitle.text = title
    }

}