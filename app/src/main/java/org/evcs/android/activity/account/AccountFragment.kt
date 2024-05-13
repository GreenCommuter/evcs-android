package org.evcs.android.activity.account

import android.content.Intent
import android.telephony.PhoneNumberUtils
import android.view.Gravity
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.base.core.util.NavigationUtils.jumpTo
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.ActivityAccountBinding
import org.evcs.android.features.auth.register.VerifyPhoneActivity
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils

class AccountFragment : ErrorFragment<DeleteAccountPresenter>(), DeleteAccountView {

    private lateinit var mChangeUserResult: ActivityResultLauncher<Intent>
    private lateinit var mBinding: ActivityAccountBinding

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = ActivityAccountBinding.bind(v)
    }

    override fun init() {
        mChangeUserResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            populate()
        }
    }

    override fun layout(): Int {
        return R.layout.activity_account
    }

    override fun createPresenter(): DeleteAccountPresenter {
        return DeleteAccountPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun populate() {
        super.populate()
        val user = UserUtils.getLoggedUser()
        mBinding.fragmentAccountEmail.setText(user.email)
        mBinding.fragmentAccountPhone.setText(PhoneNumberUtils.formatNumber(user.phone?:"", "US"))
        mBinding.fragmentAccountName.setText(UserUtils.getLoggedUser().name)
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.fragmentAccountName.setOnClickListener {
            mChangeUserResult.launch(Intent(requireContext(), ChangeNameActivity::class.java))
        }
        mBinding.fragmentAccountChangePassword.setOnClickListener { jumpTo(requireContext(), ChangePasswordActivity::class.java) }
        mBinding.fragmentAccountEmail.setOnClickListener {
            mChangeUserResult.launch(Intent(requireContext(), ChangeEmailActivity::class.java))
        }
        mBinding.fragmentAccountPhone.setOnClickListener {
            val intent = Intent(requireContext(), VerifyPhoneActivity::class.java)
            intent.putExtra(Extras.VerifyActivity.USE_CASE, VerifyPhoneActivity.UseCase.USER_REQUEST)
            mChangeUserResult.launch(intent)
        }
        mBinding.fragmentAccountDelete.setOnClickListener {
            showProgressDialog()
            presenter.checkPayments()
        }
    }

    override fun showConfirmDialog() {
        hideProgressDialog()
        EVCSDialogFragment.Builder()
            //.setTitle("Are you sure?")
            .setSubtitle(getString(R.string.delete_account_warning_subtitle), Gravity.CENTER)
            .addButton(getString(R.string.delete_account_warning_ok), { dialog ->
                dialog.dismiss()
                showProgressDialog()
                mBinding.fragmentAccountDelete.isEnabled = false
                presenter.deleteAccount()
            }, R.style.ButtonK_Blue)
            .addButton(getString(R.string.app_cancel), { dialog -> dialog.dismiss() }, R.style.ButtonK_BlueOutline)
            .show(childFragmentManager)
    }

    override fun showPaymentIssue() {
        hideProgressDialog()
        mBinding.fragmentAccountDelete.isEnabled = true
        (requireActivity() as AccountActivity).goToDeleteError()
    }

    override fun onAccountDeleted() {
        hideProgressDialog()
        ToastUtils.show(getString(R.string.delete_account_toast))
    }

}