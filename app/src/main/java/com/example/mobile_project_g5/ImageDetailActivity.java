package com.example.mobile_project_g5;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mobile_project_g5.databinding.ImageSoloLayoutBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ImageDetailActivity extends AppCompatActivity {
    private ImageSoloLayoutBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ImageClass[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_solo_layout);


        String imagePath = getIntent().getStringExtra("image_path");

        ImageView imageView = findViewById(R.id.imgSoloPhoto);
        if (!imagePath.isEmpty()) {
            Uri imageUri = Uri.parse(imagePath);
            // Sử dụng thư viện Glide để tải và hiển thị hình ảnh
            Glide.with(this)
                    .load(imageUri)
                    .into(imageView);// Sử dụng setImageResource với ID
        } else {
            String defaultPath = "android.resource://com.example.mobile_project_g5/drawable/so5";
            // Optionally set a default image
            Glide.with(this)
                    .load(Uri.parse(defaultPath))
                    .into(imageView);// Sử dụng setImageResource với ID
        }

        Button backButton = findViewById(R.id.btnSoloBack);
        backButton.setOnClickListener(v -> finish());

        // Gán sự kiện cho nút Delete
        ImageButton deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
            }
        });

        // Gán sự kiện cho nút Settings
        ImageButton settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
    }

    public static Intent newIntent(Context context, String imagePath) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra("image_path", imagePath);
        return intent;
    }

    private void deleteImage() {
        String imagePath = getIntent().getStringExtra("image_path");

        // Xóa ảnh khỏi cơ sở dữ liệu
        SQLiteDataBase dbHelper = new SQLiteDataBase(this);
        dbHelper.deleteImage(imagePath);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("deleted_image_path", imagePath);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void showPopupMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.setting_menu_solo, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.used_as_wallpaper) {
                    setAsWallpaper();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void setAsWallpaper() {
        String imagePath = getIntent().getStringExtra("image_path");
        if (imagePath != null && !imagePath.isEmpty())
        {
            try
            {
                Uri imageUri = Uri.parse(imagePath);
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                wallpaperManager.setBitmap(bitmap);
                Toast.makeText(this, "Wallpaper set successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e)
            {
                Toast.makeText(this, "Failed to set wallpaper", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else
        {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
        }
    }

}
