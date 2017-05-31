package com.jjye.aroundsf.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jjye.aroundsf.R;
import com.jjye.aroundsf.models.Geometry;
import com.jjye.aroundsf.models.Place;
import com.jjye.aroundsf.remoteAccess.api.PlaceService;
import com.jjye.aroundsf.remoteAccess.responses.NearbySearchResponse;

import java.util.HashSet;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener {
    private GoogleMap mMap;

    private HashSet<String> mPlaceIdSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mPlaceIdSet = new HashSet<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sanFran = new LatLng(37.7748295, -122.4194155);
        Marker marker = mMap.addMarker(new MarkerOptions().position(sanFran)
                        .title("YOU").draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_person)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sanFran));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        mMap.setOnMarkerDragListener(this);


        PlaceService.PlaceAPI placeAPI = new PlaceService(getString(R.string.google_places_api)).getAPI();
        placeAPI.getNearbyPlaces("restaurant", String.format("%.4f,%.4f", sanFran.latitude, sanFran.longitude))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NearbySearchResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NearbySearchResponse response) {
                        Log.e("JJJ", response.getResults().size() + " -- total");
                        placePins(response.getResults());
                    }
                });
    }

    public void onLocationChanged(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        PlaceService.PlaceAPI placeAPI = new PlaceService(getString(R.string.google_places_api)).getAPI();
        placeAPI.getNearbyPlaces("restaurant", String.format("%.4f,%.4f", latLng.latitude, latLng.longitude))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NearbySearchResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("JJJ", "ERROR" + e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(NearbySearchResponse response) {
                        Log.e("JJJ", response.getResults().size() + " -- total");
                        placePins(response.getResults());
                    }
                });
    }

    private void placePins(List<Place> result) {
        if (result != null) {
            for (Place p : result) {
                if(mPlaceIdSet.contains(p.getPlaceId())) {
                    continue;
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(p.getName());
                markerOptions.snippet(p.getVicinity());
                Geometry.Location location = p.getGeometry().getLocation();
                markerOptions.position(new LatLng(location.getLat(), location.getLng()));
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_resto));
                markerOptions.alpha(0.9f);
                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(p.getPlaceId());
                mPlaceIdSet.add(p.getPlaceId());
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
        if(marker.getTitle().equals("YOU")) {
            Log.e("JJJ", "YOU MOVED");
            onLocationChanged(marker.getPosition());
        } else Log.e("JJJ", "YOU NOT MOVED");
    }
}
