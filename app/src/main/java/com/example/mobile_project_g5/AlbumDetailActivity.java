package com.example.mobile_project_g5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlbumDetailActivity extends AppCompatActivity {
    private static final String EXTRA_ALBUM_NAME = "album_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        String albumName = getIntent().getStringExtra(EXTRA_ALBUM_NAME);
        TextView albumNameTextView = findViewById(R.id.album_name);
        albumNameTextView.setText(albumName);

        int[] images = {
                R.drawable.toys,
                R.drawable.album_icon,
                R.drawable.picture,
                // Thêm các ID drawable khác
        };


        GridView gridViewImages = findViewById(R.id.grid_view_images);
        ImageAdapter imageAdapter = new ImageAdapter(this, images); // Bạn cần tạo ImageAdapter
        gridViewImages.setAdapter(imageAdapter);
    }


    public static Intent newIntent(Context context, String albumName) {
        Intent intent = new Intent(context, AlbumDetailActivity.class);
        intent.putExtra(EXTRA_ALBUM_NAME, albumName);
        return intent;
    }
}

