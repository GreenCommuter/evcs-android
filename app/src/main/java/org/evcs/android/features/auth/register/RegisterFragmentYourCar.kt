package org.evcs.android.features.auth.register

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.core.widget.doOnTextChanged
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentRegisterYourCarBinding
import org.evcs.android.model.Car
import org.evcs.android.model.CarMaker
import org.evcs.android.navigation.controller.AbstractNavigationController
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.UserUtils
import org.joda.time.DateTime


class RegisterFragmentYourCar : ErrorFragment<RegisterPresenterYourCar>(), RegisterViewYourCar {

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

    override fun init() {
        if (hasCompletedCarScreen()) {
            skip()
            return
        }
        mBinding.fragmentRegisterYourCarMake.setItems<String>(ArrayList())
        mBinding.fragmentRegisterYourCarModel.setItems<String>(ArrayList())
        mBinding.fragmentRegisterYourCarYear.setItems(IntRange(2000, DateTime().year).map { i -> i.toString() })
        presenter!!.getCars()
        setEnableButton(true)
        mBinding.fragmentRegisterStep.text = String.format(getString(R.string.fragment_register_step), 2)
    }

    private fun hasCompletedCarScreen(): Boolean {
        return UserUtils.getLoggedUser().zipCode != null
    }

    override fun setListeners() {
        mBinding.fragmentRegisterYourCarZipcode.editText?.doOnTextChanged { _, _, _, _ ->
            setEnableButton(validFields())
        }
        mBinding.fragmentRegisterYourCarButton.setOnClickListener { onButtonClick() }
        mBinding.fragmentRegisterYourCarMake.setListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onManufacturerClicked(mBinding.fragmentRegisterYourCarMake.getSelectedItemLabel())
            }

        })
        mBinding.fragmentRegisterYourCarModel.setListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setEnableButton(validFields())
            }

        })
    }

    private fun validFields(): Boolean {
        return mBinding.fragmentRegisterYourCarMake.isItemSelected()
                && mBinding.fragmentRegisterYourCarModel.isItemSelected()
                && !TextUtils.isEmpty(mBinding.fragmentRegisterYourCarZipcode.text)
    }

    override fun showCarMakers(carMakers: List<CarMaker>) {
        val items = carMakers.map { maker -> maker.make }
        mBinding.fragmentRegisterYourCarMake.setItems(items)
    }

    fun onManufacturerClicked(manufacturer: String) {
        val items = presenter!!.getCars(manufacturer)
        mBinding.fragmentRegisterYourCarModel.setItems(items)
    }

    fun setEnableButton(validFields: Boolean) {
        mBinding.fragmentRegisterYourCarButton.isEnabled = validFields
    }

    private fun onButtonClick() {
        progressDialog.show()
        presenter!!.updateZipcode(
            mBinding.fragmentRegisterYourCarZipcode.text.toString()
        )
        presenter!!.register(
            (mBinding.fragmentRegisterYourCarModel.getSelectedItem() as Car).id,
            mBinding.fragmentRegisterYourCarYear.getSelectedItem()?.toString()
        )
    }

    override fun onCarsAdded() {
        progressDialog.dismiss()
        findNavController()
            .navigate(RegisterFragmentYourCarDirections.actionRegisterFragmentYourCarToRegisterFragmentCellPhone())
    }

    fun skip() {
        val navOptions: NavOptions = AbstractNavigationController.replaceLastNavOptions(findNavController())
        findNavController().navigate(RegisterFragmentYourCarDirections.actionRegisterFragmentYourCarToRegisterFragmentCellPhone(), navOptions)
    }

    override fun onZipCodeUpdated() {}

}