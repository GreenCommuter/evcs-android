package org.evcs.android.features.shared.places;

import com.base.core.presenter.BasePresenter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.evcs.android.features.ISearchLocationView;

import java.util.Arrays;
import java.util.List;

public class PlacesRequestPresenter<V extends ISearchLocationView> extends BasePresenter<V> {

    private static final long SECONDS_TO_WAIT = 60;
    private static final String UNITED_STATES_COUNTRY_CODE = "US";

    protected PlacesClient mGoogleApiClient;
    private String mCurrentLocationString;

    private List<AutocompletePrediction> tempResult;

    public PlacesRequestPresenter(V viewInstance, String currentLocationString) {
        super(viewInstance);
        mCurrentLocationString = currentLocationString;
    }

    public void setClient(PlacesClient googleApiClient) {
        mGoogleApiClient = googleApiClient;
    }

    /**
     * Submits an autocomplete query to the Places Geo Data Autocomplete API.
     * Results are returned as frozen AutocompletePrediction objects
     * This method will block until data is returned from the API.
     *
     * @param query Autocomplete query string
     * @return Results from the autocomplete API or null if the API client is not available or
     * the query did not complete successfully.
     */
    public List<AutocompletePrediction> getResults(CharSequence query) {
//        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
//                .setCountry(UNITED_STATES_COUNTRY_CODE).build();

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
// Call either setLocationBias() OR setLocationRestriction().
//                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setCountry(UNITED_STATES_COUNTRY_CODE)
//                .setTypeFilter(TypeFilter.ADDRESS)
//                .setSessionToken(token)
                .setQuery(query.toString())
                .build();

        Task<FindAutocompletePredictionsResponse> results = mGoogleApiClient.findAutocompletePredictions(request);

        results.addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse response) {
                tempResult = response.getAutocompletePredictions();
            }
        });

        return tempResult;

    }

    /**
     * Issue a request to the Places Geo Data API to retrieve a Place object with additional
     * details about the place.
     * @param placeId the ID of the Place we need to get
     */
    public void getPlaceFromId(String placeId) {
        if (!placeId.equals(mCurrentLocationString)) {

            List<Place.Field> placeFields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG,
                    Place.Field.VIEWPORT);
            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
            Task<FetchPlaceResponse> placeResult = mGoogleApiClient.fetchPlace(request);
            placeResult.addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                @Override
                public void onSuccess(FetchPlaceResponse response) {
                    getView().onPlaceRetrieved(response.getPlace());
                }
            });
        } else {
            getView().onCurrentLocationRetrieved();
        }
    }
}