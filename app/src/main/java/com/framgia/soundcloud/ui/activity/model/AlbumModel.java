package com.framgia.soundcloud.ui.activity.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by K on 5/21/2017.
 */
public class AlbumModel {
    @SerializedName("tracks")
    private List<SongModel> mPlaylist;
    @SerializedName("playlist_type")
    private String mPlaylistType;

    public List<SongModel> getPlaylist() {
        return mPlaylist;
    }

    public void setPlaylist(List<SongModel> playlist) {
        mPlaylist = playlist;
    }

    public String getPlaylistType() {
        return mPlaylistType;
    }

    public void setPlaylistType(String playlistType) {
        mPlaylistType = playlistType;
    }
}
