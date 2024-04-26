package org.evcs.android.activity.account

import android.view.View
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentDeleteAccountErrorBinding
import org.evcs.android.features.profile.notifications.NotificationsPresenter
import org.evcs.android.features.profile.notifications.NotificationsView
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.ui.fragment.ErrorFragment

class DeleteAccountErrorFragment : ErrorFragment<NotificationsPresenter?>(), NotificationsView {

    private lateinit var mBinding: FragmentDeleteAccountErrorBinding

    companion object {
        fun newInstance(): DeleteAccountErrorFragment {
            return DeleteAccountErrorFragment()
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_delete_account_error
    }

    override fun createPresenter(): NotificationsPresenter {
        return NotificationsPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {}

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentDeleteAccountErrorBinding.bind(v)
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.fragmentDeleteAccountErrorPayBalance.setOnClickListener {
            //Go to web view
        }
        mBinding.fragmentDeleteAccountErrorOptOut.setOnClickListener {
            showProgressDialog()
            presenter?.toggleNotifications(false)
        }
        mBinding.fragmentDeleteAccountErrorCancel.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onSuccess() {
        hideProgressDialog()
        EVCSDialogFragment.Builder()
            .setTitle(getString(R.string.delete_account_opt_out_title))
            .setSubtitle(getString(R.string.delete_account_opt_out_subtitle))
            .addButton(getString(R.string.app_close), { dialog ->
                 dialog.dismiss()
            }, R.style.ButtonK_Blue)
            .show(childFragmentManager)
    }

    //I don't know why I had to do this
    override fun onBackPressed(): Boolean {
        parentFragmentManager.popBackStackImmediate()
        return true
    }
}