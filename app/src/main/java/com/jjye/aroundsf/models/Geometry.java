package com.jjye.aroundsf.models;


/**
 * Created by jjye on 5/30/17.
 */

public class Geometry {

    private Location location;

    public Location getLocation() {
        return location;
    }

    public static class Location {
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }
}
