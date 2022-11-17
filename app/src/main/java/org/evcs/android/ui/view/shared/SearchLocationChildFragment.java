package org.evcs.android.ui.view.shared;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;

import com.base.core.permission.PermissionListener;
import com.base.core.permission.PermissionManager;
import com.base.core.util.KeyboardUtils;
import com.base.core.util.ToastUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;

import org.evcs.android.R;
import org.evcs.android.databinding.FragmentSearchLocationChildBinding;
import org.evcs.android.features.ISearchLocationView;
import org.evcs.android.features.shared.places.PlaceCurrentAutocompleteAdapter;
import org.evcs.android.model.shared.RequestError;
import org.evcs.android.ui.fragment.LoadingFragment;
import org.evcs.android.util.WordingUtils;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Fragment with FilterableAutoCompleteTextView used specifically for lists of places. The current
 * location is always shown as the first option.
 */
public class SearchLocationChildFragment extends LoadingFragment<SearchLocationChildPresenter> implements ISearchLocationView {

    private String mCurrentLocationString;
    private String mNoLocationTitlePath;
    private String mNoLocationSubtitlePath;
    private String mNoPermissionsSubtitlePath;

    private FilterableAutocompleteTextView mAddress;
    private View mClose;

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
                mCurrentLocationString);
        mAddress.setAdapter(mAdapter);
        mAddress.setText(mDefaultText);
        if (mHint != null)
            mAddress.setHint(mHint);
        getPresenter().setGoogleApiClient(getContext());
        requestLocationPermission();
    }

    @Override
    public void setUi(View v) {
        FragmentSearchLocationChildBinding binding = FragmentSearchLocationChildBinding.bind(v);
        mCurrentLocationString = getString(R.string.fragment_search_current_location);
        mNoLocationTitlePath = getString(R.string.carsharing_location_error_title_path);
        mNoLocationSubtitlePath = getString(R.string.carsharing_location_error_subtitle_path);
        mNoPermissionsSubtitlePath = getString(R.string.carsharing_gps_permissions_error_subtitle_path);
        mAddress = binding.fragmentSearchLocationAddress;
        mClose = binding.fragmentSearchLocationClose;
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
                getPresenter().getPlaceFromId(mAdapter.getPlaceId(position));
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
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCloseClicked();
            }
        });
    }

    public void setClearOnDelete(boolean clear) {
        mClearOnDelete = clear;
    }

    public void setListener(ISearchLocationListener listener) {
        mListener = listener;
    }

    public void onPlaceRetrieved(@NonNull Place response) {
        mAddress.setText(response.getAddress().toString());
        onLocationChosen(response.getAddress().toString(), response.getLatLng());
        mClearOnDelete = true;
    }

    public void onCurrentLocationRetrieved() {
        mAddress.setText(mCurrentLocationString);
        if (mCurrentLocation != null) {
            onLocationChosen(mCurrentLocationString,
                    new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));

        } else if (mIsLocationPermissionEnabled) {
            onLocationError();
        } else {
            onPermissionsError();
        }
        mClearOnDelete = true;
    }

    private void onLocationChosen(String address, LatLng latLng) {
        mAddress.dismissDropDown();
        KeyboardUtils.hideKeyboard(getActivity());
        mListener.onLocationChosen(address, latLng);
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
//        new GreenCommuterDialogFragment.Builder()
//                .setTitle(title)
//                .setSubtitle(subtitle)
//                .addButton(getString(R.string.app_ok), new GreenCommuterDialogFragment.OnClickListener() {
//                    @Override
//                    public void onClick(@NonNull GreenCommuterDialogFragment fragment) {
//                        onBackPressed();
//                        fragment.dismiss();
//                    }
//                }).show(getFragmentManager());
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

    public void setDefault(String defaultText) {
        mDefaultText = defaultText;
    }

    public interface ISearchLocationListener {

        void onLocationChosen(@NonNull String address, @NonNull LatLng latLng);

        void onLocationRemoved();

        void onCloseClicked();
    }

}