package com.jjye.aroundsf.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjye.aroundsf.R;
import com.jjye.aroundsf.models.PlaceDetail;
import com.jjye.aroundsf.viewHolders.PlaceReviewViewHolder;
import com.jjye.aroundsf.viewHolders.PlaceViewHolder;

/**
 * Created by jjye on 5/31/17.
 */

public class PlaceDetailAdapter extends RecyclerView.Adapter {

    PlaceDetail mPlaceDetail;

    public PlaceDetailAdapter() {

    }

    public void setPlaceDetail(PlaceDetail placeDetail) {
        mPlaceDetail = placeDetail;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return R.layout.view_place;
        } else {
            return R.layout.view_place_review;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        RecyclerView.ViewHolder vh;
        switch (viewType) {
            case R.layout.view_place:
                vh = new PlaceViewHolder(view);
                break;
            default:
                vh = new PlaceReviewViewHolder(view);
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlaceViewHolder) {
            ((PlaceViewHolder) holder).bind(mPlaceDetail);
        } else {
            ((PlaceReviewViewHolder) holder).bind(mPlaceDetail.getReviews().get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        if (mPlaceDetail == null) {
            return 0;
        } else if (mPlaceDetail.getReviews() == null) {
            return 1;
        } else {
            return mPlaceDetail.getReviews().size() + 1;
        }
    }

}
