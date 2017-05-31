package com.jjye.aroundsf.activities;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.jjye.aroundsf.R;
import com.jjye.aroundsf.adapters.PlaceListAdapter;
import com.jjye.aroundsf.fragments.PlaceDetailFragment;
import com.jjye.aroundsf.models.Place;
import com.jjye.aroundsf.remoteAccess.api.PlaceService;
import com.jjye.aroundsf.remoteAccess.responses.NearbySearchResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class PlacesListActivity extends AppCompatActivity implements PlaceListAdapter.ItemClickListener{

    private int THRESHOLD = 5;
    private PlaceService.PlaceAPI mPlaceAPI;
    private CompositeSubscription mSubsBucket;
    private PlaceListAdapter mAdapter;
    private List<Place> mPlaces;
    private boolean mIsLoading = true;
    private String mNextPageToken = null;
    private BottomSheetDialogFragment mBottomSheet;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        ButterKnife.bind(this);
        mPlaceAPI = new PlaceService(getString(R.string.google_places_api)).getAPI();
        mSubsBucket = new CompositeSubscription();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mPlaces = new ArrayList<>();
        mAdapter = new PlaceListAdapter(mPlaces, this);
        mRecyclerView.setAdapter(mAdapter);

        // Pagination
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (!mIsLoading && (visibleItemCount + firstVisibleItem) > (totalItemCount - THRESHOLD) && !TextUtils.isEmpty(mNextPageToken)) {
                        getNextPlaces();
                }
            }
        });

        mSubsBucket.add(mPlaceAPI.getNearbyProminentPlaces(getString(R.string.sf_lat) + "," + getString(R.string.sf_lng))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getNearbySearchSubScriber()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubsBucket.unsubscribe();
    }

    private void getNextPlaces() {
        mIsLoading = true;
        mSubsBucket.add(mPlaceAPI.getNearbyProminentPlaces(getString(R.string.sf_lat) + "," + getString(R.string.sf_lng), mNextPageToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getNearbySearchSubScriber()));
    }

    private Subscriber<NearbySearchResponse> getNearbySearchSubScriber() {
        return new Subscriber<NearbySearchResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mIsLoading = false;
                    }

                    @Override
                    public void onNext(NearbySearchResponse nearbySearchResponse) {
                        mIsLoading = false;
                        mNextPageToken = nearbySearchResponse.getNextPageToken();
                        int end = mPlaces.size();
                        mPlaces.addAll(nearbySearchResponse.getResults());
                        mAdapter.notifyItemRangeInserted(end, nearbySearchResponse.getResults().size());
                    }
                };
    }

    @Override
    public void onPlaceClicked(String placeId) {
        mBottomSheet = PlaceDetailFragment.newInstance(placeId);
        mBottomSheet.show(getSupportFragmentManager(), mBottomSheet.getTag());
    }

}
