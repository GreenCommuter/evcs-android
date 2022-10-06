package org.evcs.android.features;

import org.evcs.android.features.shared.places.IPlacesView;
import org.evcs.android.ui.view.shared.IErrorView;

public interface ISearchLocationView extends IPlacesView, IErrorView {


    /**
     * Called when the current location was chosen
     */
    void onCurrentLocationRetrieved();

}