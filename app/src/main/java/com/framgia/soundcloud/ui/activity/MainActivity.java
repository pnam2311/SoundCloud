package com.framgia.soundcloud.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.k.soundcloud.R;
import com.framgia.soundcloud.ui.MediaService;
import com.framgia.soundcloud.ui.fragment.AlbumListFragment;

import static com.framgia.soundcloud.ui.activity.Const.PLAY_SONG;

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlbumListFragment fragment = new AlbumListFragment();
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit();
    }
}


