package com.example.mobile_project_g5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailActivity extends AppCompatActivity {
    static final int REQUEST_CODE_DELETE_IMAGE = 1;
    private static final String EXTRA_ALBUM_NAME = "album_name";
    private static final String EXTRA_ALBUM_ID = "album_id";
    private static ImageClass[] images = new ImageClass[0];
    public static AlbumClass curAlbum;

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
        ImageAdapter imageAdapter = new ImageAdapter(this, images,""); // Bạn cần tạo ImageAdapter
        gridViewImages.setAdapter(imageAdapter);
        Button editBtn = findViewById(R.id.edit_btn);
        ImageButton addBtn = findViewById(R.id.add_btn);
        Button backBtn = findViewById(R.id.back_btn);

        //Nhấn nút edit sẽ hiển thị nút xóa trên từng ảnh để chọn và xóa
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAdapter.setEditMode();
            }
        });

        // nút thêm và ở activity để chọn ảnh thêm
        addBtn.setOnClickListener(v -> {
            Intent intent = AddImageActivity.newIntent(AlbumDetailActivity.this, curAlbum);
            AlbumDetailActivity.this.startActivity(intent);
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }


    public static Intent newIntent(Context context, AlbumClass cur_album) {
        Intent intent = new Intent(context, AlbumDetailActivity.class);
        intent.putExtra(EXTRA_ALBUM_NAME, cur_album.getAlbumName());
        intent.putExtra(EXTRA_ALBUM_ID, cur_album.getAlbumID());
        images = cur_album.getImages();
        curAlbum = cur_album;
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String deletedImagePath = data.getStringExtra("deleted_image_path");
            String restoredImagePath = data.getStringExtra("restored_image_path");

            if (deletedImagePath != null) {
                imageAdapter.removeImage(deletedImagePath);
            }
            if (restoredImagePath != null) {
                imageAdapter.addImage(restoredImagePath);
            }
        }
    }
}

