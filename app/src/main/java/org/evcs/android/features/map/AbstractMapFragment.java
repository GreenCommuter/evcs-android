package org.evcs.android.features.map;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;

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

    @SuppressLint({"MissingPermission", "ResourceType"})
    protected void drawLocationMarker() {
        if (getMapView() != null)
            getMapView().getMapAsync(googleMap -> {
                googleMap.setMyLocationEnabled(true);
                ((View) (getMapView().findViewById(1).getParent())).findViewById(2).setVisibility(View.GONE);
            });
        /*View locationButton = ((View) getMapView().findViewById(Integer.parseInt("1")).getParent())
                .findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 180, 180, 0);*/
    }

}
