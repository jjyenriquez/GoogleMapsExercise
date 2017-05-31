package com.jjye.aroundsf.remoteAccess.api;


import com.jjye.aroundsf.remoteAccess.responses.NearbySearchResponse;
import com.jjye.aroundsf.remoteAccess.responses.PlaceDetailResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jjye on 5/30/17.
 */

public class PlaceService extends BaseService {
    public PlaceService(String restUrl) {
        super(restUrl);
    }

    public PlaceAPI getAPI() {
        return getRetrofit().create(PlaceAPI.class);
    }

    public interface PlaceAPI {
        @GET("nearbysearch/json?rankby=distance&type=restaurant")
        public Observable<NearbySearchResponse> getNearbyPlaces(@Query("location") String LatLng);

        @GET("nearbysearch/json?radius=5000&type=restaurant")
        public Observable<NearbySearchResponse> getNearbyProminentPlaces(@Query("location") String LatLng);

        @GET("nearbysearch/json?radius=5000&type=restaurant")
        public Observable<NearbySearchResponse> getNearbyProminentPlaces(@Query("location") String LatLng, @Query("pagetoken") String pageToken);


        @GET("details/json?")
        public Observable<PlaceDetailResponse> getPlaceDetail(@Query("placeid") String placeid);
    }
}
