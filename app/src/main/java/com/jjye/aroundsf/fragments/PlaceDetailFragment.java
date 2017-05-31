package com.jjye.aroundsf.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjye.aroundsf.R;
import com.jjye.aroundsf.adapters.PlaceDetailAdapter;
import com.jjye.aroundsf.models.PlaceDetail;
import com.jjye.aroundsf.remoteAccess.api.PlaceService;
import com.jjye.aroundsf.remoteAccess.responses.PlaceDetailResponse;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlaceDetailFragment extends BottomSheetDialogFragment {

    private static String PLACE_ID = "place_id";
    private String mPlaceId;
    private PlaceService.PlaceAPI mPlaceAPI;
    private RecyclerView mRecyclerView;
    private PlaceDetailAdapter mAdapter;
    private Subscription mRxSubs;

    public static PlaceDetailFragment newInstance(String placeId) {
        Bundle args = new Bundle();
        args.putString(PLACE_ID, placeId);
        PlaceDetailFragment fragment = new PlaceDetailFragment();
        fragment.setArguments(args);
        return  fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mPlaceAPI = new PlaceService(getString(R.string.google_places_api)).getAPI();
        Bundle args = getArguments();
        if(args != null) {
            mPlaceId = args.getString(PLACE_ID,"");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_placedetail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(itemDecor);
        mAdapter = new PlaceDetailAdapter();
        mRecyclerView.setAdapter(mAdapter);

        if(mPlaceId != null) {
            mRxSubs = mPlaceAPI.getPlaceDetail(mPlaceId)
                    .map(PlaceDetailResponse.mapToPlaceDetail)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<PlaceDetail>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(PlaceDetail placeDetail) {
                            mAdapter.setPlaceDetail(placeDetail);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRxSubs.unsubscribe();
    }

}
