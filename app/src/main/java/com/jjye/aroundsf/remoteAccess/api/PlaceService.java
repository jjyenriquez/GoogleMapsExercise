package com.jjye.aroundsf.remoteAccess.api;


import com.jjye.aroundsf.remoteAccess.responses.NearbySearchResponse;

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
        @GET("nearbysearch/json?rankby=distance")
        public Observable<NearbySearchResponse> getNearbyPlaces(@Query("type") String type, @Query("location") String LatLng);
    }
}
