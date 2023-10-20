package org.evcs.android.features.map.search;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.core.permission.PermissionListener;
import com.base.core.permission.PermissionManager;
import com.base.core.util.KeyboardUtils;
import com.base.core.util.ToastUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.R;
import org.evcs.android.databinding.FragmentSearchLocationChildBinding;
import org.evcs.android.features.shared.EVCSDialogFragment;
import org.evcs.android.features.shared.places.PlaceCurrentAutocompleteAdapter;
import org.evcs.android.model.shared.RequestError;
import org.evcs.android.network.serializer.GenericListDeserializer;
import org.evcs.android.ui.fragment.LoadingFragment;
import org.evcs.android.util.Extras;
import org.evcs.android.util.StorageUtils;
import org.evcs.android.util.WordingUtils;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fragment with FilterableAutoCompleteTextView used specifically for lists of places. The current
 * location is always shown as the first option.
 */
public class SearchLocationChildFragment extends LoadingFragment<SearchLocationChildPresenter> implements ISearchLocationView {

    private String mCurrentLocationString;
    private String mSearchByName;
    private String mNoLocationTitlePath;
    private String mNoLocationSubtitlePath;
    private String mNoPermissionsSubtitlePath;

    private FilterableAutocompleteTextView mAddress;

    protected ISearchLocationListener mListener;
    private PlaceCurrentAutocompleteAdapter mAdapter;
    private Location mCurrentLocation;
    private boolean mIsLocationPermissionEnabled;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mClearOnDelete;
    private String mDefaultText;
    private String mHint;

