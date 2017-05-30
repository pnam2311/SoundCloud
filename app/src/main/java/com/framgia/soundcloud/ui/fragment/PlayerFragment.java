package com.framgia.soundcloud.ui.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.k.soundcloud.R;
import com.framgia.soundcloud.ui.MediaService;

import static com.framgia.soundcloud.ui.activity.Const.NEXT_SONG;
import static com.framgia.soundcloud.ui.activity.Const.PLAY_SONG;
import static com.framgia.soundcloud.ui.activity.Const.PREVIOUS_SONG;
import static com.framgia.soundcloud.ui.activity.Const.UPDATE_UI;

/**
 * Created by K on 5/11/2017.
 */
public class PlayerFragment extends Fragment {
    private ImageView mPlay, mNext, mPrevious, mArtwork;
    private TextView mTitle, mArtist;
    private Intent mIntent;
    private MediaService mMediaService;
    private final View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_fragment_play:
                    if (mMediaService.getPlayer() != null) {
                        if (mMediaService.isPlaying()) {
                            mMediaService.pauseMedia();
                            mPlay.setImageResource(R.drawable.ic_play);
                        } else {
                            mMediaService.resumeMedia();
                            mPlay.setImageResource(R.drawable.ic_pause);
                        }
                    }
                    break;
                case R.id.image_fragment_next:
                    mMediaService.nextSong();
                    break;
                case R.id.image_fragment_prev:
                    mMediaService.previousSong();
                    break;
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (mIntent == null) {
            mIntent = new Intent(getActivity(), MediaService.class);
            getActivity().bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(mIntent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);
        mPlay = (ImageView) v.findViewById(R.id.image_fragment_play);
        mNext = (ImageView) v.findViewById(R.id.image_fragment_next);
        mPrevious = (ImageView) v.findViewById(R.id.image_fragment_prev);
        mArtwork = (ImageView) v.findViewById(R.id.image_art);
        mTitle = (TextView) v.findViewById(R.id.text_title);
        mArtist = (TextView) v.findViewById(R.id.text_artist);
        mPlay.setOnClickListener(mListener);
        mNext.setOnClickListener(mListener);
        mPrevious.setOnClickListener(mListener);
        return v;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.MediaBinder binder = (MediaService.MediaBinder) service;
            mMediaService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void setUI() {
        mTitle.setText(mMediaService.getTitle());
        mArtist.setText(mMediaService.getArtist());
        if (mMediaService.getArtworkURL() != null) {
            Glide.with(this)
                .load(mMediaService.getArtworkURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_disc)
                .fitCenter()
                .into(mArtwork);
        } else mArtwork.setImageResource(R.drawable.ic_disc);
    }
}
