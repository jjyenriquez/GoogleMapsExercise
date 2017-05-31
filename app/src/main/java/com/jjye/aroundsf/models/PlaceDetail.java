package com.jjye.aroundsf.models;

import java.util.List;

/**
 * Created by jjye on 5/31/17.
 */

public class PlaceDetail {
    private String name;
    private String formattedAddress;
    private String formattedPhoneNumber;
    private Float rating;
    private List<Photo> photos;
    private List<Review> reviews;

    public String getName() {
        return name;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public Float getRating() {
        return rating;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
