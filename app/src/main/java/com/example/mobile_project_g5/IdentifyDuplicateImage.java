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
        String resourceName = path.substring(path.lastIndexOf("/") + 1);

        // Lấy resourceId
        int resId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(),resId);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Map<Integer, List<ImageClass>> GroupDuplicateImages() {
        Map<Integer, List<ImageClass>> groups = new HashMap<>();
        List<String> hashes = new ArrayList<>();
        List<ImageClass> images = new ArrayList<>();
        try (SQLiteDataBase db = new SQLiteDataBase(this.context)) {
                db.openDatabase();
                images = Arrays.asList(db.getAllImages());
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }
        for (ImageClass image : images) {
            /* Hash Image */
            String hash = calculatePHash(ImagetoBitmap(image.getFilePath()));
            hashes.add(hash);
        }
        for (int i = 0; i < images.size(); i++) {
            boolean isGrouped = false;
            String currentHash = hashes.get(i);

            for (Map.Entry<Integer, List<ImageClass>> entry : groups.entrySet()) {
                List<ImageClass> group = entry.getValue();
                String groupHash = calculatePHash(ImagetoBitmap(group.get(0).getFilePath())); // Lấy hash của ảnh đầu tiên trong nhóm

                // So sánh hash với ngưỡng
                if (isDuplicate(currentHash, groupHash)) { // Ngưỡng Hamming Distance = 5
                    group.add(images.get(i)); // Thêm ảnh vào nhóm
                    isGrouped = true;
                    break;
                }
            }

            if (!isGrouped) {
                int newGroupId = groups.size(); // Sử dụng kích thước hiện tại của map làm ID nhóm mới
                List<ImageClass> newGroup = new ArrayList<>();
                newGroup.add(images.get(i));
                groups.put(newGroupId, newGroup);
            }
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
        AlbumClass album = new AlbumClass("Duplicate","","",images);
        return album;
    }
}
