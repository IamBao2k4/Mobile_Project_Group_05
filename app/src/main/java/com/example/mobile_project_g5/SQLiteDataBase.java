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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class SQLiteDataBase extends SQLiteOpenHelper {
    private static final String DB_NAME = "albumAppDB.db";
    private final Context context;

    public SQLiteDataBase(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        File dbFile = context.getDatabasePath(DB_NAME);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //File dbFile = context.getDatabasePath(DB_NAME);
        CreateTables(db);
    }

    private void CreateTables(SQLiteDatabase db) {
        String createAlbumQuery = "CREATE TABLE IF NOT EXISTS Album (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT NOT NULL," +
                "Information TEXT" +
                ")";
        db.execSQL(createAlbumQuery);

        String createImageQuery = "CREATE TABLE IF NOT EXISTS Image (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Album_Id INTEGER NOT NULL," +
                "file_path TEXT NOT NULL," +
                "Information TEXT," +
                "is_favorite INTEGER," +
                "exif_datetime TEXT," +
                "activate TEXT," +
                "is_selected INTEGER," +
                "deleted_at TEXT," +
                "type TEXT" +
                ")";
        db.execSQL(createImageQuery);
        InsertMockData(db);
    }

    private void InsertMockData(SQLiteDatabase db) {
        String insertAlbumQuery = "INSERT INTO Album (Name, Information) VALUES " +
                "('Album 1', 'ALbum 1')," +
                "('ALbum 2', 'ALbum 2')," +
                "('Album 3', 'ALbum 3')";
        db.execSQL(insertAlbumQuery);

        String insertImageQuery = "INSERT INTO Image (Album_Id, file_path, Information, is_favorite, exif_datetime, activate, is_selected, deleted_at, type) VALUES " +
                "(1, 'android.resource://com.example.mobile_project_g5/drawable/banana', 'Information1', 1, '2024-11-28 00:00:00', 1, 0, NULL, 'image')," +
                "(1, 'android.resource://com.example.mobile_project_g5/drawable/cherry', 'Information2', 0, '2024-12-15 00:00:00', 1, 0, NULL, 'image')," +
                "(2, 'android.resource://com.example.mobile_project_g5/drawable/elderberry', 'Information3', 1, '2024-10-30 00:00:00', 1, 0, NULL, 'image')," +
                "(2, 'android.resource://com.example.mobile_project_g5/drawable/fig', 'Information4', 0, '2024-11-28 00:00:00', 1, 0, NULL, 'image')," +
                "(3, 'android.resource://com.example.mobile_project_g5/drawable/grape', 'Information5', 1, '2024-10-30 00:00:00', 1, 0, NULL, 'image')," +
                "(3, 'android.resource://com.example.mobile_project_g5/drawable/honeydew', 'Information6', 0, '2024-12-15 00:00:00', 1, 0, NULL, 'image')," +
                "(1, 'android.resource://com.example.mobile_project_g5/drawable/kiwi', 'Information7', 1, '2024-11-28 00:00:00', 1, 0, NULL, 'image')," +
                "(1, 'android.resource://com.example.mobile_project_g5/drawable/lemon', 'Information8', 0, '2024-10-30 00:00:00', 1, 0, NULL, 'image')," +
                "(2, 'android.resource://com.example.mobile_project_g5/drawable/mango', 'Information9', 1, '2024-12-15 00:00:00', 1, 0, NULL, 'image')," +
                "(1, 'android.resource://com.example.mobile_project_g5/drawable/nectarine', 'Information10', 0, '2024-10-30 00:00:00', 1, 0, NULL, 'image')," +
                "(2, 'android.resource://com.example.mobile_project_g5/drawable/apple', 'Information11', 1, '2024-10-30 00:00:00', 1, 0, NULL, 'image')," +
                "(3, 'android.resource://com.example.mobile_project_g5/drawable/tangerine', 'Information12', 1, '2024-11-28 00:00:00', 1, 0, NULL, 'image')," +
                "(3, 'android.resource://com.example.mobile_project_g5/drawable/orange', 'Information13', 1, '2024-10-30 00:00:00', 1, 0, NULL, 'image')," +
                "(1, 'android.resource://com.example.mobile_project_g5/drawable/pear', 'Information14', 0, '2024-12-15 00:00:00', 1, 0, NULL, 'image')," +
                "(1, 'android.resource://com.example.mobile_project_g5/drawable/raspberry', 'Information15', 1, '2024-10-30 00:00:00', 1, 0, NULL, 'image')," +
                "(2, 'android.resource://com.example.mobile_project_g5/drawable/strawberry', 'Information16', 0, '2024-11-28 00:00:00', 1, 0, NULL, 'image')," +
                "(2, 'android.resource://com.example.mobile_project_g5/drawable/strawberry1', 'Information17', 0, '2024-10-30 00:00:00', 1, 0, NULL, 'image')," +
                "(3, 'android.resource://com.example.mobile_project_g5/drawable/anhtrung1', 'Information18', 0, '2024-11-28 00:00:00', 1, 0, NULL, 'image')," +
                "(2, 'android.resource://com.example.mobile_project_g5/drawable/anhtrung2', 'Information19', 0, '2024-10-30 00:00:00', 1, 0, NULL, 'image')," +
                "(1, 'android.resource://com.example.mobile_project_g5/raw/videoplaceholder', 'Information20', 0, '2024-11-24 00:00:00', 1, 0, NULL, 'video');";
        db.execSQL(insertImageQuery);
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

    public SQLiteDatabase openDatabase() {
        File dbFile = context.getDatabasePath(DB_NAME);
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    public AlbumClass[] getAlbum() {
        SQLiteDatabase db = this.openDatabase();
        List<AlbumClass> res = new ArrayList<>();
        String[] except = new String[]{"All", "Duplicate"};
        Cursor cursor = db.rawQuery("SELECT * FROM Album WHERE Name != ? and Name != ?", except);
        if (cursor.moveToFirst()) {
            do {
                String albumName = cursor.getString(1);
                String albumID = cursor.getString(0);
                String information = cursor.getString(2);
                List<ImageClass> images = new ArrayList<ImageClass>();
                String[] param = new String[]{albumID, "1"};
                Cursor cursor2 = db.rawQuery("SELECT * FROM Image img JOIN Album a ON img.Album_Id = a.Id WHERE a.Id = ? AND img.activate = ?", param);
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
                                    cursor2.getString(8),
                                    cursor2.getString(9)
                            );
                            images.add(image);
                        }

                    } while (cursor2.moveToNext());
                }
                cursor2.close();
                AlbumClass album = new AlbumClass(albumName, albumID, information, images.toArray(new ImageClass[0]));
                res.add(album);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return res.toArray(new AlbumClass[0]);
    }

    public void addAlbum(String name, String infor) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Name", name.trim());
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
    }

    public void deleteImage(String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("activate", "0");
        cv.put("deleted_at", System.currentTimeMillis());
        int rowsUpdated = db.update("Image", cv, "file_path = ?", new String[]{path});
        if (rowsUpdated == 0) {
            Toast.makeText(context, "Lỗi khi xóa ảnh", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Xóa ảnh thành công", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public ImageClass[] getAllImages() {
        List<ImageClass> res = new ArrayList<>();
        try {
            SQLiteDatabase db = this.openDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Image WHERE activate = ?", new String[]{"1"});
            if (cursor.moveToFirst()) {
                do {
                    ImageClass image = new ImageClass(
                            cursor.getInt(0),   // imageID
                            cursor.getString(1), // albumID
                            cursor.getString(2), // filePath
                            cursor.getString(3), // information
                            cursor.getInt(4),    // isFavorite
                            cursor.getString(5), // exifDatetime
                            cursor.getString(6), // activate
                            cursor.getInt(7),    // isSelected
                            cursor.getString(8),  // deleteAt
                            cursor.getString(9)
                    );
                    res.add(image);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("getAllImages", "Error retrieving images", e);
        }
        // Sắp xếp danh sách theo exifDatetime (so sánh trực tiếp chuỗi)
        res.sort((img1, img2) -> {
            String date1 = img1.getExifDatetime();
            String date2 = img2.getExifDatetime();

            if (date1 == null || date2 == null) return 0; // Xử lý trường hợp null
            return date1.compareTo(date2); // So sánh chuỗi
        });

        return res.toArray(new ImageClass[0]);
    }

    ;
    public void loadImages(List<ImageClass> images) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ImageClass image : images) {
            try{
                ContentValues values = getContentValues(image);
                long rowId = db.insert("Image", null, values);
                if (rowId == -1) {
                    Toast.makeText(context, "Lỗi khi thêm ảnh.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            catch (Exception e){
                Log.e("DatabaseError", "Exception: ", e);
            }
        }
    }

    private static @NonNull ContentValues getContentValues(ImageClass image) {
        ContentValues values = new ContentValues();
        values.put("Album_Id", image.getAlbumID());
        values.put("file_path", image.getFilePath());
        values.put("Information", image.getInformation());
        values.put("is_favorite", 0); // 0 = Không yêu thích, 1 = Yêu thích
        values.put("exif_datetime", image.getExifDatetime());
        values.put("activate", "1"); // 1 = Kích hoạt, 0 = Không kích hoạt
        values.put("is_selected", 0); // 0 = Không được chọn, 1 = Được chọn
        values.put("deleted_at", (String) null); // NULL khi ảnh chưa bị xóa
        values.put("type", image.getType());
        return values;
    }

    public void addImage(String albumId, String filePath, String information) {
        SQLiteDatabase db = this.getWritableDatabase();
        String exifDatetime = convertMillisToDateTime(System.currentTimeMillis());
        try {
            ContentValues values = new ContentValues();
            values.put("Album_Id", albumId);
            values.put("file_path", filePath);
            values.put("Information", information);
            values.put("is_favorite", 0); // 0 = Không yêu thích, 1 = Yêu thích
            values.put("exif_datetime", exifDatetime); // Lưu thời gian hiện tại (timestamp)
            values.put("activate", "1"); // 1 = Kích hoạt, 0 = Không kích hoạt
            values.put("is_selected", 0); // 0 = Không được chọn, 1 = Được chọn
            values.put("deleted_at", (String) null); // NULL khi ảnh chưa bị xóa
            values.put("type", "image"); // Loại mặc định

            long rowId = db.insert("Image", null, values);

            if (rowId == -1) {
                Toast.makeText(context, "Lỗi khi thêm ảnh.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Thêm ảnh thành công.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("DatabaseError", "Exception: ", e);
        } finally {
            db.close();
        }
    }

    // Hàm chuyển đổi thời gian
    private String convertMillisToDateTime(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    public ImageClass[] getImagesByAlbumId(String albumId) {
        List<ImageClass> res = new ArrayList<>();
        SQLiteDatabase db = this.openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Image WHERE Album_Id = ? AND activate = ?", new String[]{albumId, "1"});
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
                        cursor.getString(8),
                        cursor.getString(9));
                res.add(image);
            } while (cursor.moveToNext());
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
    }

    public ImageClass[] getFavoriteImages() {
        List<ImageClass> res = new ArrayList<>();
        SQLiteDatabase db = this.openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Image WHERE is_favorite = 1 AND activate = 1", null);
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
                        cursor.getString(8),
                        cursor.getString(9));
                res.add(image);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return res.toArray(new ImageClass[0]);
    }

    public ImageClass[] getDeletedImage() {
        List<ImageClass> res = new ArrayList<>();
        SQLiteDatabase db = this.openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Image WHERE activate = ?", new String[]{"0"});
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
                        cursor.getString(8),
                        cursor.getString(9));
                res.add(image);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return res.toArray(new ImageClass[0]);
    }

    public void restoreImage(String filePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("activate", "1");
        cv.put("deleted_at", "");
        int rowsUpdated = db.update("Image", cv, "file_path = ?", new String[]{filePath});
        if (rowsUpdated == 0) {
            Toast.makeText(context, "Lỗi khi khôi phục ảnh", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Khôi phục ảnh thành công", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public ImageClass getImageByPath(String restoredImagePath) {
        SQLiteDatabase db = this.openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Image WHERE file_path = ?", new String[]{restoredImagePath});
        if (cursor.moveToFirst()) {
            ImageClass image = new ImageClass(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getString(8),
                    cursor.getString(9));
            cursor.close();
            db.close();
            return image;
        }
        return null;
    }

    public void updateImage(ImageClass image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", image.getImageID());
        values.put("Album_Id", image.getAlbumID());
        values.put("file_path", image.getFilePath());
        values.put("Information", image.getInformation());
        values.put("is_favorite", image.getIsFavorite());
        values.put("exif_datetime", image.getExifDatetime());
        values.put("activate", image.getActivate());
        values.put("is_selected", image.getIsSelected());
        values.put("deleted_at", image.getDeleteAt());
        values.put("type", image.getType());
        db.update("Image", values, "ID = ?", new String[]{String.valueOf(image.getImageID())});
        db.close();
    }

    public String[] getMonthYear() {
        List<String> res = new ArrayList<>();
        SQLiteDatabase db = this.openDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT strftime('%m-%Y', exif_datetime) " +
                                        "FROM Image " +
                                        "WHERE activate = 1 " +
                                        "GROUP BY strftime('%m-%Y', exif_datetime) " +
                                        "ORDER BY strftime('%m-%Y', exif_datetime);"
                                        , null);
        if (cursor.moveToFirst()) {
            do {
                res.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return res.toArray(new String[0]);
    }

    public ImageClass[] getImagesByMonthYear(String monthYear) {
        List<ImageClass> res = new ArrayList<>();
        SQLiteDatabase db = this.openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Image WHERE strftime('%m-%Y', exif_datetime) = ? AND activate = ?", new String[]{monthYear, "1"});
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
                        cursor.getString(8),
                        cursor.getString(9));
                res.add(image);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
            return res.toArray(new ImageClass[0]);
        }
        return null;
    }
}

