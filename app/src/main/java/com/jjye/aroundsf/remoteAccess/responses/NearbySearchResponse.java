package com.jjye.aroundsf.remoteAccess.responses;

import com.jjye.aroundsf.models.Place;

import java.util.List;

/**
 * Created by jjye on 5/30/17.
 */

public class NearbySearchResponse {
    private List<Place> results;

    public List<Place> getResults() {
        return results;
    }
}
