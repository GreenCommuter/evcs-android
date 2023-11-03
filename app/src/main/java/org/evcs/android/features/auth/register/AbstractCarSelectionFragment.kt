package org.evcs.android.features.auth.register

import android.view.View
import android.widget.AdapterView
import androidx.annotation.CallSuper
import org.evcs.android.R
import org.evcs.android.features.shared.DropdownWithLabel
import org.evcs.android.model.CarMaker
import org.evcs.android.ui.fragment.ErrorFragment
import org.joda.time.DateTime


abstract class AbstractCarSelectionFragment<P : CarSelectionPresenter<*>> : ErrorFragment<P>(), CarSelectionView {

    override fun layout(): Int {
        return R.layout.fragment_register_your_car
    }

    @CallSuper
    override fun init() {
        getMakeField().setItems<String>(ArrayList())
        getModelField().setItems<String>(ArrayList())
        getYearField().setItems(IntRange(2000, DateTime().plusMonths(8).year).reversed().map { i -> i.toString() })
        presenter!!.getCars()
        setEnableButton(true)
    }

    abstract fun getMakeField(): DropdownWithLabel
    abstract fun getModelField(): DropdownWithLabel
    abstract fun getYearField(): DropdownWithLabel
    abstract fun getButton(): View

    override fun setListeners() {
        getButton().setOnClickListener { onButtonClick() }
        getMakeField().setListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onManufacturerClicked(getMakeField().getSelectedItemLabel())
            }

        })
        getModelField().setListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setEnableButton(validFields())
            }

        })
    }

    protected open fun validFields(): Boolean {
        return getMakeField().isItemSelected() && getModelField().isItemSelected()
    }

    override fun showCarMakers(carMakers: List<CarMaker>) {
        val items = carMakers.map { maker -> maker.make }
        getMakeField().setItems(items)
    }

    protected open fun onManufacturerClicked(manufacturer: String) {
        val items = presenter!!.getCars(manufacturer)
        getModelField().setItems(items.map { car -> car.model })
    }

    fun setEnableButton(validFields: Boolean) {
        getButton().isEnabled = validFields
    }

    protected open fun onButtonClick() {
        progressDialog.show()
        presenter!!.register(
                getMakeField().getSelectedItem() as String,
                getModelField().getSelectedItem() as String,
                getYearField().getSelectedItem()?.toString()
        )
    }

}