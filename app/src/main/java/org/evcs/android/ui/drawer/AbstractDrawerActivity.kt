package org.evcs.android.ui.drawer

import android.graphics.Color
import org.evcs.android.activity.AbstractSupportedVersionActivity
import org.evcs.android.R
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.TextView
import androidx.navigation.NavController
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.NavigationUI
import com.facebook.drawee.view.SimpleDraweeView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.base.core.util.KeyboardUtils
import org.evcs.android.BuildConfig
import org.evcs.android.databinding.ActivityToolbarBaseBinding

/**
 * Activity with a drawer and toolbar
 */
abstract class AbstractDrawerActivity : AbstractSupportedVersionActivity() {
    private lateinit var mToolbar: Toolbar

    private lateinit var mNavigationView: NavigationView
    private var mDrawerLayout: DrawerLayout? = null
    private lateinit var mVersionTextView: TextView
    private lateinit  var mActivityBaseContent: View

    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private var mNavigationItemListener: NavigationView.OnNavigationItemSelectedListener? = null
    private var mAvatarOnClickListener: View.OnClickListener? = null

    override fun inflate(layoutInflater: LayoutInflater): View {
        val binding = ActivityToolbarBaseBinding.inflate(layoutInflater)
        mDrawerLayout = binding.activityDrawer
        navController = Navigation.findNavController(this, R.id.activity_base_content)
        navigationView = binding.activityNavigationView
        mVersionTextView = binding.activityVersionTextView
        mActivityBaseContent = findViewById(R.id.activity_base_content)
        return binding.root
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation.
        return NavigationUI.navigateUp(navController!!, mDrawerLayout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.itemId != android.R.id.home && super.onOptionsItemSelected(item)
    }

    override fun init() {
        super.init()
        configureFragmentManager()
        //        setSupportActionBar(mToolbar);
        val versionName = BuildConfig.VERSION_NAME
        mVersionTextView.text = String.format(
            getString(R.string.drawer_activity_version_name_prefix_format),
            versionName
        )
    }

    override fun populate() {
        populateHeader(firstHeaderView)
        mDrawerLayout!!.setScrimColor(Color.TRANSPARENT)
        mDrawerLayout!!.drawerElevation = 0f
    }

    override fun setListeners() {
        setHeaderListener(firstHeaderView)
        mNavigationView.setNavigationItemSelectedListener { item ->
            mDrawerLayout!!.closeDrawers()
            if (mNavigationItemListener != null) {
                mNavigationItemListener!!.onNavigationItemSelected(item)
            } else false
        }
        mDrawerToggle = object : ActionBarDrawerToggle(
            this, mDrawerLayout, mToolbar,
            R.string.drawer_menu_open, R.string.drawer_menu_close
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                KeyboardUtils.hideKeyboard(this@AbstractDrawerActivity, getNavigationView()!!)
                super.onDrawerSlide(drawerView, slideOffset)
                mActivityBaseContent.x = slideOffset * mNavigationView.width
            }
        }

        // Listener when the arrow is shown
        mDrawerToggle.setToolbarNavigationClickListener(View.OnClickListener { onBackPressed() })
        mDrawerLayout!!.addDrawerListener(mDrawerToggle)
    }

    /**
     * Returns the first header in the navigation view. For some reason the navigation can have
     * multiple headers, but we are only using one.
     *
     * @return First header view
     */
    private val firstHeaderView: View
        private get() = mNavigationView.getHeaderView(0)

    /**
     * Populates the header with the user's name and avatar.
     *
     * @param headerView Header view to populate
     */
    protected abstract fun populateHeader(headerView: View?)

    /**
     * Sets a listener to which notifies when the user clicks the avatar in the navigation drawer
     *
     * @param avatarOnClickListener Avatar on click listener
     */
    protected fun setAvatarOnClickListener(avatarOnClickListener: View.OnClickListener?) {
        mAvatarOnClickListener = avatarOnClickListener
    }

    /**
     * Sets a listener to decide what to do when the user clicks on a item in the drawer menu.
     *
     * @param listener Listener to add
     */
    protected fun setNavigationItemListener(listener: NavigationView.OnNavigationItemSelectedListener?) {
        mNavigationItemListener = listener
    }

    /**
     * Sets a click listener for the header's avatar in the navigation view.
     *
     * @param headerView Header view to set listeners
     */
    private fun setHeaderListener(headerView: View) {
        val avatar = headerView.findViewById<SimpleDraweeView>(R.id.navigation_header_avatar)
        avatar.setOnClickListener { view ->
            mDrawerLayout!!.closeDrawers()
            if (mAvatarOnClickListener != null) {
                mAvatarOnClickListener!!.onClick(view)
            }
        }
    }

    /**
     * Configure the fragmentManager to add a listener to update the toolbar and drawer when a new
     * fragment is added. This listener will be added recursively to all the child [ ].
     */
    private fun configureFragmentManager() {
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewCreated(
                    fm: FragmentManager, f: Fragment, v: View,
                    savedInstanceState: Bundle?
                ) {
                    if (f is IToolbarView) {
                        // Clear toolbar or load menu from fragment
                        f.setHasOptionsMenu(true)
                    }
                }

                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                    if (currentFocus != null) {
                        KeyboardUtils.hideKeyboard(this@AbstractDrawerActivity, currentFocus!!)
                    }
                }
            }, true
        )
    }

    override fun onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout!!.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Returns the [NavigationView] for this drawer.
     *
     * @return Navigation View
     */
    fun getNavigationView(): NavigationView {
        return mNavigationView
    }
}