package com.example.mobile_project_g5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailActivity extends AppCompatActivity {
    private static final String EXTRA_ALBUM_NAME = "album_name";
    private static final String EXTRA_ALBUM_ID = "album_id";
    private static ImageClass[] images = new ImageClass[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        String albumName = getIntent().getStringExtra(EXTRA_ALBUM_NAME);
        String albumID = getIntent().getStringExtra(EXTRA_ALBUM_ID);
        TextView albumNameTextView = findViewById(R.id.album_name);
        albumNameTextView.setText(albumName);

        GridView gridViewImages = findViewById(R.id.grid_view_images);
        ImageAdapter imageAdapter = new ImageAdapter(this, images); // Bạn cần tạo ImageAdapter
        gridViewImages.setAdapter(imageAdapter);
    }


    public static Intent newIntent(Context context, AlbumClass cur_album) {
        Intent intent = new Intent(context, AlbumDetailActivity.class);
        intent.putExtra(EXTRA_ALBUM_NAME, cur_album.getAlbumName());
        intent.putExtra(EXTRA_ALBUM_ID, cur_album.getAlbumID());
        images = cur_album.getImages();
        return intent;
    }
}

