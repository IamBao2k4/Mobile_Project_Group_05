package com.example.mobile_project_g5.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project_g5.Component.AlbumClass;
import com.example.mobile_project_g5.Adapter.ImageAdapter;
import com.example.mobile_project_g5.Component.ImageClass;
import com.example.mobile_project_g5.Helper.SQLiteDataBase;
import com.example.mobile_project_g5.R;

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
        Button submit = findViewById(R.id.submit_btn);
        RecyclerView gridViewImages = findViewById(R.id.grid_view_images);
        ImageAdapter imageAdapter = new ImageAdapter(this, images, "add"); // Bạn cần tạo ImageAdapter
        gridViewImages.setLayoutManager(new GridLayoutManager(this, 2));
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

        ImageButton back = findViewById(R.id.back_btn);
        back.setOnClickListener(view -> {
            finish();
        });
    }

    public static Intent newIntent(Context context, AlbumClass cur_album) {
        SQLiteDataBase sql = new SQLiteDataBase(context);
        Intent intent = new Intent(context, AddImageActivity.class);
        intent.putExtra(EXTRA_ALBUM_NAME, cur_album.getAlbumName());
        intent.putExtra(EXTRA_ALBUM_ID, cur_album.getAlbumID());
        images = sql.getAllImages();
        curAlbum = cur_album;
        return intent;
    }
}
