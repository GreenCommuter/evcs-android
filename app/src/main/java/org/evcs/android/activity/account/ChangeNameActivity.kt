package org.evcs.android.activity.account

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.widget.doOnTextChanged
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityChangeNameBinding
import org.evcs.android.model.shared.RequestError
import org.evcs.android.util.UserUtils


class ChangeNameActivity : BaseActivity2(), ChangeNameView {

    private lateinit var mBinding: ActivityChangeNameBinding
    private lateinit var mPresenter: ChangeNamePresenter

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityChangeNameBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mPresenter = ChangeNamePresenter(this, EVCSApplication.getInstance().retrofitServices)
        mPresenter.onViewCreated()
    }

    override fun populate() {
        super.populate()
        mBinding.fragmentChangeNameText.editText?.setText(UserUtils.getLoggedUser().name)
        mBinding.fragmentChangeNameText.editText?.doOnTextChanged {
                text, _, _, _ -> mBinding.activityChangeNameSave.isEnabled = text!!.isNotEmpty()
        }
    }

    override fun setListeners() {
        mBinding.activityChangeNameSave.setOnClickListener {
            mBinding.activityChangeNameSave.isEnabled = false
            mPresenter.changeName(mBinding.fragmentChangeNameText.text.trim())
        }
        mBinding.activityChangeNameToolbar.setNavigationOnClickListener { finish() }

        //Workaround to keep the adjust resize behaviour without ruining the toolbar with fitsSystemWindows
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.activityAccountLayout) { v, insets ->
            v.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            insets.consumeSystemWindowInsets()
        }
    }

    override fun showError(requestError: RequestError) {
        mBinding.activityChangeNameSave.isEnabled = true
        ToastUtils.show(requestError.body)
    }

    override fun onNameUpdate() {
        finish()
    }
}