package com.framgia.soundcloud.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.k.soundcloud.R;
import com.framgia.soundcloud.ui.MediaService;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.framgia.soundcloud.ui.activity.Const.INT_KEY;
import static com.framgia.soundcloud.ui.activity.Const.LIST_KEY;
import static com.framgia.soundcloud.ui.activity.Const.NEXT_SONG;
import static com.framgia.soundcloud.ui.activity.Const.PLAY_SONG;
import static com.framgia.soundcloud.ui.activity.Const.PREVIOUS_SONG;
import static com.framgia.soundcloud.ui.activity.Const.UPDATE_UI;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mPlayButton, mNextButton, mPreviousButton, mArtwork;
    private TextView mTitle, mArtist;
    private ArrayList<SongItem> mSongItems = new ArrayList<>();
    private MediaService mMediaService;
    private Intent mIntent;
    private int mCurPos;
    private SeekBar mSeekBar;
    private TextView mStartTimeText;
    private TextView mEndTimeText;
    private double mStartTime = 0;
    private double mEndTime = 0;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mPlayButton = (ImageView) findViewById(R.id.image_player_play);
        mNextButton = (ImageView) findViewById(R.id.image_player_next);
        mPreviousButton = (ImageView) findViewById(R.id.image_player_prev);
        mArtwork = (ImageView) findViewById(R.id.song_artwork);
        mTitle = (TextView) findViewById(R.id.text_player_title);
        mArtist = (TextView) findViewById(R.id.text_player_artist);
        mStartTimeText = (TextView) findViewById(R.id.text_start_timer);
        mEndTimeText = (TextView) findViewById(R.id.text_end_timer);
        mPlayButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mSongItems = getIntent().getParcelableArrayListExtra(LIST_KEY);
        mCurPos = getIntent().getIntExtra(INT_KEY, -1);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mMediaService.getPlayer() != null && fromUser) {
                    mMediaService.getPlayer().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mIntent == null) {
            mIntent = new Intent(this, MediaService.class);
            bindService(mIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            startService(mIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.MediaBinder binder = (MediaService.MediaBinder) service;
            mMediaService = binder.getService();
            if (mMediaService.getPlayer() == null) {
                mMediaService.setList(mSongItems);
                mMediaService.setCurPos(mCurPos);
                mMediaService.initMediaPlayer();
                setUI();
                mPlayButton.setImageResource(R.drawable.ic_pause);
            } else if (mCurPos != -1) {
                mMediaService.stopMedia();
                mMediaService.getPlayer().release();
                mMediaService.setList(mSongItems);
                mMediaService.setCurPos(mCurPos);
                mMediaService.initMediaPlayer();
                setUI();
                mPlayButton.setImageResource(R.drawable.ic_pause);
            } else {
                setUI();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            unbindService(mServiceConnection);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_player_play:
//                if (mMediaService.getPlayer() != null) {
                    if (!mMediaService.isPlaying()) {
                        mMediaService.resumeMedia();
                        mPlayButton.setImageResource(R.drawable.ic_pause);
                    } else if (mMediaService.isPlaying()) {
                        mMediaService.pauseMedia();
                        mPlayButton.setImageResource(R.drawable.ic_play);
                    }
//                }
                break;
            case R.id.image_player_next:
                mMediaService.nextSong();
                mCurPos = mMediaService.getCurPos();
                setUI();
                break;
            case R.id.image_player_prev:
                mMediaService.previousSong();
                mCurPos = mMediaService.getCurPos();
                setUI();
                break;
        }
    }

    public void setUI() {
        if (mMediaService.isPlaying()) mPlayButton.setImageResource(R.drawable.ic_pause);
        mEndTime = mMediaService.getDuration();
        mSeekBar.setMax((int) mEndTime);
        mSeekBar.setProgress((int) mStartTime);
        mHandler.postDelayed(UpdateSongTime, 100);
        mStartTime = mMediaService.getCurrentPosition();
        mTitle.setText(mMediaService.getTitle());
        mArtist.setText(mMediaService.getArtist());
        mStartTimeText.setText(String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes((long) mStartTime),
            TimeUnit.MILLISECONDS.toSeconds((long) mStartTime) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                    mStartTime))));
        mEndTimeText.setText(String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes((long) mEndTime),
            TimeUnit.MILLISECONDS.toSeconds((long) mEndTime) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                    mEndTime))));
        if (mMediaService.getArtworkURL() != null) {
            mMediaService.getArtworkURL().replace("large", "t500x500");
            Glide.with(this)
                .load(mMediaService.getArtworkURL().replace("large", "t500x500"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_disc)
                .fitCenter()
                .into(mArtwork);
        } else mArtwork.setImageResource(R.drawable.ic_disc);
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            mStartTime = mMediaService.getPlayer().getCurrentPosition();
            mStartTimeText.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) mStartTime),
                TimeUnit.MILLISECONDS.toSeconds((long) mStartTime) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                        toMinutes((long) mStartTime)))
            );
            mSeekBar.setProgress((int) mStartTime);
            mHandler.postDelayed(this, 100);
        }
    };
}