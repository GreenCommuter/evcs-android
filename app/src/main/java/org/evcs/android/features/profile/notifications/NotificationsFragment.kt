package org.evcs.android.features.profile.notifications

import android.view.View
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentNotificationsBinding
import org.evcs.android.ui.fragment.ErrorFragment

class NotificationsFragment : ErrorFragment<NotificationsPresenter>(), NotificationsView {

    private lateinit var mBinding: FragmentNotificationsBinding

    override fun layout(): Int {
        return R.layout.fragment_notifications
    }

    override fun createPresenter(): NotificationsPresenter {
        return NotificationsPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentNotificationsBinding.bind(v)
    }

    override fun setListeners() {
        mBinding.notificationsToolbar.setNavigationOnClickListener { requireActivity().finish() }
        mBinding.notificationsSwitch.setOnClickListener {
            presenter.toggleNotifications(mBinding.notificationsSwitch.isChecked)
        }
    }

    override fun onSuccess() {
        ToastUtils.show("Notifications " + if (mBinding.notificationsSwitch.isChecked) "en" else "dis" + "abled")
    }
}
