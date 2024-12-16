package com.example.mobile_project_g5.Helper;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.mobile_project_g5.Component.ImageClass;
import com.example.mobile_project_g5.Helper.SQLiteDataBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
public class ReadMediaFromExternalStorage{
    private final Context context;
    public ReadMediaFromExternalStorage(Context context) {
        this.context = context;
    }

    public List<ImageClass> loadMediaData() {
        List<ImageClass> mediaList = new ArrayList<>();
        SQLiteDataBase db = new SQLiteDataBase(context);
        // Uri cho ảnh và video
        Uri[] mediaUris = {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        };

        String[] projection = {
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.DATA,
        };

        for (Uri uri : mediaUris) {
            try (Cursor cursor = context.getContentResolver().query(
                    uri,
                    projection,
                    null,
                    null
            )) {
                if (cursor != null && cursor.moveToFirst()) {
                    Log.d("MediaCursor", "Cursor count: " + cursor.getCount());

                    int idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
                    int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                    int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED);
                    int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    int mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE);

                    // Di chuyển con trỏ đến dòng đầu tiên nếu có dữ liệu


                    do {
                        ImageClass image = new ImageClass();
                        image.setAlbumID("0");
                        image.setInformation(cursor.getString(nameColumn));
                        String date = cursor.getString(dateColumn);
                        long timestamp = Long.parseLong(date);
                        Date newdate = new Date(timestamp * 1000);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String formattedDate = sdf.format(newdate);
                        image.setExifDatetime(formattedDate);
                        image.setFilePath("file://"+cursor.getString(pathColumn));
                        //image.setFilePath(cursor.getString(pathColumn));

                        String mimeType = cursor.getString(mimeTypeColumn);

                        // Kiểm tra loại file
                        String type = mimeType.startsWith("image/") ? "image" : "video";
                        image.setType(type);

                        // Thêm vào danh sách
                        mediaList.add(image);
                    } while (cursor.moveToNext());
                } else {
                    Log.d("MediaCursor", "Cursor is null or empty");
                    try {
                        Log.d("MediaCursor", "Cursor count: " + cursor.getCount());
                        Log.d("MediaCursor", "Cursor columns:" + cursor.getColumnCount());
                    } catch (Exception e) {
                        Log.d("MediaCursor", "Error querying media: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Log.d("MediaCursor", "Error querying media: " + e.getMessage());
                e.printStackTrace();
            }
        }
        Log.d("MediaList", "Check" + mediaList.isEmpty());
        db.loadImages(mediaList);
        return mediaList;
    }

    public void loadImagesOnce() {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isLoaded = prefs.getBoolean("isLoaded", false);
        if (!isLoaded) {
            loadMediaData();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isLoaded", true);
            editor.apply();
        }
        else {
            Log.d("MediaCursor", "Images already loaded");
            //Toast.makeText(context, "Images already loaded", Toast.LENGTH_SHORT).show();
        }

    }

}
