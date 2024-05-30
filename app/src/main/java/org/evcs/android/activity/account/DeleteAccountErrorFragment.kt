package org.evcs.android.activity.account

import android.view.View
import com.base.core.util.NavigationUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentDeleteAccountErrorBinding
import org.evcs.android.features.profile.notifications.NotificationsActivity
import org.evcs.android.features.profile.notifications.NotificationsPresenter
import org.evcs.android.features.profile.notifications.NotificationsView
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.PaymentUtils
import org.evcs.android.util.UserUtils

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

    override fun populate() {
        setOptOutButton(UserUtils.getLoggedUser().marketingNotifications == true)
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.fragmentDeleteAccountErrorPayBalance.setOnClickListener {
            PaymentUtils.goToPendingPayment(requireContext())
        }
        mBinding.fragmentDeleteAccountErrorOptOut.setOnClickListener {
            showProgressDialog()
            presenter?.optOut()
        }
        mBinding.fragmentDeleteAccountErrorCancel.setOnClickListener {
            onBackPressed()
        }
    }

    //Check if this has to be enabled always
    fun setOptOutButton(enabled: Boolean) {
        mBinding.fragmentDeleteAccountErrorOptOut.isEnabled = enabled
        val text =
            if (enabled) R.string.delete_account_error_opt_out else R.string.delete_account_opt_out_title
        mBinding.fragmentDeleteAccountErrorOptOut.text = getString(text)
    }

    override fun onSuccess() {
        val user = UserUtils.getLoggedUser()
        user.marketingNotifications = false
        UserUtils.saveUser(user)

        hideProgressDialog()
        setOptOutButton(false)
        EVCSDialogFragment.Builder()
            .setTitle(getString(R.string.delete_account_opt_out_title))
            .setSubtitle(getString(R.string.delete_account_opt_out_subtitle))
            .addButton(getString(R.string.app_close), { dialog ->
                dialog.dismiss()
                requireActivity().finish()
                NavigationUtils.jumpTo(requireContext(), NotificationsActivity::class.java)
            }, R.style.ButtonK_Blue)
            .setCancelable(false)
            .show(childFragmentManager)
    }

    //I don't know why I had to do this
    override fun onBackPressed(): Boolean {
        parentFragmentManager.popBackStackImmediate()
        return true
    }
}