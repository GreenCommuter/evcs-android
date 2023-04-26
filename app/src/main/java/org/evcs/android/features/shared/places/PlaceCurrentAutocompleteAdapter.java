package org.evcs.android.features.shared.places;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
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
import org.evcs.android.ui.view.shared.SearchLocationChildFragment;

import java.util.ArrayList;
import java.util.List;

public class PlaceCurrentAutocompleteAdapter extends ArrayAdapter<CustomLocation> implements Filterable {

    private static final int MAX_RESULTS = 6;

    private final PlacesRequestPresenter mPresenter;
    private final ArrayList<AutocompletePrediction> mResultList;
    private final ArrayList<CustomLocation> mResultNames;
    private PlaceCurrentAutocompleteAdapter.AutocompleteListener mListener;
    private String mCurrentLocationString;

    public PlaceCurrentAutocompleteAdapter(Context context, PlacesRequestPresenter presenter, String currentLocationString) {
        super(context, R.layout.adapter_autocomplete_custom, R.id.adapter_autocomplete_custom_text);
        mPresenter = presenter;
        mResultList = new ArrayList<>();
        mResultNames = new ArrayList<>();
        mCurrentLocationString = currentLocationString;
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
        name.setTypeface(Typeface.DEFAULT);
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
                    results.count = 1;
                } else {
                    results.count = resultValues.size() + 1;
                    mResultList.add(0, getCurrentLocationPrediction());
                    mResultList.addAll(resultValues);
                }


                for (int i = 0; i < Math.min(mResultList.size(), MAX_RESULTS); i++) {
                    if (i == 0) {
                        mResultNames.add(new CustomLocation(
                                mResultList.get(i).getFullText(null).toString(),
                                R.drawable.ic_map_car_location));
                    } else {
                        mResultNames.add(new CustomLocation(mResultList.get(i).getFullText(null).toString()));
                    }
                }

                if (mListener != null) {
                    if (mResultList.isEmpty()) {
                        mListener.onResultsRetrieved(null);
                    } else {
                        mListener.onResultsRetrieved(mResultList.get(0).getPlaceId());
                    }
                }

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
        for (int i = 0; i < Math.min(history.size(), MAX_RESULTS - 1); i++) {
           mResultNames.add(new CustomLocation(history.get(i).location.getName(), R.drawable.ic_clock_light_blue));
           mResultList.add(AutocompletePrediction.builder(history.get(i).key).setFullText(history.get(i).location.getName()).build());
        }
        return mResultList.size();
    }

    private AutocompletePrediction getCurrentLocationPrediction() {
        return AutocompletePrediction.builder(mCurrentLocationString)
                .setFullText(mCurrentLocationString)
                .build();
    }

    public interface AutocompleteListener {
        void onResultsRetrieved(String placeId);
    }
}
