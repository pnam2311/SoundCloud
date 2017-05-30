package com.framgia.soundcloud.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.k.soundcloud.R;
import com.framgia.soundcloud.ui.activity.PlayerActivity;
import com.framgia.soundcloud.ui.activity.SongItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.framgia.soundcloud.ui.activity.Const.CLIENT_ID;
import static com.framgia.soundcloud.ui.activity.Const.LIST_KEY;
import static com.framgia.soundcloud.ui.activity.Const.NEXT_SONG;
import static com.framgia.soundcloud.ui.activity.Const.PLAY_SONG;
import static com.framgia.soundcloud.ui.activity.Const.PREVIOUS_SONG;
import static com.framgia.soundcloud.ui.activity.Const.UPDATE_UI;

public class MediaService extends Service implements MediaPlayer.OnErrorListener, MediaPlayer
    .OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener {
    private static final String TAG = "MediaService";
    private ArrayList<SongItem> mSongItems;
    private MediaPlayer mPlayer;
    private int mCurPos;
    private IBinder mMediaBind = new MediaBinder();
    private int mStatus = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public class MediaBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMediaBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        mPlayer.release();
        return false;
    }

    public void setList(ArrayList<SongItem> songs) {
        mSongItems = new ArrayList<>();
        mSongItems = songs;
    }

    public void initMediaPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnSeekCompleteListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (mSongItems.get(mCurPos).isStreamable() == true) {
            try {
                mPlayer
                    .setDataSource(mSongItems.get(mCurPos).getStreamUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
            Toast.makeText(this, "Preparing... Do not click (bug)", Toast.LENGTH_SHORT).show();
        } else nextSong();
    }

    public void nextSong() {
        if (mPlayer != null && mCurPos < mSongItems.size() - 1) {
            mCurPos++;
            stopMedia();
            mPlayer.release();
            initMediaPlayer();
        }
    }

    public void previousSong() {
        if (mPlayer != null && mCurPos > 0) {
            mCurPos--;
            stopMedia();
            mPlayer.release();
            initMediaPlayer();
        }
    }

    public int getCurPos() {
        return mCurPos;
    }

    public void playMedia() {
        if (!mPlayer.isPlaying()) mPlayer.start();
    }

    public void stopMedia() {
        if (mPlayer == null) return;
        if (mPlayer.isPlaying()) mPlayer.stop();
    }

    public void pauseMedia() {
        if (mPlayer == null) return;
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mCurPos = mPlayer.getCurrentPosition();
        }
    }

    public void resumeMedia() {
        mPlayer.seekTo(mPlayer.getCurrentPosition());
        mPlayer.start();
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mSongItems.get(mCurPos).getDuration();
    }

    public String getArtworkURL() {
        return mSongItems.get(mCurPos).getUrl();
    }

    public void onDestroy() {
        if (mPlayer.isPlaying()) mPlayer.stop();
        mPlayer.release();
        stopSelf();
    }

    public String getTitle() {
        return mSongItems.get(mCurPos).getSongTitle();
    }

    public String getArtist() {
        return mSongItems.get(mCurPos).getUsername();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mPlayer != null) {
            if (mCurPos < mSongItems.size())
                nextSong();
            else {
                stopMedia();
            }
        }
    }

    public void setCurPos(int pos) {
        mCurPos = pos;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mPlayer.release();
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onPrepared(MediaPlayer mp) {
        playMedia();
        Intent notIntent = new Intent(this, PlayerActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
            notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(pendInt)
            .setSmallIcon(R.drawable.ic_cloud)
            .setTicker(mSongItems.get(mCurPos).getSongTitle())
            .setOngoing(true)
            .setContentTitle("Playing")
            .setContentText(mSongItems.get(mCurPos).getSongTitle());
        Notification not = builder.build();
        startForeground(1, not);
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        nextSong();
    }

    public MediaPlayer getPlayer() {
        return mPlayer;
    }
}
