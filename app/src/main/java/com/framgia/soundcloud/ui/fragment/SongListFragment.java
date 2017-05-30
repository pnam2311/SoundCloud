package com.framgia.soundcloud.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.k.soundcloud.R;
import com.framgia.soundcloud.ui.activity.ItemDecoration;
import com.framgia.soundcloud.ui.activity.PlayerActivity;
import com.framgia.soundcloud.ui.activity.SongAdapter;
import com.framgia.soundcloud.ui.activity.SongItem;
import com.framgia.soundcloud.ui.activity.model.AlbumModel;
import com.framgia.soundcloud.ui.activity.model.SongModel;
import com.framgia.soundcloud.ui.activity.service.ServiceGenerator;
import com.framgia.soundcloud.ui.activity.service.SoundCloudService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.framgia.soundcloud.ui.activity.Const.CLIENT_ID;
import static com.framgia.soundcloud.ui.activity.Const.ID_KEY;
import static com.framgia.soundcloud.ui.activity.Const.KIND_KEY;
import static com.framgia.soundcloud.ui.activity.Const.LOADING_MESSAGE;
import static com.framgia.soundcloud.ui.activity.Const.NO_SONG;
import static com.framgia.soundcloud.ui.activity.Const.TYPE_PLAYLIST;
import static com.framgia.soundcloud.ui.activity.Const.TYPE_TRACK;

/**
 * Created by ducpm on 21/05/17.
 */
public class SongListFragment extends Fragment {
    private int mId;
    private ProgressBar mProgressBar;
    private String mKind;
    private RecyclerView mRecyclerView;
    private SongAdapter mAdapter;
    private ArrayList<SongItem> mSongs;
    private int mSpace = 5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_song_list, container, false);
        mId = getArguments().getInt(ID_KEY);
        mKind = getArguments().getString(KIND_KEY);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_song_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new ItemDecoration(mSpace));
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_loading_song);
        if (mKind.equals(TYPE_TRACK)) getSong();
        else getPlaylist();
        return v;
    }

    public void getSong() {
        mProgressBar.setVisibility(View.VISIBLE);
        SoundCloudService service = ServiceGenerator.createService(SoundCloudService.class);
        service.getSong(mId, CLIENT_ID).enqueue(new Callback<SongModel>() {
            @Override
            public void onResponse(Call<SongModel> call, Response<SongModel> response) {
                if (response != null) {
                    SongModel model = response.body();
                    mSongs = new ArrayList<>();
                    mSongs.add(new SongItem(model));
                }
                mAdapter = new SongAdapter(mSongs);
                mRecyclerView.setAdapter(mAdapter);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<SongModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPlaylist() {
        SoundCloudService service = ServiceGenerator.createService(SoundCloudService.class);
        service.getAlbum(mId, CLIENT_ID).enqueue(new Callback<AlbumModel>() {
            @Override
            public void onResponse(Call<AlbumModel> call, Response<AlbumModel> response) {
                if (response != null) {
                    AlbumModel model = response.body();
                    mSongs = new ArrayList<>();
                    int size = model.getPlaylist().size();
                    if (size != 0) {
                        for (int i = 0; i < size; i++) {
                            mSongs.add(new SongItem(model.getPlaylist().get(i)));
                        }
                        mAdapter = new SongAdapter(mSongs);
                        mRecyclerView.setAdapter(mAdapter);
                    } else
                        Toast.makeText(getActivity(), NO_SONG, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AlbumModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static SongListFragment newInstance(int id, String kind) {
        SongListFragment fragment = new SongListFragment();
        Bundle args = new Bundle();
        args.putInt(ID_KEY, id);
        args.putString(KIND_KEY, kind);
        fragment.setArguments(args);
        return fragment;
    }
}

