package org.evcs.android.features.charging

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.base.core.fragment.BaseDialogFragment
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.activity.ChargingActivity
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.features.map.keepStatusBar
import org.evcs.android.model.Session
import org.evcs.android.model.shared.RequestError
import org.evcs.android.util.Extras
import org.evcs.android.util.SpinnerUtils

//Dialog to show while we get the current session
//If the request fails: go back, don't select charging
//If it is null: go to charging fragment, select charging
//It it is not null: go to "in progress", select charging, make sure when they come back we are back in the map
class PreChargingFragment : BaseDialogFragment<ChargingTabPresenter<*>?>(), ChargingTabView {

    private lateinit var mLauncher: ActivityResultLauncher<Intent>
    private lateinit var mProgressDialog: Dialog

    override fun layout(): Int {
        return R.layout.fragment_base
    }

    override fun createPresenter(): ChargingTabPresenter<*>? {
        return ChargingTabPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {
        mProgressDialog = SpinnerUtils.getNewProgressDialog(context, R.layout.spinner_layout)
        mProgressDialog.show()
        presenter?.onViewCreated()
        presenter?.getCurrentCharge()
        mLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            MainNavigationController.getInstance().onMapClicked()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        keepStatusBar(view)
    }

    override fun onChargeRetrieved(response: Session?) {
        //using hide() causes a bug on some phones
        mProgressDialog.cancel()
        MainNavigationController.getInstance().selectCharging()
        if (response != null) {
            val intent = Intent(requireContext(), ChargingActivity::class.java)
            intent.putExtra(Extras.StartCharging.SESSION, response)
            mLauncher.launch(intent)
        } else {
            findNavController().navigate(R.id.chargingFragment)
            dismiss()
        }
    }

    override fun showError(requestError: RequestError) {
        ToastUtils.show(requestError.body)
        mProgressDialog.hide()
        dismiss()
    }
}