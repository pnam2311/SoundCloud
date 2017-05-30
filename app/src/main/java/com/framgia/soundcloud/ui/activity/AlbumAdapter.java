package com.framgia.soundcloud.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.k.soundcloud.R;
import com.framgia.soundcloud.ui.fragment.SongListFragment;

import java.util.ArrayList;
import java.util.List;

import static com.framgia.soundcloud.ui.activity.Const.ID_KEY;
import static com.framgia.soundcloud.ui.activity.Const.KIND_KEY;
import static com.framgia.soundcloud.ui.fragment.SongListFragment.newInstance;

/**
 * Created by K on 5/10/2017.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<AlbumItem> mAlbums;
    private Context mContext;

    public void setAlbums(List<AlbumItem> albums) {
        mAlbums = albums;
    }

    public AlbumAdapter(ArrayList<AlbumItem> albumItems) {
        mAlbums = albumItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        AlbumItem item = mAlbums.get(i);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public AlbumItem getItem(int i) {
        return mAlbums.get(i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text_album_title);
            mImageView = (ImageView) itemView.findViewById(R.id.image_album_art);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = getItem(getAdapterPosition()).getId();
                    String kind = getItem(getAdapterPosition()).getKind();
                    Fragment fragment = newInstance(id, kind);
                    ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                }
            });
        }

        public void bindData(AlbumItem item) {
            if (item != null) {
                mTextView.setText(item.getTitle());
                Glide.with(mContext)
                    .load(item.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(mImageView);
            }
        }
    }
}