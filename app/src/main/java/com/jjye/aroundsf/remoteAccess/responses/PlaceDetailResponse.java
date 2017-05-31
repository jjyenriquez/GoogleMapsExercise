package com.jjye.aroundsf.remoteAccess.responses;

import com.jjye.aroundsf.models.PlaceDetail;

import rx.functions.Func1;

/**
 * Created by CYE on 5/31/17.
 */

public class PlaceDetailResponse {
    private PlaceDetail result;

    public PlaceDetail getResult() {
        return result;
    }

    public static Func1<PlaceDetailResponse, PlaceDetail> mapToPlaceDetail = new Func1<PlaceDetailResponse, PlaceDetail>() {
        @Override
        public PlaceDetail call(PlaceDetailResponse placeDetailResponse) {
            return placeDetailResponse.getResult();
        }
    };
}
