package com.example.mobile_project_g5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class SQLiteDataBase extends SQLiteOpenHelper {
    private static final String DB_NAME = "albumAppDB.db";
    private final Context context;

    public SQLiteDataBase(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Không cần thiết vì chúng ta đang sử dụng cơ sở dữ liệu có sẵn
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý nâng cấp cơ sở dữ liệu nếu cần
    }

    public AlbumClass[] getAlbum() {
        List<AlbumClass> res = new ArrayList<>();
        SQLiteDatabase db = this.openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Album", null);
        if (cursor.moveToFirst()) {
            do {
                String albumName = cursor.getString(1);
                String albumID = cursor.getString(0);
                String information = cursor.getString(2);
                List<ImageClass> images = new ArrayList<ImageClass>();
                Cursor cursor2 = db.rawQuery("SELECT * FROM Image img JOIN Album a ON img.Album_Id = a.Id", null);
                if (cursor2.moveToFirst()) {
                    do {
                        if (cursor2.getString(1).equals(albumID)) {
                            ImageClass image = new ImageClass(
                                    cursor2.getInt(0),
                                    cursor2.getString(1),
                                    cursor2.getString(2),
                                    cursor2.getString(3),
                                    cursor2.getInt(4),
                                    cursor2.getString(5),
                                    cursor2.getString(6),
                                    cursor2.getInt(7),
                                    cursor2.getString(8));
                            images.add(image);
                        }

                    } while (cursor2.moveToNext());
                }
                cursor2.close();
                AlbumClass album = new AlbumClass(albumName, albumID, information, images.toArray(new ImageClass[0]));
                res.add(album);
            }while (cursor.moveToNext()) ;
        }
        cursor.close();
        db.close();
        return res.toArray(new AlbumClass[0]);
    }

    public SQLiteDatabase openDatabase() {
        File dbFile = context.getDatabasePath(DB_NAME);
        File file = new File(dbFile.toString());
        copyDatabase(dbFile);
//        Toast.makeText(context, "Open database success", Toast.LENGTH_SHORT).show();
        return SQLiteDatabase.openDatabase(dbFile.getPath(),null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void copyDatabase(File dbFile) {
        if (!dbFile.exists()) {
            try {
                InputStream openDB = context.getAssets().open(DB_NAME);
                OutputStream copyDB = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = openDB.read(buffer)) > 0) {
                    copyDB.write(buffer, 0, length);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteAlbum(String Id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("Album", "Id = ?", new String[]{Id});
        db.close();
    }

    public void deleteImage(String filePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("Image", "file_path = ?", new String[]{filePath});
        db.close();
    }

    public ImageClass[] getImagesByAlbumId(String albumId) {
        List<ImageClass> images = new ArrayList<>();
        SQLiteDatabase db = this.openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Image WHERE Album_Id = ?", new String[]{albumId});
        if (cursor.moveToFirst()) {
            do {
                ImageClass image = new ImageClass(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getString(8));
                images.add(image);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return images.toArray(new ImageClass[0]);
    }
}