    public static SearchLocationChildFragment newInstance() {

        Bundle args = new Bundle();

        SearchLocationChildFragment fragment = new SearchLocationChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init() {
        mAdapter = new PlaceCurrentAutocompleteAdapter(getContext(), getPresenter().getPlacesPresenter(),
                mCurrentLocationString, mSearchByName);
        mAddress.setAdapter(mAdapter);
        mAddress.setText(mDefaultText);
        if (mHint != null)
            mAddress.setHint(mHint);
        mAddress.setDropDownVerticalOffset(7);
        getPresenter().setGoogleApiClient(getContext());
        requestLocationPermission();
    }

    @Override
    public void setUi(View v) {
        FragmentSearchLocationChildBinding binding = FragmentSearchLocationChildBinding.bind(v);
        mCurrentLocationString = getString(R.string.fragment_search_current_location);
        mSearchByName = getString(R.string.fragment_search_search_by_name);
        mNoLocationTitlePath = getString(R.string.carsharing_location_error_title_path);
        mNoLocationSubtitlePath = getString(R.string.carsharing_location_error_subtitle_path);
        mNoPermissionsSubtitlePath = getString(R.string.carsharing_gps_permissions_error_subtitle_path);
        mAddress = binding.fragmentSearchLocationAddress;
    }

    @Override
    public int layout() {
        return R.layout.fragment_search_location_child;
    }

    public SearchLocationChildPresenter createPresenter() {
        //Strings are not yet bound
        return new SearchLocationChildPresenter(this, getString(R.string.fragment_search_current_location));
    }

    @SuppressLint("MissingPermission")
    private void setCurrentLocationListener() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mCurrentLocation = location;
                }
            }
        });
    }

    @Override
    public void setListeners() {
        mAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                String placeId = mAdapter.getPlaceId(position);
                if (position == 0) {
                    onSearchByNameClick(placeId);
                    return;
                }
                org.evcs.android.model.Location location = findInLocationHistory(placeId);
                if (location == null)
                    getPresenter().getPlaceFromId(placeId);
                else
                    mListener.onLocationChosen(location);
            }
        });
        mAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (mClearOnDelete && before - count == 1) {
                    mClearOnDelete = false;
                    mAddress.setText("");
                    mListener.onLocationRemoved();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        //Yes I need both
        mAddress.setOnClickListener(v -> showHistory());
        mAddress.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showHistory();
        });
    }

    private void onSearchByNameClick(String placeId) {
        //do this better
        String query = placeId.split(":")[1];
        ToastUtils.show(query);
        mAddress.setText(query);
    }

    private void showHistory() {
        if (mAddress.length() <= BaseConfiguration.AUTOCOMPLETE_ADAPTER_THRESHOLD) {
            mAdapter.showHistory();
            mAdapter.notifyDataSetChanged();
            mAddress.post(() -> mAddress.showDropDown());
        }
    }

    public void setClearOnDelete(boolean clear) {
        mClearOnDelete = clear;
    }

    public void setListener(ISearchLocationListener listener) {
        mListener = listener;
    }

    public void onPlaceRetrieved(@NonNull Place response) {
        mAddress.setText(response.getAddress());
        onLocationChosen(response.getAddress(), response.getLatLng(), response.getViewport());
        mClearOnDelete = true;
    }

    public void onCurrentLocationRetrieved() {
        mAddress.setText(mCurrentLocationString);
        if (mCurrentLocation != null) {
            onLocationChosen(mCurrentLocationString,
                    new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), null);

        } else if (mIsLocationPermissionEnabled) {
            onLocationError();
        } else {
            onPermissionsError();
        }
        mClearOnDelete = true;
    }

    private void onLocationChosen(String address, LatLng latLng, @Nullable LatLngBounds viewport) {
        mAddress.dismissDropDown();
        KeyboardUtils.hideKeyboard(getActivity());
        mListener.onLatLngChosen(address, latLng, viewport);
    }

    public void onPermissionsGranted() {
        mIsLocationPermissionEnabled = true;
        if (getContext() != null)
            setCurrentLocationListener();
    }

    public void onPermissionsDenied() {
        mIsLocationPermissionEnabled = false;
        onPermissionsError();
    }

    public void onLocationError() {
        showPopUp(
                WordingUtils.getWording(mNoLocationTitlePath),
                WordingUtils.getWording(mNoLocationSubtitlePath)
        );
    }

    public void onPermissionsError() {
        showPopUp(
                WordingUtils.getWording(mNoLocationTitlePath),
                WordingUtils.getWording(mNoPermissionsSubtitlePath)
        );
    }

    public void setHint(String text) {
        mHint = text;
    }

    @Override
    public void showError(@NonNull RequestError requestError) {
        ToastUtils.show(requestError.getBody());
    }

    public void showPopUp(String title, String subtitle) {
        new EVCSDialogFragment.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .addButton(getString(R.string.app_ok), new EVCSDialogFragment.OnClickListener() {
                    @Override
                    public void onClick(@NonNull EVCSDialogFragment fragment) {
                        onBackPressed();
                        fragment.dismiss();
                    }
                }).show(getFragmentManager());
    }

    private void requestLocationPermission() {
        PermissionManager.getInstance().requestPermission(getActivity(), new PermissionListener() {
            @Override
            public void onPermissionsGranted() {
                SearchLocationChildFragment.this.onPermissionsGranted();
            }

            @Override
            public void onPermissionsDenied(@NonNull String[] deniedPermissions) {
                SearchLocationChildFragment.this.onPermissionsDenied();
            }
        }, ACCESS_FINE_LOCATION);

    }

    public static void saveToLocationHistory(org.evcs.android.model.Location location) {
        saveToLocationHistory(new LocationHistoryItem(location, Integer.toString(location.hashCode())));
    }

    static void saveToLocationHistory(LocationHistoryItem item) {
        List<LocationHistoryItem> history = getLocationHistory();
        history.remove(item);
        history.add(0, item);
        StorageUtils.storeInSharedPreferences(Extras.SearchActivity.HISTORY, new ArrayList<>(history));
    }

    public static List<LocationHistoryItem> getLocationHistory() {
        String json = StorageUtils.getStringFromSharedPreferences(Extras.SearchActivity.HISTORY, "");
        List<LocationHistoryItem> result = GenericListDeserializer.deserialize(json, LocationHistoryItem.class);
        return result == null ? new ArrayList<>() : result;
    }

    public static @Nullable org.evcs.android.model.Location findInLocationHistory(String key) {
        List<LocationHistoryItem> locationHistory = getLocationHistory();
        for (LocationHistoryItem i : locationHistory) {
            if (i.key.equals(key)) return i.location;
        }
        return null;
    }

    public static class LocationHistoryItem implements Serializable {
        public org.evcs.android.model.Location location;
        public String key;

        public LocationHistoryItem(org.evcs.android.model.Location location, String id) {
            this.location = location;
            key = id;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            return Objects.equals(this.location.getId(), ((LocationHistoryItem) o).location.getId());
        }

    }

    public void setDefault(String defaultText) {
        mDefaultText = defaultText;
    }

    public interface ISearchLocationListener {

        void onLatLngChosen(@NonNull String address, @NonNull LatLng latLng, LatLngBounds viewport);

        void onLocationChosen(@NonNull org.evcs.android.model.Location location);

        void onLocationRemoved();
    }

}