package org.evcs.android.activity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import org.evcs.android.R
import org.evcs.android.features.account.ChangePasswordFragment
import org.evcs.android.databinding.ActivityToolbarNonavBinding
import org.evcs.android.ui.view.shared.EVCSToolbar

class SimpleToolbarActivity : BaseActivity2() {

    lateinit var mToolbar : EVCSToolbar

    override fun init() {
        replaceFragment(R.id.activity_base_content,
                intent.getSerializableExtra("Fragment") as Class<out Fragment>)
        val title = intent.getStringExtra("ToolbarTitle")
        if (title != null && title.length > 0) {
            mToolbar.visibility = View.VISIBLE
            mToolbar.title = title
        }
    }

    override fun setListeners() {
        super.setListeners()
        mToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun inflate(layoutInflater: LayoutInflater): View {
        val binding = ActivityToolbarNonavBinding.inflate(layoutInflater)
        mToolbar = binding.activityToolbar
        return binding.root
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context, fragment: Class<out Fragment>, toolbarTitle: String): Intent {
            val intent = Intent(context, SimpleToolbarActivity::class.java)
            intent.putExtra("Fragment", fragment)
            intent.putExtra("ToolbarTitle", toolbarTitle)
            return intent
        }
    }
//
//    abstract fun getInitialFragment() : Class<out Fragment>
//
//    abstract fun getToolbarTitle() : String
}