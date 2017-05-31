package com.jjye.aroundsf.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjye.aroundsf.R;
import com.jjye.aroundsf.models.Place;
import com.jjye.aroundsf.viewHolders.PlaceListViewHolder;

import java.util.List;

/**
 * Created by jjye on 5/31/17.
 */

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListViewHolder> {

    private List<Place> mPlaces;
    private ItemClickListener mListener;

    public PlaceListAdapter(List<Place> places, ItemClickListener listener) {
        mPlaces = places;
        mListener = listener;
    }

    @Override
    public PlaceListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_place_list_item, parent, false);
        PlaceListViewHolder vh = new PlaceListViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlaceListViewHolder holder, int position) {
        holder.bind(mPlaces.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public interface ItemClickListener {
        void onPlaceClicked(String placeId);
    }
}
