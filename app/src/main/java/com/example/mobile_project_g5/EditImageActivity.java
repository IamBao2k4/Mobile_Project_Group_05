package com.example.mobile_project_g5;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
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
    ImageClass image;

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
            SQLiteDataBase dbHelper = new SQLiteDataBase(this);
            ImageClass [] images = dbHelper.getAllImages();

            for (ImageClass imageClass : images) {
                if (imageClass.getFilePath().equals(imagePath)) {
                    image = imageClass;
                    break;
                }
            }
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
            Bitmap originalBitmap = getBitmapFromPhotoEditorView();
            Bitmap filteredBitmap = null;
            switch (which) {
                case 0:
                    applySepiaFilter();
                    break;
                case 1:
                    applyGrayscaleFilter();
                    break;
                case 2:
                    applyBrightnessFilter(50);
                    break;
                case 3:
                    applyContrastFilter(1.5f);
                    break;
                case 4:
                    applyInvertFilter();
                    break;
            }
            if (filteredBitmap != null) {
                photoEditorView.getSource().setImageBitmap(filteredBitmap);
                Toast.makeText(this, "Đã áp dụng bộ lọc!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void applySepiaFilter() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{
                0.393f, 0.769f, 0.189f, 0f, 0f,
                0.349f, 0.686f, 0.168f, 0f, 0f,
                0.272f, 0.534f, 0.131f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
        });
        photoEditorView.getSource().setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    private void applyGrayscaleFilter() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        photoEditorView.getSource().setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    private void applyBrightnessFilter(int brightness) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{
                1, 0, 0, 0, brightness,
                0, 1, 0, 0, brightness,
                0, 0, 1, 0, brightness,
                0, 0, 0, 1, 0
        });
        photoEditorView.getSource().setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    private void applyContrastFilter(float contrast) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{
                contrast, 0f, 0f, 0f, 128f * (1 - contrast),
                0f, contrast, 0f, 0f, 128f * (1 - contrast),
                0f, 0f, contrast, 0f, 128f * (1 - contrast),
                0f, 0f, 0f, 1f, 0f
        });
        photoEditorView.getSource().setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    private void applyInvertFilter() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{
                -1f, 0f, 0f, 0f, 255f,
                0f, -1f, 0f, 0f, 255f,
                0f, 0f, -1f, 0f, 255f,
                0f, 0f, 0f, 1f, 0f
        });
        photoEditorView.getSource().setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    private Bitmap getBitmapFromPhotoEditorView() {
        // Kích hoạt cache và lấy bitmap
        photoEditorView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(photoEditorView.getDrawingCache());
        photoEditorView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void saveImage() {
        Bitmap bitmap = getBitmapFromPhotoEditorView();

        try {
            OutputStream outputStream;
            String imagePath = "";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, "edited_image_" + System.currentTimeMillis() + ".png");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Edit Image");

                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                outputStream = getContentResolver().openOutputStream(uri);
                imagePath = uri.toString();

            } else {
                File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "EditedImages");

                if(!storageDir.exists()){
                    storageDir.mkdirs();
                }

                String fileName = "edited_image_" + + System.currentTimeMillis() + ".png";
                File imageFile = new File(storageDir, fileName);
                outputStream = new FileOutputStream(imageFile);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(imageFile);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);
            }

            // Lưu bitmap vào file
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            String albumID = getIntent().getStringExtra("album_id");

            SQLiteDataBase dbHelper = new SQLiteDataBase(this);
            dbHelper.addImage(albumID, imagePath, "edit image");

            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Undo() {
        if (photoEditor != null) {
            photoEditor.undo();
            Toast.makeText(this, "Đã hoàn tác", Toast.LENGTH_SHORT).show();
        }
    }

    private void Redo() {
        if (photoEditor != null) {
            photoEditor.redo();
            Toast.makeText(this, "Đã làm lại", Toast.LENGTH_SHORT).show();
        }
    }
}