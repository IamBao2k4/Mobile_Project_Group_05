//package com.example.mobile_project_g5;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.ColorMatrix;
//import android.graphics.ColorMatrixColorFilter;
//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//
//import ja.burhanrashid52.photoeditor.PhotoEditor;
//import ja.burhanrashid52.photoeditor.PhotoEditorView;
//
//public class EditImageActivity extends AppCompatActivity {
//    private PhotoEditor photoEditor;
//    private PhotoEditorView photoEditorView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_image);
//
//        photoEditorView = findViewById(R.id.editImageView);
//
//        // Khởi tạo PhotoEditor
//        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
//                .setPinchTextScalable(true) // Cho phép phóng to thu nhỏ văn bản
//                .build();
//
//
//        // Lấy đường dẫn ảnh từ Intent
//        String imagePath = getIntent().getStringExtra("image_path");
//
//        if (imagePath != null && !imagePath.isEmpty()) {
//            Uri imageUri = Uri.parse(imagePath);
//            // Sử dụng thư viện Glide để tải và hiển thị hình ảnh
//            Glide.with(this)
//                    .load(imageUri)
//                    .override(photoEditorView.getWidth(), photoEditorView.getHeight()) // Đảm bảo ảnh khớp với View
//                    .into(photoEditorView.getSource());// Sử dụng setImageResource với ID
//        } else {
//            String defaultPath = "android.resource://com.example.mobile_project_g5/drawable/so5";
//            // Optionally set a default image
//            Glide.with(this)
//                    .load(Uri.parse(defaultPath))
//                    .override(photoEditorView.getWidth(), photoEditorView.getHeight()) // Đảm bảo ảnh khớp với View
//                    .into(photoEditorView.getSource());// Sử dụng setImageResource với ID
//        }
//        photoEditorView.getSource().setScaleType(ImageView.ScaleType.FIT_XY); // Đặt scaleType phù hợp
//
//        Button backButton = findViewById(R.id.btnSoloBack);
//        backButton.setOnClickListener(v -> finish());
//
//
//        ImageButton btnAddText = findViewById(R.id.btnAddText);
//        ImageButton btnDraw = findViewById(R.id.btnDraw);
//        ImageButton btnFilter = findViewById(R.id.btnFilter);
//        ImageButton btnUndo = findViewById(R.id.btnUndo);
//        ImageButton btnRedo = findViewById(R.id.btnRedo);
//
//        btnAddText.setOnClickListener(v -> addTextToImage());
//        btnDraw.setOnClickListener(v -> enableDrawing());
//        btnFilter.setOnClickListener(v -> showFilterDialog());
//        btnUndo.setOnClickListener(v -> Undo());
//        btnRedo.setOnClickListener(v -> Redo());
//
//    }
//
//    private void addTextToImage() {
//        // Hiển thị dialog để nhập văn bản
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Nhập văn bản");
//
//        final EditText input = new EditText(this);
//        builder.setView(input);
//
//        builder.setPositiveButton("OK", (dialog, which) -> {
//            String text = input.getText().toString();
//            if (!text.isEmpty()) {
//                // Thêm văn bản vào ảnh
//                photoEditor.addText(text, Color.RED);
//                Toast.makeText(this, "Đã thêm văn bản!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Vui lòng nhập văn bản!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
//        builder.show();
//
//    }
//
//    private void enableDrawing() {
//        photoEditor.setBrushDrawingMode(true);
//        photoEditor.setBrushColor(Color.BLUE);
//        photoEditor.setBrushSize(10);
//
//        Toast.makeText(this, "Đã bật chế độ vẽ", Toast.LENGTH_SHORT).show();
//    }
//
//    private void showFilterDialog() {
//        // Tạo danh sách bộ lọc để người dùng chọn
//        String[] filters = {"Sepia", "Grayscale", "Brightness", "Contrast", "Invert"};
//
//        // Tạo dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select filter");
//        builder.setItems(filters, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Áp dụng bộ lọc tương ứng với lựa chọn
//                switch (which) {
//                    case 0:
//                        applySepiaFilter();
//                        break;
//                    case 1:
//                        applyGrayscaleFilter();
//                        break;
//                    case 2:
//                        applyBrightnessFilter();
//                        break;
//                    case 3:
//                        applyContrastFilter();
//                        break;
//                    case 4:
//                        applyInvertFilter();
//                        break;
//                }
//            }
//        });
//        builder.show();
//    }
//
//    // Các phương thức áp dụng bộ lọc
//    private void applySepiaFilter() {
//        photoEditor.setFilterEffect(ja.burhanrashid52.photoeditor.PhotoFilter.SEPIA);
//        Toast.makeText(this, "Áp dụng bộ lọc Sepia!", Toast.LENGTH_SHORT).show();
//    }
//
//    private void applyGrayscaleFilter() {
//        // Tạo ColorMatrix để chuyển ảnh thành grayscale
//        ColorMatrix matrix = new ColorMatrix();
//        matrix.setSaturation(0);  // Thiết lập saturation = 0 để ảnh chuyển thành grayscale
//
//        // Áp dụng color matrix cho ảnh trong PhotoEditorView
//        photoEditorView.getSource().setColorFilter(new ColorMatrixColorFilter(matrix));
//        Toast.makeText(this, "Đã áp dụng bộ lọc Grayscale!", Toast.LENGTH_SHORT).show();
//    }
//
//    private void applyBrightnessFilter() {
//        photoEditor.setFilterEffect(ja.burhanrashid52.photoeditor.PhotoFilter.BRIGHTNESS);
//        Toast.makeText(this, "Áp dụng bộ lọc Brightness!", Toast.LENGTH_SHORT).show();
//    }
//
//    private void applyContrastFilter() {
//        photoEditor.setFilterEffect(ja.burhanrashid52.photoeditor.PhotoFilter.CONTRAST);
//        Toast.makeText(this, "Áp dụng bộ lọc Contrast!", Toast.LENGTH_SHORT).show();
//    }
//
//    private void applyInvertFilter() {
//        // Tạo ColorMatrix để đảo ngược màu sắc
//        ColorMatrix matrix = new ColorMatrix();
//        matrix.setSaturation(0);  // Đảm bảo saturation bằng 0 để ảnh chuyển sang grayscale
//        matrix.set(new float[] {
//                -1, 0, 0, 0, 255,  // Red
//                0, -1, 0, 0, 255,  // Green
//                0, 0, -1, 0, 255,  // Blue
//                0, 0, 0, 1, 0      // Alpha (không thay đổi)
//        });
//
//        // Áp dụng bộ lọc đảo ngược màu sắc cho ảnh trong PhotoEditorView
//        photoEditorView.getSource().setColorFilter(new ColorMatrixColorFilter(matrix));
//        Toast.makeText(this, "Đã áp dụng bộ lọc Invert!", Toast.LENGTH_SHORT).show();
//    }
//
//    private void Undo(){
//        photoEditor.undo();
//    }
//
//    private void Redo() {
//        photoEditor.redo();
//    }
//}




