package org.evcs.android.features.shared.places;

import androidx.annotation.NonNull;

import com.google.android.libraries.places.api.model.Place;

public interface IPlacesView {

    /**
     * Called when the Place has been successfully retrieved from GeoDataApi from an ID
     *
     * @param response The Place for the ID
     */
    void onPlaceRetrieved(@NonNull Place response);

}
