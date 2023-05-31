package org.evcs.android.activity.account

import android.view.View
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.view.shared.EVCSToolbar2
import org.evcs.android.util.ViewUtils
import org.evcs.android.util.validator.ValidatorManager


abstract class UpdateUserActivity : BaseActivity2(), UpdateUserView {

    protected lateinit var mValidatorManager: ValidatorManager
    protected lateinit var mPresenter: UpdateUserPresenter

    override fun init() {
        mPresenter = UpdateUserPresenter(this, EVCSApplication.getInstance().retrofitServices)
        mValidatorManager = ValidatorManager()
        mPresenter.onViewCreated()
    }

    override fun populate() {
        super.populate()
        mValidatorManager.setOnAnyTextChangedListener {
            getButton().isEnabled = mValidatorManager.areAllFieldsValid()
        }
    }

    abstract fun getButton(): View

    override fun setListeners() {
        getButton().setOnClickListener {
            getButton().isEnabled = false
            onSaveClicked()
        }
        getToolbar().setNavigationOnClickListener { finish() }

        ViewUtils.setAdjustResize(getLayout())
    }

    abstract fun getToolbar(): EVCSToolbar2

    abstract fun getLayout(): View

    abstract fun onSaveClicked()

    override fun showError(requestError: RequestError) {
        getButton().isEnabled = true
        ToastUtils.show(requestError.body)
    }

    override fun onUserUpdate() {
        finish()
    }
}