package com.example.mobile_project_g5;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class EditImageActivity extends AppCompatActivity {
    private PhotoEditor photoEditor;
    private PhotoEditorView photoEditorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        photoEditorView = findViewById(R.id.editImageView);

        // Khởi tạo PhotoEditor
        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true) // Cho phép phóng to thu nhỏ văn bản
                .build();

        // Lấy đường dẫn ảnh từ Intent
        String imagePath = getIntent().getStringExtra("image_path");
        if (imagePath != null && !imagePath.isEmpty()) {
            loadImage(Uri.parse(imagePath));
        } else {
            // Sử dụng ảnh mặc định nếu không có ảnh từ Intent
            String defaultPath = "android.resource://com.example.mobile_project_g5/drawable/so5";
            loadImage(Uri.parse(defaultPath));
        }

        // Cài đặt scaleType
        photoEditorView.getSource().setScaleType(ImageView.ScaleType.FIT_XY);

        // Xử lý nút Back
        Button backButton = findViewById(R.id.btnSoloBack);
        backButton.setOnClickListener(v -> finish());

        // Các chức năng chỉnh sửa
        ImageButton btnAddText = findViewById(R.id.btnAddText);
        ImageButton btnDraw = findViewById(R.id.btnDraw);
        ImageButton btnFilter = findViewById(R.id.btnFilter);
        ImageButton btnUndo = findViewById(R.id.btnUndo);
        ImageButton btnRedo = findViewById(R.id.btnRedo);
        Button btnSave = findViewById(R.id.btnSave);

        btnAddText.setOnClickListener(v -> addTextToImage());
        btnDraw.setOnClickListener(v -> enableDrawing());
        btnFilter.setOnClickListener(v -> showFilterDialog());
        btnUndo.setOnClickListener(v -> Undo());
        btnRedo.setOnClickListener(v -> Redo());
        btnSave.setOnClickListener(v -> saveImage());
    }

    private void loadImage(Uri uri) {
        // Sử dụng ViewTreeObserver để đảm bảo view đã đo lường xong trước khi tải ảnh
        ViewTreeObserver viewTreeObserver = photoEditorView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {
            Glide.with(this)
                    .load(uri)
                    .override(photoEditorView.getWidth(), photoEditorView.getHeight())
                    .into(photoEditorView.getSource());
        });
    }

    private void addTextToImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập văn bản");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String text = input.getText().toString();
            if (!text.isEmpty()) {
                photoEditor.addText(text, Color.RED);
                Toast.makeText(this, "Đã thêm văn bản!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Vui lòng nhập văn bản!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void enableDrawing() {
        photoEditor.setBrushDrawingMode(true);
        photoEditor.setBrushColor(Color.BLUE);
        photoEditor.setBrushSize(10);
        Toast.makeText(this, "Đã bật chế độ vẽ", Toast.LENGTH_SHORT).show();
    }

    private void showFilterDialog() {
        String[] filters = {"Sepia", "Grayscale", "Brightness", "Contrast", "Invert"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn bộ lọc");
        builder.setItems(filters, (dialog, which) -> {
            switch (which) {
                case 0:
                    photoEditor.setFilterEffect(ja.burhanrashid52.photoeditor.PhotoFilter.SEPIA);
                    break;
                case 1:
                    applyGrayscaleFilter();
                    break;
                case 2:
                    photoEditor.setFilterEffect(ja.burhanrashid52.photoeditor.PhotoFilter.BRIGHTNESS);
                    break;
                case 3:
                    photoEditor.setFilterEffect(ja.burhanrashid52.photoeditor.PhotoFilter.CONTRAST);
                    break;
                case 4:
                    applyInvertFilter();
                    break;
            }
            Toast.makeText(this, "Đã áp dụng bộ lọc!", Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }

    private void applyGrayscaleFilter() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        photoEditorView.getSource().setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    private void applyInvertFilter() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{
                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0, 0, 1, 0
        });
        photoEditorView.getSource().setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    private void saveImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android >= 10: Sử dụng Scoped Storage
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "edited_image_" + System.currentTimeMillis() + ".png");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/EditedImages");

            ContentResolver resolver = getContentResolver();
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (imageUri != null) {
                try {
                    String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            + "/EditedImages/edited_image_" + System.currentTimeMillis() + ".png";

                    photoEditor.saveAsFile(filePath, new PhotoEditor.OnSaveListener() {
                        @Override
                        public void onSuccess(@NonNull String imagePath) {

                            SQLiteDataBase dbHelper = new SQLiteDataBase(EditImageActivity.this);
                            dbHelper.addImage(null, imagePath, null);
                            updateImageStatus(imagePath);

                            Toast.makeText(EditImageActivity.this, "Ảnh đã lưu tại: " + imagePath, Toast.LENGTH_LONG).show();

                            // Gọi hàm cập nhật Gallery
                            notifyGallery(imagePath);

                            reloadImageView(imagePath);
                        }

                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(EditImageActivity.this, "Không thể lưu ảnh: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Không thể lưu ảnh!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Không thể tạo URI để lưu ảnh!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Android < 10: Lưu ảnh vào bộ nhớ ngoài
            File outputDir = new File(Environment.getExternalStorageDirectory(), "EditedImages");
            if (!outputDir.exists() && !outputDir.mkdirs()) {
                Toast.makeText(this, "Không thể tạo thư mục lưu ảnh!", Toast.LENGTH_SHORT).show();
                return;
            }

            File outputFile = new File(outputDir, "edited_image_" + System.currentTimeMillis() + ".png");

            try {
                photoEditor.saveAsFile(outputFile.getAbsolutePath(), new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        SQLiteDataBase dbHelper = new SQLiteDataBase(EditImageActivity.this);
                        dbHelper.addImage(null, imagePath, null);
                        updateImageStatus(imagePath);

                        Toast.makeText(EditImageActivity.this, "Ảnh đã lưu tại: " + imagePath, Toast.LENGTH_LONG).show();

                        // Gọi hàm cập nhật Gallery
                        notifyGallery(imagePath);

                        reloadImageView(imagePath);
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditImageActivity.this, "Không thể lưu ảnh: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Không thể lưu ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Cập nhật trạng thái active = 1 cho ảnh trong cơ sở dữ liệu (hoặc lưu trữ)
    private void updateImageStatus(String imagePath) {
        SQLiteDataBase dbHelper = new SQLiteDataBase(this);
        // Giả sử bạn lưu ảnh trong SQLite, cập nhật trạng thái active = 1
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("active", 1); // Cập nhật trạng thái active = 1
        values.put("image_path", imagePath); // Đường dẫn ảnh

        String whereClause = "image_path = ?";
        String[] whereArgs = {imagePath};

        int rows = database.update("images", values, whereClause, whereArgs);
        if (rows > 0) {
            Log.d("EditImageActivity", "Cập nhật trạng thái active = 1 thành công!");
        }
    }

    // Cập nhật lại giao diện sau khi lưu ảnh
    private void reloadImageView(String imagePath) {
        Glide.with(this)
                .load(imagePath)
                .into(photoEditorView.getSource());  // Cập nhật lại hình ảnh trong ImageView
    }

    // Thêm ảnh vào thư viện để hiển thị trong ứng dụng ảnh của hệ thống
    private void notifyGallery(String imagePath) {
        MediaScannerConnection.scanFile(this, new String[]{imagePath}, null, (path, uri) -> {
            // Callback khi hoàn tất quét
            Log.d("EditImageActivity", "Gallery đã cập nhật: " + path);
        });
    }


    private void Undo() {
        photoEditor.undo();
    }

    private void Redo() {
        photoEditor.redo();
    }
}



