package org.evcs.android.features.shared.places;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.places.api.model.AutocompletePrediction;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.R;
import org.evcs.android.features.map.search.SearchLocationChildFragment;
import org.evcs.android.util.FontUtils;

import java.util.ArrayList;
import java.util.List;

public class PlaceCurrentAutocompleteAdapter extends ArrayAdapter<CustomLocation> implements Filterable {

    private static final int MAX_RESULTS = 5;

    private final PlacesRequestPresenter mPresenter;
    private final ArrayList<AutocompletePrediction> mResultList;
    private final ArrayList<CustomLocation> mResultNames;
    private PlaceCurrentAutocompleteAdapter.AutocompleteListener mListener;
    private String mCurrentLocationString;
    private String mSearchByNameString;

    public PlaceCurrentAutocompleteAdapter(Context context, PlacesRequestPresenter presenter,
                                           String currentLocationString, String searchByNameString) {
        super(context, R.layout.adapter_autocomplete_custom, R.id.adapter_autocomplete_custom_text);
        mPresenter = presenter;
        mResultList = new ArrayList<>();
        mResultNames = new ArrayList<>();
        mCurrentLocationString = currentLocationString;
        mSearchByNameString = searchByNameString;
    }

    public PlaceCurrentAutocompleteAdapter setListener(PlaceCurrentAutocompleteAdapter.AutocompleteListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public CustomLocation getItem(int position) {
        try {
            return mResultNames.get(position);
        } catch (IndexOutOfBoundsException e) {
            Log.e("Autocomplete adapter", "Out of bounds");
            return new CustomLocation("loading...");
        }
    }

    public String getPlaceId(int position) {
        return mResultList.get(position).getPlaceId();
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            view = inflater.inflate(R.layout.adapter_autocomplete_custom, parent, false);
        }
        CustomLocation customLocation = getItem(position);
        TextView name = view.findViewById(R.id.adapter_autocomplete_custom_text);
        ImageView imageView = view.findViewById(R.id.adapter_autocomplete_custom_icon);
        imageView.setImageResource(customLocation.getDrawable());
        name.setText(customLocation.getLocation());

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence query) {
                FilterResults results = new FilterResults();
                List<AutocompletePrediction> resultValues = new ArrayList<>();

                if (query.length() >= BaseConfiguration.AUTOCOMPLETE_ADAPTER_THRESHOLD) {
                    //This is a blocking call, but performFiltering() is called in a worker thread
                    resultValues = mPresenter.getResults(query);
                }
                else {
                    results.count = showHistory();
                    //Without this it crashes when erasing for modifying in a worker thread
                    notifyDataSetChanged();
                    return results;
                }

                mResultList.clear();
                mResultNames.clear();
                if (resultValues == null) {
                    // Current Location is the default value
                    results.count = 2;
                } else {
                    results.count = resultValues.size() + 2;
                    mResultList.add(0, getSearchByName(query));
                    mResultList.add(1, getCurrentLocationPrediction());
                    mResultList.addAll(resultValues);
                }

                SpannableString a = FontUtils.getSpannable(new String[]{mSearchByNameString, mResultList.get(0).getFullText(null).toString()}, Color.BLACK);
                mResultNames.add(new CustomLocation(a));

                mResultNames.add(new CustomLocation(
                        mResultList.get(1).getFullText(null),
                        R.drawable.ic_map_car_location));

                for (int i = 2; i < Math.min(mResultList.size(), MAX_RESULTS + 2); i++) {
                    mResultNames.add(new CustomLocation(mResultList.get(i).getFullText(null).toString()));
                }

//                if (mListener != null) {
//                    if (mResultList.isEmpty()) {
//                        mListener.onResultsRetrieved(null);
//                    } else {
//                        mListener.onResultsRetrieved(mResultList.get(0).getPlaceId());
//                    }
//                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence query, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

        };
    }

    public int showHistory() {
        List<SearchLocationChildFragment.LocationHistoryItem> history =
                SearchLocationChildFragment.getLocationHistory();
        mResultList.clear();
        mResultNames.clear();
        for (int i = 0; i < Math.min(history.size(), MAX_RESULTS); i++) {
           String name = history.get(i).location.getName();
           mResultNames.add(new CustomLocation(name, R.drawable.ic_clock_light_blue));
           mResultList.add(AutocompletePrediction.builder(history.get(i).key).setFullText(name).build());
        }
        return mResultList.size();
    }

    private AutocompletePrediction getCurrentLocationPrediction() {
        return AutocompletePrediction.builder(mCurrentLocationString)
                .setFullText(mCurrentLocationString)
                .build();
    }

    private AutocompletePrediction getSearchByName(CharSequence query) {
        return AutocompletePrediction.builder(mSearchByNameString + " " + query)
                .setFullText(query.toString())
                .build();
    }

    public interface AutocompleteListener {
        void onResultsRetrieved(String placeId);
    }
}
