package org.evcs.android.features.serviceSelection

import android.content.Intent
import android.view.View
import com.base.maps.AbstractMapFragment
import org.evcs.android.ui.drawer.IToolbarView
import org.evcs.android.R
import org.evcs.android.EVCSApplication
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.model.shared.RequestError
import com.base.core.util.ToastUtils
import org.evcs.android.databinding.FragmentMainMapBinding
import org.evcs.android.features.auth.initialScreen.AuthActivity
import org.evcs.android.features.main.MainActivity
import org.evcs.android.ui.drawer.IToolbarView.ToolbarState
import org.evcs.android.navigation.INavigationListener
import org.evcs.android.model.user.User
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils

class MainMapFragment : AbstractMapFragment<MainMapPresenter>(), IChooseServiceView, IToolbarView {

    private lateinit var mBinding: FragmentMainMapBinding
    private lateinit var mChooseServiceListener: IMainMapListener
    private var mAlreadyLoaded = false

    override fun layout(): Int {
        return R.layout.fragment_main_map
    }

    override fun createPresenter(): MainMapPresenter {
        return MainMapPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        mBinding = FragmentMainMapBinding.bind(v)
        super.setUi(v)
    }

    override fun init() {
        super.init()
        mChooseServiceListener = MainNavigationController.getInstance()
        if (!mAlreadyLoaded) {
            presenter.getLoggedUser()
        }
    }

    override fun setListeners() {
        mBinding.signOut.setText(if (UserUtils.getLoggedUser() == null) "Sign in"  else "Sign out")
        mBinding.signOut.setOnClickListener {
            if (UserUtils.getLoggedUser() == null)
                startActivity(Intent(requireContext(), AuthActivity::class.java))
            else
                UserUtils.logout(null)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isOpeningApp) {
        }
    }

    override fun getMapViewId(): Int {
        return R.id.map_view
    }

    private fun showTermsDialog() {}

    override fun showError(requestError: RequestError) {
        ToastUtils.show(requestError.body)
    }

    val isOpeningApp: Boolean
        get() = requireActivity().intent.getBooleanExtra(Extras.Root.OPENING_KEY, false)

    override fun onLoadError(user: User) {
        mAlreadyLoaded = true
        //        hideProgressDialog();
    }

    override fun getToolbarState(): ToolbarState {
        return ToolbarState.SHOW_HAMBURGER
    }

    override fun getToolbarTitle(): String? {
        return getString(R.string.choose_service_title)
    }

    /**
     * Listener for the ChooseService Fragment
     */
    interface IMainMapListener : INavigationListener {
        fun onCarSharingSelected(skipPlaceholder: Boolean)
    }

    override fun getSelectedPolylineColorId(): Int {
        return 0
    }

    override fun getPolylineColorId(): Int {
        return 0
    }

    override fun getSelectedPinRes(): Int {
        return 0
    }

    override fun getUnselectedPinRes(): Int {
        return 0
    }

    companion object {
        fun newInstance(): MainMapFragment {
            return MainMapFragment()
        }
    }
}