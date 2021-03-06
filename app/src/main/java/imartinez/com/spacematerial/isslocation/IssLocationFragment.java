package imartinez.com.spacematerial.isslocation;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import imartinez.com.spacematerial.App;
import imartinez.com.spacematerial.R;
import imartinez.com.spacematerial.base.BaseCleanFragment;
import imartinez.com.spacematerial.isslocation.IssLocationPresenter.IssLocationRouter;
import imartinez.com.spacematerial.isslocation.IssLocationPresenter.IssLocationView;
import javax.inject.Inject;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class IssLocationFragment extends BaseCleanFragment<IssLocationPresenter>
        implements IssLocationView, IssLocationRouter, OnMapReadyCallback {

    @Inject
    IssLocationPresenter issLocationPresenter;

    @BindView(R.id.iss_location_map)
    MapView issLocationMapView;

    private GoogleMap map;
    private Marker locationMarker;
    private Snackbar errorSnackbar;

    public static IssLocationFragment newInstance() {
        IssLocationFragment fragment = new IssLocationFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        DaggerIssLocationComponent.builder()
                .appComponent(((App) getActivity().getApplication()).getAppComponent())
                .build()
                .inject(this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iss_location, container, false);
        ButterKnife.bind(this, view);

        // Obtain the map view and get notified when the map is ready to be used.
        issLocationMapView.onCreate(savedInstanceState);
        issLocationMapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onResume() {
        issLocationMapView.onResume();
        getPresenter().onViewVisibilityChanged(true);
        super.onResume();
    }

    @Override
    public void onPause() {
        issLocationMapView.onPause();
        getPresenter().onViewVisibilityChanged(false);
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // Safe check to avoid calling getPresenter before the view has been fully initialized
        if (issLocationPresenter != null && issLocationPresenter.isBoundToViewAndRouter()) {
            getPresenter().onViewVisibilityChanged(isVisibleToUser);
        }
    }

    @Override
    protected IssLocationPresenter resolvePresenter() {
        issLocationPresenter.bindViewAndRouter(this, this);
        return issLocationPresenter;
    }

    @Override
    public void presentIssLocation(IssLocation issLocation) {
        if (errorSnackbar != null && errorSnackbar.isShown()) {
            errorSnackbar.dismiss();
        }
        // Add a marker in ISS location and move the camera
        LatLng latLng = new LatLng(issLocation.latitude(), issLocation.longitude());
        if (locationMarker != null) {
            locationMarker.remove();
        }
        locationMarker = map.addMarker(
                new MarkerOptions().title(getString(R.string.iss_location_title)).position(latLng));
        locationMarker.setSnippet(getDateCurrentTimeZone(issLocation.timestamp()));
        locationMarker.showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void presentIssRetrievalError() {
        errorSnackbar = Snackbar.make(getView(), R.string.error_fetching_iss_location, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPresenter().onRetrySelected();
                    }
                });
        errorSnackbar.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getPresenter().onMapReady();
    }

    // TODO: 1/11/16 Take this function to DateUtils. Review the function implementation
    public String getDateCurrentTimeZone(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
            return "";
        }
    }
}
