package com.jjye.aroundsf.models;

import com.jjye.aroundsf.R;
import com.jjye.aroundsf.app.AroundSFApp;

/**
 * Created by jjye on 5/31/17.
 */

public class Photo {
    private String photoReference;

    public String getPhotoReference() {
        return photoReference;
    }

    public String toURLString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AroundSFApp.getInstance().getString(R.string.google_places_api))
                .append("photo?maxwidth=400&photoreference=")
                .append(photoReference)
                .append("&key=")
                .append(AroundSFApp.getInstance().getString(R.string.google_places_api_key));
        return sb.toString();
    }
}
