package com.framgia.soundcloud.ui.fragment;

import android.app.ProgressDialog;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.k.soundcloud.R;
import com.framgia.soundcloud.ui.activity.AlbumAdapter;
import com.framgia.soundcloud.ui.activity.AlbumItem;
import com.framgia.soundcloud.ui.activity.ItemDecoration;
import com.framgia.soundcloud.ui.activity.model.AlbumModel;
import com.framgia.soundcloud.ui.activity.model.SearchResponse;
import com.framgia.soundcloud.ui.activity.service.ServiceGenerator;
import com.framgia.soundcloud.ui.activity.service.SoundCloudService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.framgia.soundcloud.ui.activity.Const.CLIENT_ID;
import static com.framgia.soundcloud.ui.activity.Const.CROP_SIZE;
import static com.framgia.soundcloud.ui.activity.Const.DEFAULT_SIZE;

/**
 * Created by K on 5/19/2017.
 */
public class AlbumListFragment extends Fragment{
    private static final int mLimit = 50;
    private RecyclerView mRecyclerView;
    private AlbumAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private SearchView mSearch;
    private ArrayList<AlbumItem> mAlbumItems;
    private String mSearchText;
    private ProgressBar mProgressBar;
    private int mColNum = 3;
    private int mOffset = 0;
    private int mSpacing = 10;
    private int mVisible, mTotal, mFirst;
    private boolean mLoading = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_album_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_album_list);
        mLayoutManager = new GridLayoutManager(getActivity(), mColNum);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new ItemDecoration(mSpacing));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mVisible = mRecyclerView.getChildCount();
                    mTotal = mLayoutManager.getItemCount();
                    mFirst = mLayoutManager.findFirstVisibleItemPosition();
                    if (mLoading && mVisible + mFirst >= mTotal) {
                            mLoading = false;
                            mOffset = mOffset + mLimit;
                            getData(mOffset);
                    }
                }
                mLoading = true;
            }
        });
        mSearch = (SearchView) v.findViewById(R.id.search_view);
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchText = query;
                mProgressBar.setVisibility(View.VISIBLE);
                getData(mOffset);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_loading);
        return v;
    }

    public void getData(int offset) {
        SoundCloudService service = ServiceGenerator.createService(SoundCloudService.class);
        service.getSearch(CLIENT_ID, mSearchText, mLimit, offset)
            .enqueue(new Callback<SearchResponse>
                () {
                @Override
                public void onResponse(Call<SearchResponse> call,
                                       Response<SearchResponse> response) {
                    if (response != null) {
                        SearchResponse model = response.body();
                        loadData(model);
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void loadData(SearchResponse model) {
        List<SearchResponse.Collection> list = model.getCollection();
        if (mOffset == 0) mAlbumItems = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKind().equals("user")) continue;
            String url = list.get(i).getArtworkUrl();
            if (url != null) list.get(i).setArtworkUrl(url.replace(DEFAULT_SIZE, CROP_SIZE));
            AlbumItem item = new AlbumItem(model,i);
            item.setId(list.get(i).getId());
            item.setKind(list.get(i).getKind());
            mAlbumItems.add(item);
        }
        setAdapter();
    }

    public void setAdapter() {
        if (mOffset == 0) {
            mAdapter = new AlbumAdapter(mAlbumItems);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}