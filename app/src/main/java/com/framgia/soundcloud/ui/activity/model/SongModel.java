package com.framgia.soundcloud.ui.activity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ducpm on 16/05/17.
 */
public class SongModel{
    @SerializedName("genre")
    private String mGenre;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("id")
    private int mId;
    @SerializedName("artwork_url")
    private String mArtworkUrl;
    @SerializedName("user")
    private User mUser;
    @SerializedName("duration")
    private int mDuration;
    @SerializedName("stream_url")
    private String mStreamUrl;
    @SerializedName("streamable")
    private boolean mStreamable;

    public boolean isStreamable() {
        return mStreamable;
    }

    public void setStreamable(boolean streamable) {
        mStreamable = streamable;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public String getStreamUrl() {
        return mStreamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        mStreamUrl = streamUrl;
    }

    public class User {
        // de search user (for later)
        @SerializedName("username")
        private String mUsername;
        @SerializedName("id")
        private String mId;
        @SerializedName("avatar_url")
        private String mUserAvatarUrl;

        public String getUsername() {
            return mUsername;
        }

        public void setUsername(String username) {
            mUsername = username;
        }

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getUserAvatarUrl() {
            return mUserAvatarUrl;
        }

        public void setUserAvatarUrl(String userAvatarUrl) {
            mUserAvatarUrl = userAvatarUrl;
        }
    }
}
