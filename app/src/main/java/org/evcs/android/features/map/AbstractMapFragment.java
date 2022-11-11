package org.evcs.android.features.map;

import androidx.annotation.NonNull;

import com.base.core.presenter.BasePresenter;
import com.base.maps.IMapPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import org.evcs.android.R;

public abstract class AbstractMapFragment<T extends BasePresenter & IMapPresenter>
        extends com.base.maps.AbstractMapFragment<T> {

    @Override
    public int getSelectedPolylineColorId() {
        return 0;
    }

    @Override
    public int getPolylineColorId() {
        return 0;
    }

    @Override
    public int getSelectedPinRes() {
        return R.drawable.ic_map_pin_selected;
    }

    @Override
    public int getUnselectedPinRes() {
        return R.drawable.ic_map_pin_unselected;
    }

    protected void centerMap(@NonNull LatLng latLng) {
        applyCameraUpdate(CameraUpdateFactory.newLatLng(latLng), true);
    }

}
