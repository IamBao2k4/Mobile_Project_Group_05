package com.example.mobile_project_g5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailActivity extends AppCompatActivity {
    static final int REQUEST_CODE_DELETE_IMAGE = 1;
    private static final String EXTRA_ALBUM_NAME = "album_name";
    private static final String EXTRA_ALBUM_ID = "album_id";
    private static ImageClass[] images = new ImageClass[0];

    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        String albumName = getIntent().getStringExtra(EXTRA_ALBUM_NAME);
        String albumID = getIntent().getStringExtra(EXTRA_ALBUM_ID);
        TextView albumNameTextView = findViewById(R.id.album_name);
        albumNameTextView.setText(albumName);

        SQLiteDataBase dbHelper = new SQLiteDataBase(this);
        images = dbHelper.getImagesByAlbumId(albumID);

        GridView gridViewImages = findViewById(R.id.grid_view_images);
        imageAdapter = new ImageAdapter(this, images);
        gridViewImages.setAdapter(imageAdapter);
    }


    public static Intent newIntent(Context context, AlbumClass cur_album) {
        Intent intent = new Intent(context, AlbumDetailActivity.class);
        intent.putExtra(EXTRA_ALBUM_NAME, cur_album.getAlbumName());
        intent.putExtra(EXTRA_ALBUM_ID, cur_album.getAlbumID());
        images = cur_album.getImages();
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String deletedImagePath = data.getStringExtra("deleted_image_path");

            if (deletedImagePath != null) {
                imageAdapter.removeImage(deletedImagePath);
            }
        }
    }
}

