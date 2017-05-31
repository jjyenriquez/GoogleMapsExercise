package com.jjye.aroundsf.activities;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jjye.aroundsf.R;
import com.jjye.aroundsf.fragments.PlaceDetailFragment;
import com.jjye.aroundsf.models.Geometry;
import com.jjye.aroundsf.models.Place;
import com.jjye.aroundsf.remoteAccess.api.PlaceService;
import com.jjye.aroundsf.remoteAccess.responses.NearbySearchResponse;

import java.util.HashSet;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private PlaceService.PlaceAPI mPlaceAPI;
    private BottomSheetDialogFragment mBottomSheet;
    private HashSet<String> mPlaceIdSet;
    private CompositeSubscription mSubsBucket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mPlaceIdSet = new HashSet<>();
        mPlaceAPI = new PlaceService(getString(R.string.google_places_api)).getAPI();
        mSubsBucket = new CompositeSubscription();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubsBucket.unsubscribe();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double lat = Double.parseDouble(getString(R.string.sf_lat));
        double lng = Double.parseDouble(getString(R.string.sf_lng));
        LatLng sanFran = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sanFran)
                .title("YOU").draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_person)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sanFran));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);

        mSubsBucket.add(mPlaceAPI.getNearbyPlaces(latLngToString(sanFran))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getNearbySearchSubscriber()));
    }

    public void onLocationChanged(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mSubsBucket.add(mPlaceAPI.getNearbyPlaces(latLngToString(latLng))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getNearbySearchSubscriber()));
    }

    private void placePins(List<Place> result, HashSet<String> existingMarkers) {
        if (result != null) {
            for (Place p : result) {
                if (existingMarkers.contains(p.getPlaceId())) {
                    continue;
                }
                MarkerOptions markerOptions = new MarkerOptions();
                Geometry.Location location = p.getGeometry().getLocation();
                markerOptions.title(p.getName()).snippet(p.getVicinity())
                        .position(new LatLng(location.getLat(), location.getLng()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_resto))
                        .alpha(0.9f);
                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(p.getPlaceId());
                existingMarkers.add(p.getPlaceId());
            }
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        onLocationChanged(marker.getPosition());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String placeId = (String) marker.getTag();
        mBottomSheet = PlaceDetailFragment.newInstance(placeId);
        mBottomSheet.show(getSupportFragmentManager(), mBottomSheet.getTag());
        return false;
    }

    private Subscriber<NearbySearchResponse> getNearbySearchSubscriber() {
        return new Subscriber<NearbySearchResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NearbySearchResponse response) {
                placePins(response.getResults(), mPlaceIdSet);
            }
        };
    }

    private String latLngToString(LatLng latLng) {
        return String.format("%.4f,%.4f", latLng.latitude, latLng.longitude);
    }
}
