package org.evcs.android.features.auth.register

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentRegisterYourCarBinding
import org.evcs.android.features.shared.DropdownWithLabel
import org.evcs.android.model.user.UserCar
import org.evcs.android.navigation.controller.AbstractNavigationController
import org.evcs.android.util.UserUtils


class RegisterFragmentYourCar : AbstractCarSelectionFragment<RegisterPresenterYourCar>(), RegisterViewYourCar {

    private lateinit var mBinding: FragmentRegisterYourCarBinding

    /**
     * Returns a new RegisterFragment instance.
     *
     * @return new instance.
     */
    fun newInstance(): RegisterFragmentYourCar {
        val args = Bundle()
        val fragment = RegisterFragmentYourCar()
        fragment.arguments = args
        return fragment
    }

    override fun layout(): Int {
        return R.layout.fragment_register_your_car
    }

    override fun createPresenter(): RegisterPresenterYourCar {
        return RegisterPresenterYourCar(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentRegisterYourCarBinding.bind(v)
    }

    override fun getMakeField(): DropdownWithLabel {
        return mBinding.fragmentRegisterYourCarMake
    }

    override fun getModelField(): DropdownWithLabel {
        return mBinding.fragmentRegisterYourCarModel
    }

    override fun getYearField(): DropdownWithLabel {
        return mBinding.fragmentRegisterYourCarYear
    }

    override fun getButton(): View {
        return mBinding.fragmentRegisterYourCarButton
    }

    override fun init() {
        if (hasCompletedCarScreen()) {
            skip()
            return
        }
        super.init()
    }

    private fun hasCompletedCarScreen(): Boolean {
        return UserUtils.getLoggedUser().zipCode != null
    }

    override fun setListeners() {
        mBinding.fragmentRegisterYourCarZipcode.editText?.doOnTextChanged { _, _, _, _ ->
            setEnableButton(validFields())
        }
        super.setListeners()
    }

    override fun validFields(): Boolean {
        return super.validFields()
                && !TextUtils.isEmpty(mBinding.fragmentRegisterYourCarZipcode.text)
    }

    override fun onButtonClick() {
        presenter!!.updateZipcode(
            mBinding.fragmentRegisterYourCarZipcode.text.toString()
        )
        super.onButtonClick()
    }

    override fun onCarsAdded(car : UserCar) {
        progressDialog.dismiss()
        findNavController()
            .navigate(RegisterFragmentYourCarDirections.actionRegisterFragmentYourCarToRegisterFragmentCellPhone())
    }

    fun skip() {
        val navOptions = AbstractNavigationController.replaceLastNavOptions(findNavController())
        findNavController().navigate(RegisterFragmentYourCarDirections.actionRegisterFragmentYourCarToRegisterFragmentCellPhone(), navOptions)
    }

    override fun onZipCodeUpdated() {}

}