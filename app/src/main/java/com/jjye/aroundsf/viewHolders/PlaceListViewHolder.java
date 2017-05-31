package com.jjye.aroundsf.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jjye.aroundsf.R;
import com.jjye.aroundsf.adapters.PlaceListAdapter;
import com.jjye.aroundsf.app.AroundSFApp;
import com.jjye.aroundsf.models.Place;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jjye on 5/31/17.
 */

public class PlaceListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgView)
    ImageView imgView;

    @BindView(R.id.textName)
    TextView textName;

    @BindView(R.id.textRating)
    TextView textRating;

    public PlaceListViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }


    public void bind(final Place place, final PlaceListAdapter.ItemClickListener listener) {
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

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPlaceClicked(place.getPlaceId());
            }
        });
    }

}
