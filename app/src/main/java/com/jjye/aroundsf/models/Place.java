package com.jjye.aroundsf.models;

import java.util.List;

/**
 * Created by jjye on 5/30/17.
 */

public class Place {
    private Geometry geometry;
    private String name;
    private String vicinity;
    private String placeId;
    private List<Photo> photos;
    private Float rating;

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

    public List<Photo> getPhotos() {
        return photos;
    }

    public Float getRating() {
        return rating;
    }
}
