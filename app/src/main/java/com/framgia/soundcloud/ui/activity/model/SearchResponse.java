package com.framgia.soundcloud.ui.activity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by K on 5/12/2017.
 */

public class SearchResponse implements Parcelable{
    @SerializedName("collection")
    private List<Collection> mCollection;
    @SerializedName("total_results")
    private int mTotal;

    public List<Collection> getCollection() {
        return mCollection;
    }

    public void setCollection(
        List<Collection> collection) {
        mCollection = collection;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public class Collection {
        @SerializedName("kind")
        private String mKind;
        @SerializedName("id")
        private int mId;
        @SerializedName("title")
        private String mTitle;
        @SerializedName("artwork_url")
        private String mArtworkUrl;

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

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

        public String getArtworkUrl() {
            return mArtworkUrl;
        }

        public void setArtworkUrl(String artworkUrl) {
            mArtworkUrl = artworkUrl;
        }


    }

}
