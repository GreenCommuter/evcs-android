//package org.evcs.android.util.wolmo;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.IdRes;
//import androidx.annotation.Nullable;
//
//import com.base.core.presenter.BasePresenter;
//import com.google.android.gms.maps.MapView;
//
//import ar.com.wolox.wolmo.core.fragment.WolmoFragment;
//import ar.com.wolox.wolmo.core.presenter.BasePresenter;
//
///**
// * A handy class that enables the user to use a single map.
// *
// * The inflated view must contain an instance of {@link MapView}
// *
// * The fragment overrides some lifecycle methods in order to enable the usage of the map, its
// * according to https://developers.google.com/android/reference/com/google/android/gms/maps/MapView
// *
// * The map is accessible through {@link BaseDrawableMapFragment#getMapView()}.
// */
//public abstract class BaseDrawableMapFragment<T extends BasePresenter> extends BaseFragment<T> {
//
//    private MapView mMapView;
//
//    /* Lifecycle methods overriden for delegation to MapView */
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = super.onCreateView(inflater, container, savedInstanceState);
//        mMapView = view.findViewById(getMapViewId());
//
//        if (mMapView != null) {
//            mMapView.onCreate(savedInstanceState);
//        }
//        return view;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        if (mMapView != null) {
//            mMapView.onStart();
//        }
//    }
//
//    @Override
//    public void onStop() {
//        if (mMapView != null) {
//            mMapView.onStop();
//        }
//
//        super.onStop();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (mMapView != null) {
//            mMapView.onResume();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        if (mMapView != null) {
//            mMapView.onPause();
//        }
//
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroyView() {
//        if (mMapView != null) {
//            mMapView.onDestroy();
//            mMapView.destroyDrawingCache();
//            mMapView = null;
//        }
//        super.onDestroyView();
//    }
//
//    @Override
//    public void onLowMemory() {
//        if (mMapView != null) {
//            mMapView.onLowMemory();
//        }
//
//        super.onLowMemory();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        if (mMapView != null) {
//            mMapView.onSaveInstanceState(outState);
//        }
//    }
//
//    /**
//     * Access the map view, for example, to draw on it.
//     * @return the map view
//     */
//    @Nullable
//    protected final MapView getMapView() {
//        return mMapView;
//    }
//
//    /**
//     * Extending fragment must override this method with the id used for the map view on the xml,
//     * so we don't force an id name.
//     * @return The id of the map view
//     */
//    protected abstract @IdRes
//    int getMapViewId();
//
//}