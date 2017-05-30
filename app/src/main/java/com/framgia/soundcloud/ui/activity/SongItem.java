package com.framgia.soundcloud.ui.activity;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.soundcloud.ui.activity.model.SongModel;

import java.io.Serializable;

import static com.framgia.soundcloud.ui.activity.Const.CLIENT_ID;

/**
 * Created by ducpm on 16/05/17.
 */
public class SongItem implements Parcelable {
    private String mUrl;
    private String mSongTitle;
    private String mUsername;
    private String mStreamUrl;
    private boolean mStreamable;
    private int mDuration;

    public SongItem(SongModel model){
        mUrl = model.getArtworkUrl();
        mStreamUrl = model.getStreamUrl() + "?client_id=" + CLIENT_ID;
        mSongTitle = model.getTitle();
        mUsername = model.getUser().getUsername();
        mDuration = model.getDuration();
        mStreamable = model.isStreamable();
    }

    protected SongItem(Parcel in) {
        mUrl = in.readString();
        mSongTitle = in.readString();
        mUsername = in.readString();
        mStreamUrl = in.readString();
        mStreamable = in.readByte() != 0;
        mDuration = in.readInt();
    }

    public static final Creator<SongItem> CREATOR = new Creator<SongItem>() {
        @Override
        public SongItem createFromParcel(Parcel in) {
            return new SongItem(in);
        }

        @Override
        public SongItem[] newArray(int size) {
            return new SongItem[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getSongTitle() {
        return mSongTitle;
    }

    public void setSongTitle(String songTitle) {
        mSongTitle = songTitle;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getStreamUrl() {
        return mStreamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        mStreamUrl = streamUrl;
    }

    public boolean isStreamable() {
        return mStreamable;
    }

    public void setStreamable(boolean streamable) {
        mStreamable = streamable;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUrl);
        dest.writeString(mSongTitle);
        dest.writeString(mUsername);
        dest.writeString(mStreamUrl);
        dest.writeByte((byte) (mStreamable ? 1 : 0));
        dest.writeInt(mDuration);
    }
}