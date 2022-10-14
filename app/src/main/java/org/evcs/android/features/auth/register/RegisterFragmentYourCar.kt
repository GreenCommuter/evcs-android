package org.evcs.android.features.auth.register

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isEmpty
import androidx.navigation.Navigation
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentRegisterYourCarBinding
import org.evcs.android.model.Car
import org.evcs.android.model.CarMaker
import org.evcs.android.ui.fragment.ErrorFragment


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
        mBinding.fragmentRegisterYourCarMake.setItems<Any?>(ArrayList())
        mBinding.fragmentRegisterYourCarModel.setItems<Any?>(ArrayList())
        presenter!!.getCars()
        setEnableButton(true)
        mBinding.fragmentRegisterStep.text =
            String.format(getString(R.string.fragment_register_step), 2)
    }

    override fun setListeners() {
        mBinding.fragmentRegisterYourCarButton.setOnClickListener { onButtonClick() }
        mBinding.fragmentRegisterYourCarMake.setListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onManufacturerClicked(mBinding.fragmentRegisterYourCarMake.getSelectedItem().toString())
            }

        })
        mBinding.fragmentRegisterYourCarModel.setListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setEnableButton(validfields())
            }

        })
    }

    private fun validfields(): Boolean {
        return mBinding.fragmentRegisterYourCarMake.isItemSelected()
                && mBinding.fragmentRegisterYourCarModel.isItemSelected()
                && !TextUtils.isEmpty(mBinding.fragmentRegisterYourCarZipcode.text)
    }

    override fun showCarMakers(carMakers: List<CarMaker>) {
        val items = carMakers.map { maker -> maker.make }
        mBinding.fragmentRegisterYourCarMake.setItems(items)
    }

    fun onManufacturerClicked(manufacturer: String) {
        val items = presenter!!.getCars(manufacturer)//.map { car -> car.model }
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
            mBinding.fragmentRegisterYourCarYear.text.toString()
        )
        Navigation.findNavController(requireView())
            .navigate(RegisterFragmentYourCarDirections.actionRegisterFragmentYourCarToRegisterFragmentCellPhone())
    }

    fun onTokenSent() {
        progressDialog.dismiss()
    }

}