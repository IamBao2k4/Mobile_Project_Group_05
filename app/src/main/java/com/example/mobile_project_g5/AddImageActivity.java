package com.example.mobile_project_g5;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddImageActivity  extends AppCompatActivity {
    private static ImageClass[] images;
    private static final String EXTRA_ALBUM_NAME = "album_name";
    private static final String EXTRA_ALBUM_ID = "album_id";
    private static AlbumClass curAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_image_layout);
        String albumName = getIntent().getStringExtra(EXTRA_ALBUM_NAME);
        String albumID = getIntent().getStringExtra(EXTRA_ALBUM_ID);
        Button submit = findViewById(R.id.submit_btn);
        GridView gridViewImages = findViewById(R.id.grid_view_images);
        ImageAdapter imageAdapter = new ImageAdapter(this, images, "add"); // Bạn cần tạo ImageAdapter
        gridViewImages.setAdapter(imageAdapter);

        submit.setOnClickListener(view -> {
            List<ImageClass> chosen;
            chosen = imageAdapter.getImages_chosen();
            chosen.addAll(Arrays.asList(curAlbum.getImages()));
            curAlbum.setImages(chosen.toArray(new ImageClass[0]));
            SQLiteDataBase db = new SQLiteDataBase(this);
            if(db.updateImageInAlbum(curAlbum.getAlbumID(), curAlbum.getImages()))
                Toast.makeText(this, "Đã thêm ảnh vào album", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this, "Thêm ảnh thất bại", Toast.LENGTH_SHORT).show();
            }
            db.close();
            finish();
        });
    }

    public static Intent newIntent(Context context, AlbumClass cur_album) {
        SQLiteDataBase sql = new SQLiteDataBase(context);
        Intent intent = new Intent(context, AddImageActivity.class);
        intent.putExtra(EXTRA_ALBUM_NAME, cur_album.getAlbumName());
        intent.putExtra(EXTRA_ALBUM_ID, cur_album.getAlbumID());
        images = sql.getImagebyAlbumId("0");
        curAlbum = cur_album;
        return intent;
    }
}
