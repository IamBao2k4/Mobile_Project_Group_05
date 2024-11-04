package com.example.mobile_project_g5;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.util.Log;
import android.widget.Toast;

public class SQLiteDataBase extends SQLiteOpenHelper {
    private static final String DB_NAME = "albumAppDB.db";
    private final Context context;

    public SQLiteDataBase(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAlbumQuery = "CREATE TABLE IF NOT EXISTS Album (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Information TEXT)";
        db.execSQL(createAlbumQuery);
        String createImageQuery = "CREATE TABLE IF NOT EXISTS Image (Id INTEGER PRIMARY KEY AUTOINCREMENT, file_path TEXT, Information TEXT, is_favorite INTERGER, exif_datetime TEXT, activate TEXT, is_selected INTEGER, delete_at TEXT, FOREIGN KEY (Album_Id) REFERENCES Album(Id))";
        db.execSQL(createImageQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý nâng cấp cơ sở dữ liệu nếu cần
        String dropAlbumQuery = "DROP TABLE IF EXISTS Album";
        db.execSQL(dropAlbumQuery);
        String dropImageQuery = "DROP TABLE IF EXISTS Image";
        db.execSQL(dropImageQuery);
        onCreate(db);
    }

    public AlbumClass[] getAlbum() {
        SQLiteDatabase db = this.openDatabase();
        List<AlbumClass> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Album", null);
        if (cursor.moveToFirst()) {
            do {
                String albumName = cursor.getString(1);
                String albumID = cursor.getString(0);
                String information = cursor.getString(2);
                List<ImageClass> images = new ArrayList<ImageClass>();
                String[] param = new String[]{albumID};
                Cursor cursor2 = db.rawQuery("SELECT * FROM Image img JOIN Album a ON img.Album_Id = a.Id WHERE a.Id = ?", param);
                if (cursor2.moveToFirst()) {
                    do {
                       {
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

    public void addAlbum(String name, String infor) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Name",name.trim());
            values.put("Information", infor.trim());
            db.insert("Album", null, values);
            Toast.makeText(context, "Thêm album thành công", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Lỗi khi thêm album: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("DatabaseError", "Exception: ", e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
    public void deleteAlbum(String albumID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Album", "Id = ?", new String[]{albumID});
        db.close();
        AlbumClass[] albums = getAlbum();
    }

    public ImageClass[] getImagebyAlbumId(String albumID){
        SQLiteDatabase db = this.openDatabase();
        List<ImageClass> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Image Where Image.Album_Id == ?", new String[]{albumID});
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
                res.add(image);
            }while (cursor.moveToNext()) ;
        }
        cursor.close();
        db.close();
        return res.toArray(new ImageClass[0]);
    }

    public boolean updateImageInAlbum(String albumId, ImageClass[] images) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ImageClass image : images) {
            ContentValues values = new ContentValues();
            values.put("Album_Id", albumId);
            int rowsAffected = db.update("Image", values, "ID = ?", new String[]{String.valueOf(image.getImageID())});
            if (rowsAffected == 0) {
                //db.close();
                return false;
            }
            }
        db.close();
        return true;
                images.add(image);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return images.toArray(new ImageClass[0]);
    }
}
