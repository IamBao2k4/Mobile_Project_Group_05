package com.example.mobile_project_g5;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.mobile_project_g5.databinding.ImageSoloLayoutBinding;
import com.bumptech.glide.request.RequestOptions;
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
        String type = getIntent().getStringExtra("type");

        ImageView imageView = findViewById(R.id.imgSoloPhoto);
        if (!imagePath.isEmpty()) {
            Uri imageUri = Uri.parse(imagePath);
            // Use Glide to load and display the image with fitCenter transformation
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions().fitCenter())
                    .into(imageView);
        } else {
            String defaultPath = "android.resource://com.example.mobile_project_g5/drawable/so5";
            // Optionally set a default image
            Glide.with(this)
                    .load(Uri.parse(defaultPath))
                    .apply(new RequestOptions().fitCenter())
                    .into(imageView);
        }

        ImageButton deleteButton = findViewById(R.id.delete_button);
        ImageButton restoreButton = findViewById(R.id.restore_button);

        if(type.equals("deleted")){
            deleteButton.setVisibility(View.GONE);
            restoreButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.VISIBLE);
            restoreButton.setVisibility(View.GONE);
        }

        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreImage();
                finish();
            }
        });

        Button backButton = findViewById(R.id.btnSoloBack);
        backButton.setOnClickListener(v -> finish());

        // Gán sự kiện cho nút Delete

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
                finish();
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

    public static Intent newIntent(Context context, String imagePath, String imgInfo, String type) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra("image_path", imagePath);
        intent.putExtra("image_info", imgInfo);
        intent.putExtra("type", type);
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
    }

    private void restoreImage() {
        String imagePath = getIntent().getStringExtra("image_path");

        // Khôi phục ảnh đã xóa
        SQLiteDataBase dbHelper = new SQLiteDataBase(this);
        dbHelper.restoreImage(imagePath);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("restored_image_path", imagePath);
        setResult(RESULT_OK, resultIntent);
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
                if(item.getItemId() == R.id.information){
                    showImageInfoDialog();
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

    private void showImageInfoDialog() {
        String imgInfo = getIntent().getStringExtra("image_info");

        // Tao AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông tin ảnh");

        // Hien thong tin anh
        if (imgInfo != null && !imgInfo.isEmpty()) {
            builder.setMessage(imgInfo);
        } else {
            builder.setMessage("Không có thông tin chi tiết.");
        }

        // Nut OK dong dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();  // Đóng dialog khi nhấn "OK"
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
