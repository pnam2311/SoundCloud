package com.framgia.soundcloud.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.framgia.soundcloud.ui.activity.Const.INT_KEY;
import static com.framgia.soundcloud.ui.activity.Const.LIST_KEY;

/**
 * Created by K on 5/18/2017.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private ArrayList<SongItem> mSongs;
    private Context mContext;

    public SongAdapter(ArrayList<SongItem> songs) {
        mSongs = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        SongItem item = mSongs.get(i);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mAlbumArt;
        TextView mTitle;
        TextView mArtist;
        TextView mDuration;

        public ViewHolder(View itemView) {
            super(itemView);
            mAlbumArt = (ImageView) itemView.findViewById(R.id.image_song_art);
            mTitle = (TextView) itemView.findViewById(R.id.text_song_title);
            mArtist = (TextView) itemView.findViewById(R.id.text_song_artist);
            mDuration = (TextView) itemView.findViewById(R.id.text_song_duration);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSongs != null) {
                        int m = getAdapterPosition();
                        Intent activity = new Intent(mContext, PlayerActivity.class);
                        activity.putExtra(INT_KEY, m);
                        activity.putParcelableArrayListExtra(LIST_KEY, mSongs);
                        mContext.startActivity(activity);
                    }
                }
            });
        }

        public void bindData(SongItem item) {
            if (item != null) {
                mTitle.setText(item.getSongTitle());
                mArtist.setText(item.getUsername());
                mDuration.setText(duration(item.getDuration()));
                Glide.with(mContext)
                    .load(item.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_disc)
                    .into(mAlbumArt);
            }
        }

        public String duration(int millis) {
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            return hms;
        }
    }
}
