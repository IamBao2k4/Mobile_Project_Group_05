package com.example.mobile_project_g5;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageDetailActivity extends AppCompatActivity {
    private ImageSoloLayoutBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ImageClass[] images;
    private ImageView imageView;
    private Bitmap processedImage;

    private static final int SELECT_IMAGE = 1;
    private static final String API_KEY = "nQoMh52MRLDm6BZFvK6bQsPU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_solo_layout);


        String imagePath = getIntent().getStringExtra("image_path");

        imageView = findViewById(R.id.imgSoloPhoto);
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

    public static Intent newIntent(Context context, String imagePath, String imgInfo, String albumID) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra("image_path", imagePath);
        intent.putExtra("image_info", imgInfo);
        intent.putExtra("album_id", albumID);
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
                if(item.getItemId() == R.id.remove_bg){
                    removeBackground();
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

    private void removeBackground() {
        Bitmap bitmap = ((android.graphics.drawable.BitmapDrawable) imageView.getDrawable()).getBitmap();
        Toast.makeText(this, "Removing background...", Toast.LENGTH_SHORT).show();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), byteArrayOutputStream.toByteArray());
        MultipartBody.Part body = MultipartBody.Part.createFormData("image_file", "image.png", requestFile);

        RemoveBgAPI removeBgAPI = APIClient.getClient().create(RemoveBgAPI.class);
        Call<ResponseBody> call= removeBgAPI.removeBackground(
                API_KEY,body
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try{
                        byte[] imageBytes = response.body().bytes();
                        processedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        imageView.setImageBitmap(processedImage);
                        Toast.makeText(ImageDetailActivity.this, "Background removed!", Toast.LENGTH_SHORT).show();

                        // Lưu ảnh đã xóa nền vào bộ nhớ
                        saveImageToGallery(processedImage);



                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(ImageDetailActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ImageDetailActivity.this, "Fail to remove backgorund", Toast.LENGTH_SHORT).show();
                Log.e("RemoveBg", "Error: " + t.getMessage());
            }
        });
    }

    // Hàm lưu ảnh đã xử lý vào bộ nhớ
    private void saveImageToGallery(Bitmap bitmap) {
        OutputStream outputStream;
        String imagePath = "";
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, "bg_removed_" + System.currentTimeMillis() + ".png");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Remove BG Images");

                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                outputStream = getContentResolver().openOutputStream(uri);
                imagePath = uri.toString();

            }
            else{
                File storagDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Remove Bg Images");

                if(!storagDir.exists()){
                    storagDir.mkdirs();
                }

                String fileName = "bg_removed_" + + System.currentTimeMillis() + ".png";
                File imageFile = new File(storagDir, fileName);
                outputStream = new FileOutputStream(imageFile);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(imageFile);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            String albumID = getIntent().getStringExtra("album_id");

            SQLiteDataBase dbHelper = new SQLiteDataBase(this);
            dbHelper.addImage(albumID, imagePath, "remove bg");

            Toast.makeText(ImageDetailActivity.this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e){
            e.printStackTrace();
            Toast.makeText(ImageDetailActivity.this, "Fail to save image", Toast.LENGTH_SHORT).show();
        }
    }

}
