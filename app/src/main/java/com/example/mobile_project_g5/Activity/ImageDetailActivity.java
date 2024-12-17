package com.example.mobile_project_g5.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mobile_project_g5.APIClient;
import com.example.mobile_project_g5.Component.ImageClass;
import com.example.mobile_project_g5.Helper.OnSwipeTouchListener;
import com.example.mobile_project_g5.R;
import com.example.mobile_project_g5.RemoveBgAPI;
import com.example.mobile_project_g5.Helper.SQLiteDataBase;
import com.example.mobile_project_g5.databinding.ImageSoloLayoutBinding;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private ImageView imageView;
    private Bitmap processedImage;
    String imagePath;
    final BitmapDrawable[] bitmapDrawable = new BitmapDrawable[1];
    final Bitmap[] bitmap = new Bitmap[1];

    private static final int SELECT_IMAGE = 1;
    private static final String API_KEY = "nQoMh52MRLDm6BZFvK6bQsPU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_solo_layout);


        imagePath = getIntent().getStringExtra("image_path");
        String type = getIntent().getStringExtra("type");

        SQLiteDataBase sql = new SQLiteDataBase(this);
        ImageClass image = sql.getImageByPath(imagePath);

        ImageButton settingsButton = findViewById(R.id.settings_button);
        ImageButton favoriteButton = findViewById(R.id.favorite_button);
        ImageButton deleteButton = findViewById(R.id.delete_button);
        ImageButton restoreButton = findViewById(R.id.restore_button);
        ImageButton shareButton = findViewById(R.id.share_button);
        ImageButton editButton = findViewById(R.id.edit_button);
        imageView = findViewById(R.id.imgSoloPhoto);
        ImageButton backButton = findViewById(R.id.back_btn);

        GlideImage(imagePath);

        // Set image favorite icon
        if(image.getIsFavorite() == 1){
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else{
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        // set event for favorite button
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite(image, favoriteButton, sql);
            }
        });

        // Set visibility for delete and restore button based on type
        if(type.equals("deleted")){
            restoreButton.setVisibility(View.VISIBLE);
        } else {
            restoreButton.setVisibility(View.GONE);
        }

        // Set visibility for edit button based on image activation
        if(image.getActivate().equals("0"))
        {
            editButton.setVisibility(View.GONE);
            restoreButton.setVisibility(View.VISIBLE);
            favoriteButton.setVisibility(View.GONE);
            shareButton.setVisibility(View.GONE);
        }
        else
        {
            editButton.setVisibility(View.VISIBLE);
            restoreButton.setVisibility(View.GONE);
            favoriteButton.setVisibility(View.VISIBLE);
            shareButton.setVisibility(View.VISIBLE);
        }

        // set event for restore button
        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreImage();
                finish();
            }
        });


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditImageActivity();
            }
        });


        // set event for delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
                finish();
            }
        });

        // set event for share button
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        // set event for share button
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImageHandler(bitmap[0]);
            }
        });

        // Set event for back button
        backButton.setOnClickListener(v -> finish());

        View currentView = findViewById(android.R.id.content);
        currentView.setOnTouchListener(new OnSwipeTouchListener(ImageDetailActivity.this) {
            @Override
            public void onSwipeRight() {
                imagePath = getPreImage(imagePath);
                GlideImage(imagePath);
            }

            @Override
            public void onSwipeLeft() {
                imagePath = getNextImage(imagePath);
                GlideImage(imagePath);
            }
        });

        sql.close();
    }

    private void GlideImage(String curImagePath) {
        // Load image from path for solo view
        if (!curImagePath.isEmpty()) {
            //Uri imageUri = Uri.parse(curImagePath);
            // Use Glide to load and display the image with fitCenter transformation
            Glide.with(this)
                    .load(imagePath)
                    .apply(new RequestOptions().fitCenter())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // Handle the error
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // Image is loaded, now you can get the bitmap
                            bitmapDrawable[0] = (BitmapDrawable) resource;
                            bitmap[0] = bitmapDrawable[0].getBitmap();
                            // Do something with the bitmap
                            return false;
                        }
                    })
                    .into(imageView);
        }
        else {
            String defaultPath = "android.resource://com.example.mobile_project_g5/drawable/so5";
            // Optionally set a default image
            Glide.with(this)
                    .load(Uri.parse(defaultPath))
                    .apply(new RequestOptions().fitCenter())
                    .into(imageView);
        }
    }

    private String getNextImage(String imagePath) {
        SQLiteDataBase db = new SQLiteDataBase(this);
        String albumID = getIntent().getStringExtra("album_id");
        ImageClass[] images = db.getImagesByAlbumId(albumID);
        int index = 0;
        for (int i = 0; i < images.length; i++) {
            if (images[i].getFilePath().equals(imagePath)) {
                index = i;
                break;
            }
        }
        String imagePathNext = index == images.length - 1 ? images[images.length - 1].getFilePath() : images[index + 1].getFilePath();
        db.close();
        return imagePathNext;
    }

    private String getPreImage(String imagePath) {
        SQLiteDataBase db = new SQLiteDataBase(this);
        String albumID = getIntent().getStringExtra("album_id");
        ImageClass[] images = db.getImagesByAlbumId(albumID);
        int index = 0;
        for (int i = 0; i < images.length; i++) {
            if (images[i].getFilePath().equals(imagePath)) {
                index = i;
                break;
            }
        }
        String imagePathPre = index == 0 ? images[0].getFilePath() : images[index - 1].getFilePath();
        db.close();
        return imagePathPre;
    }

    private void shareImageHandler(Bitmap bitmap) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri bmpUri;
        String textToShare = "Share image";
        bmpUri = saveImage(bitmap, getApplicationContext());
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Image");
        startActivity(Intent.createChooser(shareIntent, "Share image using"));
    }

    private static Uri saveImage(Bitmap bitmap, Context applicationContext) {
        File imageFolder = new File(applicationContext.getCacheDir(), "images");
        Uri uri = null;
        try {
            imageFolder.mkdirs();
            File file = new File(imageFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(applicationContext, "com.example.mobile_project_g5.fileprovider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void toggleFavorite(ImageClass image, ImageButton favoriteButton, SQLiteDataBase sql) {
        if(image.getIsFavorite() == 1){
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            image.setIsFavorite(0);
        }
        else{
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
            image.setIsFavorite(1);
        }
        sql.updateImage(image);
    }


    public static Intent newIntent(Context context, String imagePath, String imgInfo, String type, String albumID) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra("image_path", imagePath);
        intent.putExtra("image_info", imgInfo);
        intent.putExtra("album_id", albumID);
        intent.putExtra("type", type);
        return intent;
    }

    private void deleteImage() {
        String imagePath = getIntent().getStringExtra("image_path");
        SQLiteDataBase dbHelper = new SQLiteDataBase(this);
        ImageClass image =  dbHelper.getImageByPath(imagePath);

        // Xóa ảnh khỏi cơ sở dữ liệu
        if(image.getActivate().equals("1")){
            dbHelper.deleteImage(imagePath);
        }
        else{
            dbHelper.removeImage(imagePath);
        }
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
                if(item.getItemId() == R.id.remove_bg){
                    removeBackground();
                    return true;
                }
                if(item.getItemId() == R.id.text_recognition){
                    if (imagePath != null && !imagePath.isEmpty()) {
                        Intent intent = new Intent(ImageDetailActivity.this, TextRecognitionActivity.class);
                        intent.putExtra("imagePath", imagePath);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ImageDetailActivity.this, "Image path is invalid!", Toast.LENGTH_SHORT).show();
                    }
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

    private void openEditImageActivity() {
        String imagePath = getIntent().getStringExtra("image_path");

        Intent intent = new Intent(this, EditImageActivity.class);
        intent.putExtra("image_path", imagePath);
        startActivity(intent);
    }

}
