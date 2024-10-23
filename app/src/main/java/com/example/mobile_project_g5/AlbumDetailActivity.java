package com.example.mobile_project_g5;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlbumDetailActivity extends AppCompatActivity {
    private static final String EXTRA_ALBUM_NAME = "album_name";

    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        String albumName = getIntent().getStringExtra(EXTRA_ALBUM_NAME);
        TextView albumNameTextView = findViewById(R.id.album_name);
        albumNameTextView.setText(albumName);

        //tao hoac mo database
        mydatabase = openOrCreateDatabase("qlalbum.sql", MODE_PRIVATE, null);
        //Tao table chua du lieu
        try{
            String sql = "CREATE TABLE picture(imgID INT primary key, info TEXT)";
            mydatabase.execSQL(sql);
        }
        catch (Exception e)
        {
            Log.e("Error", "Table da ton tai");
        }

        int[] images = {
                R.drawable.picture1,
                R.drawable.picture2,
                R.drawable.picture3,
                // Thêm các ID drawable khác
        };

        //them du lieu vao database
        for(int i=0;i<3;i++) {
            ContentValues myvalue = new ContentValues();
            myvalue.put("imgID", images[i]);
            myvalue.put("info", "Day la anh thu " + (i+1));
            mydatabase.insert("picture", null, myvalue);
        }


        //lay du lieu tu database de truyen di
        Cursor cursor = mydatabase.rawQuery("SELECT imgID, info FROM picture", null);
        int[] imagesFromDB = new int[cursor.getCount()];
        String[] infoFromDB = new String[cursor.getCount()];
        int index = 0;

        if (cursor.moveToFirst()) {
            do {
                imagesFromDB[index] = cursor.getInt(0); // Lấy imgID
                infoFromDB[index] = cursor.getString(1); // Lấy info
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();



        GridView gridViewImages = findViewById(R.id.grid_view_images);
        ImageAdapter imageAdapter = new ImageAdapter(this, imagesFromDB, infoFromDB);// Bạn cần tạo ImageAdapter
        gridViewImages.setAdapter(imageAdapter);
    }


    public static Intent newIntent(Context context, String albumName) {
        Intent intent = new Intent(context, AlbumDetailActivity.class);
        intent.putExtra(EXTRA_ALBUM_NAME, albumName);
        return intent;
    }
}

