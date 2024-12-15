package com.example.mobile_project_g5;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class IdentifyDuplicateImage {
    private Context context;
    public IdentifyDuplicateImage(Context context) {
        this.context = context;
    }
    public String calculatePHash(Bitmap bitmap) {
        // Resize ảnh xuống kích thước nhỏ hơn để đơn giản hóa
        Bitmap resized = null;
        if (bitmap != null && !bitmap.isRecycled()) {
            try {
                // Tạo bitmap mới với kích thước 8x8
                resized = Bitmap.createScaledBitmap(bitmap, 8, 8, false);
                // Sử dụng resized
            } catch ( OutOfMemoryError e) {
            }
        }
        else {
            return null;
        }

        // Chuyển ảnh sang màu xám
        Bitmap grayBitmap = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(grayBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[]{
                0.299f, 0.587f, 0.114f, 0, 0,
                0.299f, 0.587f, 0.114f, 0, 0,
                0.299f, 0.587f, 0.114f, 0, 0,
                0, 0, 0, 1, 0
        })));
        canvas.drawBitmap(resized, 0, 0, paint);

        // Lấy độ sáng trung bình
        int[] pixels = new int[64];
        grayBitmap.getPixels(pixels, 0, 8, 0, 0, 8, 8);
        int sum = 0;
        for (int pixel : pixels) {
            sum += Color.red(pixel);
        }
        int avg = sum / 64;

        // Tính hash
        StringBuilder hash = new StringBuilder();
        for (int pixel : pixels) {
            hash.append(Color.red(pixel) >= avg ? '1' : '0');
        }
        return hash.toString();
    }
    public boolean isDuplicate(String hash1, String hash2) {
        int count = 0;
        for (int i = 0; i < hash1.length(); i++) {
            if (hash1.charAt(i) != hash2.charAt(i)) {
                count++;
            }
            if (count > 5) {
                return false;
            }
        }
        return true;
    }
    public Bitmap ImagetoBitmap(String path){
        if(path.contains("android.resource"))
        {
            String resourceName = path.substring(path.lastIndexOf("/") + 1);

            // Lấy resourceId
            int resId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
            try {
                Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(), resId);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            try {
                path = path.replace("file://", "");
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public Map<Integer, List<ImageClass>> GroupDuplicateImages() {
        Map<Integer, List<ImageClass>> groups = new HashMap<>();
        Map<String, List<ImageClass>> hashToGroupMap = new HashMap<>(); // Map từ hash -> nhóm
        List<ImageClass> images;

        // Lấy danh sách ảnh từ database
        try (SQLiteDataBase db = new SQLiteDataBase(this.context)) {
            db.openDatabase();
            images = Arrays.asList(db.getAllImages());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        for (ImageClass image : images) {
            if (!image.getType().equals("image")) continue;

            String hash = calculatePHash(ImagetoBitmap(image.getFilePath()));

            boolean isGrouped = false;
            for (String groupHash : hashToGroupMap.keySet()) {
                if (isDuplicate(hash, groupHash)) {
                    Objects.requireNonNull(hashToGroupMap.get(groupHash)).add(image);
                    isGrouped = true;
                    break;
                }
            }

            if (!isGrouped) {
                List<ImageClass> newGroup = new ArrayList<>();
                newGroup.add(image);
                hashToGroupMap.put(hash, newGroup);
            }
        }

        int groupId = 0;
        for (List<ImageClass> group : hashToGroupMap.values()) {
            groups.put(groupId++, group);
        }

        return groups;
    }
    public AlbumClass toAlbumClass(Map<Integer, List<ImageClass>> groups) {
        int totalImages = 0;
        for (List<ImageClass> group : groups.values()) {
            if (group.size() >= 2)
                totalImages += group.size();
        }
        ImageClass[] images = new ImageClass[totalImages];

        int index = 0;
        for (List<ImageClass> group : groups.values()) {
            if(group.size() >= 2) {
                for (ImageClass image : group) {
                    images[index++] = image;
                }
            }
        }
        AlbumClass album = new AlbumClass("Duplicate","-1","",images);
        return album;
    }
}