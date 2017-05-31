package com.jjye.aroundsf.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jjye.aroundsf.R;
import com.jjye.aroundsf.models.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jjye on 5/31/17.
 */

public class PlaceReviewViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textName)
    TextView textName;

    @BindView(R.id.textRating)
    TextView textRating;

    @BindView(R.id.textContent)
    TextView textContent;

    public PlaceReviewViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bind(Review review) {
        textName.setText(review.getAuthorName());
        textRating.setText("Rating: " + String.valueOf(review.getRating()));
        textContent.setText(review.getText());
    }

}
