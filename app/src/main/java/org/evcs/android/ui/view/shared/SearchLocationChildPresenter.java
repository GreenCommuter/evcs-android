package org.evcs.android.ui.view.shared;

import android.content.Context;

import com.base.core.presenter.BasePresenter;
import com.google.android.libraries.places.api.Places;

import org.evcs.android.R;
import org.evcs.android.features.ISearchLocationView;
import org.evcs.android.features.shared.places.PlacesRequestPresenter;

public class SearchLocationChildPresenter extends BasePresenter<ISearchLocationView> {

    private final PlacesRequestPresenter<ISearchLocationView> mPlacesRequestPresenter;

    public SearchLocationChildPresenter(ISearchLocationView view,
                                        String currentLocationString) {
        super(view);
        mPlacesRequestPresenter = new PlacesRequestPresenter<>(view, currentLocationString);
    }

    public void setGoogleApiClient(Context context) {
        Places.initialize(context, context.getString(R.string.google_api_key));
        mPlacesRequestPresenter.setClient(Places.createClient(context));
    }

    /**
     * See {@link PlacesPresenter#getPlaceFromId(String)}}
     */
    public void getPlaceFromId(String placeId) {
        mPlacesRequestPresenter.getPlaceFromId(placeId);
    }

    /**
     * Returns the {@link PlacesPresenter} used in this class
     *
     * @return Places Presenter
     */
    public PlacesRequestPresenter getPlacesPresenter() {
        return mPlacesRequestPresenter;
    }

    @Override
    public void detachView() {
        mPlacesRequestPresenter.detachView();
        super.detachView();
    }
}
