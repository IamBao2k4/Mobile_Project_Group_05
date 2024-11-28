package com.example.mobile_project_g5;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

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

        if (!imagePath.isEmpty()) {
            Uri imageUri = Uri.parse(imagePath);
            // Sử dụng thư viện Glide để tải và hiển thị hình ảnh
            Glide.with(this)
                    .load(imageUri)
                    .override(photoEditorView.getWidth(), photoEditorView.getHeight()) // Đảm bảo ảnh khớp với View
                    .into(photoEditorView.getSource());// Sử dụng setImageResource với ID
        } else {
            String defaultPath = "android.resource://com.example.mobile_project_g5/drawable/so5";
            // Optionally set a default image
            Glide.with(this)
                    .load(Uri.parse(defaultPath))
                    .override(photoEditorView.getWidth(), photoEditorView.getHeight()) // Đảm bảo ảnh khớp với View
                    .into(photoEditorView.getSource());// Sử dụng setImageResource với ID
        }
        photoEditorView.getSource().setScaleType(ImageView.ScaleType.FIT_XY); // Đặt scaleType phù hợp

        Button backButton = findViewById(R.id.btnSoloBack);
        backButton.setOnClickListener(v -> finish());


        ImageButton btnAddText = findViewById(R.id.btnAddText);
        ImageButton btnDraw = findViewById(R.id.btnDraw);
        ImageButton btnFilter = findViewById(R.id.btnFilter);
        ImageButton btnUndo = findViewById(R.id.btnUndo);
        ImageButton btnRedo = findViewById(R.id.btnRedo);

        btnAddText.setOnClickListener(v -> addTextToImage());
        btnDraw.setOnClickListener(v -> enableDrawing());
        btnFilter.setOnClickListener(v -> applyFilter());
        btnUndo.setOnClickListener(v -> Undo());
        btnRedo.setOnClickListener(v -> Redo());

    }

    private void addTextToImage() {
        // Hiển thị dialog để nhập văn bản
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập văn bản");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String text = input.getText().toString();
            if (!text.isEmpty()) {
                // Thêm văn bản vào ảnh
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

    private void applyFilter() {

    }

    private void Undo(){
        photoEditor.undo();
    }

    private void Redo() {
        photoEditor.redo();
    }
}