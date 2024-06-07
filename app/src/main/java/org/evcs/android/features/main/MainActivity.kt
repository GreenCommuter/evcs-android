package org.evcs.android.features.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import androidx.navigation.Navigation.findNavController
import com.base.core.util.NavigationUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.evcs.android.R
import org.evcs.android.activity.AbstractSupportedVersionActivity
import org.evcs.android.databinding.ActivityBaseNavhostWithBottomNavBinding
import org.evcs.android.features.profile.plans.PlansActivity
import org.evcs.android.features.shared.IVersionView
import org.evcs.android.util.Extras
import org.evcs.android.util.PushNotificationUtils
import org.evcs.android.util.UserUtils

class MainActivity : AbstractSupportedVersionActivity(), IVersionView {
    var mNavigationController: MainNavigationController? = null
    private lateinit var menuView: BottomNavigationView
    private lateinit var mButton: TextView
    var isBottomOfStack = true
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isBottomOfStack = intent.getBooleanExtra(Extras.MainActivity.IS_BOTTOM, true)
    }

    @CallSuper
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        createNotificationChannel()
        mNavigationController = MainNavigationController(this, true,
            findNavController(this, R.id.activity_base_content))
        //mNavigationController!!.startFlow()

//        if (!intent.hasExtra(Extras.Root.VIEW_KEY)) {
//            return
//        }
    }

    override fun inflate(layoutInflater: LayoutInflater): View {
        val binding = ActivityBaseNavhostWithBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        menuView = binding.bottomNavigation
        mButton = binding.bottomNavigationButton
        return binding.root
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notifications_channel_name)
            val description = getString(R.string.notifications_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(PushNotificationUtils.CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun getNavGraphId(): Int {
        return R.navigation.navigation_graph
    }

    override fun isSupportedVersion(isSupported: Boolean, versionWording: String?) {
        if (!isSupported) {
            showNotSupportedVersion(versionWording)
        }
    }

    override fun populate() {
        super.populate()
        if (UserUtils.getLoggedUser() == null) {
            menuView.menu.clear()
            mButton.visibility = View.VISIBLE
        } else {
            menuView.inflateMenu(R.menu.drawer)
            mButton.visibility = View.GONE
        }
        menuView.selectedItemId = R.id.menu_drawer_map
        updateProfileAlert()
    }

    override fun setListeners() {
        super.setListeners()
        mButton.setOnClickListener { NavigationUtils.jumpTo(this, PlansActivity::class.java) }
        menuView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_drawer_map -> mNavigationController!!.onMapClicked()
                R.id.menu_drawer_charging -> mNavigationController!!.goToPreCharging()
                R.id.menu_drawer_profile -> mNavigationController!!.goToProfile()
            }
            //will be updated after canceling session
            false
        }
    }

    override fun onNetworkError() {
        //Handled by fragment
    }

    fun setSelectedItem(item: Int) {
        for (i in 0 until menuView.menu.size()) {
            menuView.menu.getItem(i).isChecked = false
        }
        menuView.menu.findItem(item)?.isChecked = true
    }

    fun updateProfileAlert() {
//        showProfileAlert(UserUtils.getLoggedUser()?.activeSubscription?.issue ?: false ||
//        !(UserUtils.getLoggedUser()?.isPhoneVerified ?: true))
    }

    fun showProfileAlert(show: Boolean) {
        if (!::menuView.isInitialized) return
        if (show) {
            menuView.getOrCreateBadge(R.id.menu_drawer_profile)
        } else {
            menuView.removeBadge(R.id.menu_drawer_profile)
        }
    }

    //Needed because if there's a scrollview the menu can be shown above the keyboard instead of below
    fun attachKeyboardListener() {
        val view = window.decorView.rootView
        KeyboardListener.attach(view) { isShown -> menuView.isVisible = !isShown }
    }

    fun detachKeyboardListener() {
        menuView.isVisible = true
        KeyboardListener.detach(window.decorView.rootView)
    }

}