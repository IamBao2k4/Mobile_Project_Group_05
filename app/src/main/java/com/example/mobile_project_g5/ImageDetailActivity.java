package com.example.mobile_project_g5;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.mobile_project_g5.databinding.ImageSoloLayoutBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ImageDetailActivity extends AppCompatActivity {
    private ImageSoloLayoutBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_solo_layout);

        int imageId = getIntent().getIntExtra("image_id", -1);

        ImageView imageView = findViewById(R.id.imgSoloPhoto);
        if (imageId != -1) {
            imageView.setImageResource(imageId);
        } else {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
            // Optionally set a default image
            imageView.setImageResource(R.drawable.toys);
        }

        Button backButton = findViewById(R.id.btnSoloBack);
        backButton.setOnClickListener(v -> finish());

        // Gán sự kiện cho nút Settings
        ImageButton settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
    }

    public static Intent newIntent(Context context, int imageId) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra("image_id", imageId);
        return intent;
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
        int imageId = getIntent().getIntExtra("image_id", -1);
        if (imageId != -1) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
            try {
                wallpaperManager.setBitmap(bitmap);
                Toast.makeText(this, "Wallpaper set successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to set wallpaper", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
        }
    }

}
