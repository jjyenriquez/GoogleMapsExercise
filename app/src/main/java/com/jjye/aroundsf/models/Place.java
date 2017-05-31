package com.jjye.aroundsf.models;

/**
 * Created by jjye on 5/30/17.
 */

public class Place {
    private Geometry geometry;
    private String name;
    private String vicinity;
    private String placeId;

    public Geometry getGeometry() {
        return geometry;
    }

    public String getName() {
        return name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getPlaceId() {
        return placeId;
    }
}
