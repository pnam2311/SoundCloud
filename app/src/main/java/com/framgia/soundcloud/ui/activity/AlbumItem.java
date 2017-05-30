package com.framgia.soundcloud.ui.activity;

import com.framgia.soundcloud.ui.activity.model.AlbumModel;
import com.framgia.soundcloud.ui.activity.model.SearchResponse;

/**
 * Created by K on 5/10/2017.
 */
public class AlbumItem {
    private String mTitle;
    private String mUrl;
    private String mKind;
    private int mId;

    public AlbumItem(SearchResponse model, int i) {
        mTitle = model.getCollection().get(i).getTitle();
        mUrl = model.getCollection().get(i).getArtworkUrl();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getKind() {
        return mKind;
    }

    public void setKind(String kind) {
        mKind = kind;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
