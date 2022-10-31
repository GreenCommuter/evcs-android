package org.evcs.android.features.map;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.base.maps.IMapPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.R;
import org.evcs.android.network.service.presenter.ServicesPresenter;
import org.evcs.android.util.LocationUtils;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Allows drawing of multiple mapItems.
 * It also allows to highlight one of them by setting it as 'selected'. This draws its
 * {@link Marker} in a different fashion than the regular containers.
 */
public abstract class SelectionMapFragment<T extends ServicesPresenter & IMapPresenter,
        MI> extends AbstractMapFragment<T> {

    private Container mSelectedContainer;

    int mMapPadding;

    protected LinkedHashMap<Integer, Container> mVContainerMap; // MapItem ID -> Container
    protected LinkedHashMap<String, Container> mMarkerContainerMap; // Marker ID -> Container

    @Override
    public void init() {
        super.init();

        mVContainerMap = new LinkedHashMap<>();
        mMarkerContainerMap = new LinkedHashMap<>();

        mMapPadding = (int) getResources().getDimension(R.dimen.map_padding);
    }

    @Override
    @CallSuper
    public void clearMap() {
        mVContainerMap.clear();
        mMarkerContainerMap.clear();
        super.clearMap();
    }

    @Override
    protected float getDefaultZoom() {
        return BaseConfiguration.Map.DEFAULT_ZOOM;
    }

    @Override
    protected LatLng getDefaultLatlng() {
        return new LatLng(BaseConfiguration.Map.DEFAULT_LATITUDE, BaseConfiguration.Map.DEFAULT_LONGITUDE);
    }

    /**
     * Renders the mapItems, centers the camera in the bounds they depict and determines the
     * default selected Container.
     * @param mapItems target mapItems
     */
    @CallSuper
    public void showMapItems(@NonNull final List<MI> mapItems) {
        if (mapItems.isEmpty()) {
            return;
        }

        final LatLngBounds mapItemsBounds = LocationUtils.addDiagonal(drawMapItems(mapItems));

        applyCameraUpdate(CameraUpdateFactory.newLatLngBounds(mapItemsBounds, mMapPadding));
        mSelectedContainer = mVContainerMap.get(mapItems.get(getDefaultSelectedMapItem()).hashCode());

        setMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Container c = mMarkerContainerMap.get(marker.getId());
                if (c != null) {
                    onContainerClicked(c);
                }
                return true;
            }
        });
    }

    /**
     * Center map if positions are not visible.
     * @param latLngsList list of positions
     * @param forceZoom if false, will only change the camera if the container was not completely
     *                  visible
     */
    protected void centerMap(@NonNull final List<LatLng> latLngsList, boolean forceZoom) {
        LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
        for (LatLng latLng : latLngsList) {
            boundsBuilder.include(latLng);
        }
        if (!contains(latLngsList) || forceZoom) {
            applyCameraUpdate(CameraUpdateFactory
                    .newLatLngBounds(LocationUtils.addDiagonal(boundsBuilder.build()), mMapPadding));
        }
    }

    protected void centerMap(@NonNull LatLng latLng) {
        applyCameraUpdate(CameraUpdateFactory.newLatLng(latLng), true);
    }

    protected void centerMap(@NonNull final List<LatLng> latLngsList) {
        centerMap(latLngsList, true);
    }

    /**
     * Transform the positions in container to list off positions and call centerMap method.
     * @param container the container of positions.
     */

    protected void centerFromContainer(@NonNull final Container container) {
        centerMap(container.getMarker().getPosition());
    }

    /**
     * Each extending class should handle what happens when a {@link Container} is clicked.
     * Whenever a marker is clicked, this is called with its container
     * @param container the container of the marker clicked.
     */
    protected abstract void onContainerClicked(@NonNull Container container);

    /**
     * Draws a {@link Container} on the map.
     * @param mapItem target mapItem to be drawn
     * @param selected determines whether the route should be highlighted
     */
    private Container drawContainer(@NonNull MI mapItem, boolean selected) {

        Container container = getContainer(mapItem, selected);

        mVContainerMap.put(mapItem.hashCode(), container);
        mMarkerContainerMap.put(container.getMarker().getId(), container);

        return container;
    }

    /**
     * Each extending class has its own way of drawing the items.
     * @param mapItem The item to be drawn.
     * @param selected Whether to draw this as selected or not.
     * @return A {@link Container} with all the element drawn on the map.
     */
    @NonNull
    protected abstract Container getContainer(MI mapItem, boolean selected);

    /**
     * Renders a {@link List<MI>} on the map.
     *
     * @param mapItems target mapItems
     *
     * @return {@link LatLngBounds} bounding the locations of the mapItems.
     */
    private LatLngBounds drawMapItems(@NonNull List<MI> mapItems) {
        LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();

        for (int i = 0; i < mapItems.size(); i++) {
            MI mapItem = mapItems.get(i);

            Container c = drawContainer(mapItem, i == getDefaultSelectedMapItem());

            boundsBuilder.include(c.getMarker().getPosition());
        }

        return boundsBuilder.build();
    }

    /**
     * Renders every property from a {@link Container}.
     *
     * @param container target route
     * @param selected determines wether it should render selected or not
     */
    private void setUpContainer(@NonNull Container container, boolean selected) {
        formatMarker(container.marker, selected);
    }

    /**
     * Toggles between current selection of {@link Container} and a new one.
     *
     * @param selectedContainer intended new selected route
     *
     * @return true if it was a different container, making the toggling effective,
     *         else false.
     */
    protected boolean toggleContainerSelection(@NonNull Container selectedContainer) {
        if (selectedContainer.equals(mSelectedContainer)) {
            return false;
        }

        if (mSelectedContainer != null) {
            setUpContainer(mSelectedContainer, false);
        }
        setUpContainer(selectedContainer, true);
        mSelectedContainer = selectedContainer;
        return true;
    }

    /**
     * @return currently selected {@link Container}
     */
    public Container getSelectedContainer() {
        return mSelectedContainer;
    }

    /**
     * @return integer designating the default selected mapItem position
     *          if it's not a valid index, it renders every mapItem as unselected
     */
    protected abstract int getDefaultSelectedMapItem();

    /**
     * This class serves as a container of all the elements a MapItem requires to be drawn on the
     * map.
     */
    protected class Container {
        private final MI mapItem;
        private final Marker marker;

        public Container(MI mapItem, Marker marker) {
            this.mapItem = mapItem;
            this.marker = marker;
        }

        public MI getMapItem() {
            return mapItem;
        }

        public Marker getMarker() {
            return marker;
        }

    }
}
