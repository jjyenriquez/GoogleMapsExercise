package com.jjye.aroundsf.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jjye.aroundsf.R;
import com.jjye.aroundsf.adapters.PlaceListAdapter;
import com.jjye.aroundsf.app.AroundSFApp;
import com.jjye.aroundsf.models.Place;
import com.jjye.aroundsf.models.PlaceDetail;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jjye on 5/31/17.
 */

public class PlaceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.placeViewContainer)
    RelativeLayout placeViewContainer;

    @BindView(R.id.imgView)
    ImageView imgView;

    @BindView(R.id.textName)
    TextView textName;

    @BindView(R.id.textAddress)
    TextView textAddress;

    @BindView(R.id.textContact)
    TextView textContact;

    @BindView(R.id.textRating)
    TextView textRating;

    public PlaceViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bind(PlaceDetail place) {
        if(place == null) {
            return;
        }

        if(place.getPhotos() == null || place.getPhotos().isEmpty()) {
            imgView.setImageResource(android.R.color.darker_gray);
        } else {
            Glide.with(AroundSFApp.getInstance())
                    .load(place.getPhotos().get(0).toURLString())
                    .placeholder(android.R.color.darker_gray)
                    .into(imgView);
        }

        textName.setText(place.getName());
        textAddress.setText(place.getFormattedAddress());
        textContact.setText(place.getFormattedPhoneNumber());
        textRating.setText(String.valueOf(place.getRating()));
    }

    public void bindLight(final Place place, final PlaceListAdapter.ItemClickListener listener) {
        if(place == null) {
            return;
        }

        if(place.getPhotos() == null || place.getPhotos().isEmpty()) {
            imgView.setImageResource(android.R.color.darker_gray);
        } else {
            Glide.with(AroundSFApp.getInstance())
                    .load(place.getPhotos().get(0).toURLString())
                    .centerCrop()
                    .placeholder(android.R.color.darker_gray)
                    .into(imgView);
        }

        textName.setText(place.getName());
        textRating.setText("Rating:" + place.getRating());
        textContact.setVisibility(View.GONE);
        textAddress.setVisibility(View.GONE);

        placeViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPlaceClicked(place.getPlaceId());
            }
        });
    }

}
