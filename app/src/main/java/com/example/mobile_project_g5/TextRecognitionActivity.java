package com.example.mobile_project_g5;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;

public class TextRecognitionActivity extends AppCompatActivity {

    private ImageView cameraImage;
    private Button copyTextBtn;
    private TextView resultText;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognition);

        // Ánh xạ view
        cameraImage = findViewById(R.id.cameraImage);
        copyTextBtn = findViewById(R.id.copyTextBtn);
        resultText = findViewById(R.id.resultText);
        backBtn = findViewById(R.id.backBtn);

        // Nhận đường dẫn hình ảnh từ Intent
        String imagePath = getIntent().getStringExtra("imagePath");

        if (imagePath != null) {
            if (imagePath.startsWith("android.resource://")) {
                // Tách tên tài nguyên từ đường dẫn
                String resourceName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
                if (resId != 0) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                    cameraImage.setImageBitmap(bitmap); // Hiển thị ảnh
                    recognizeText(bitmap); // Nhận diện văn bản
                } else {
                    Toast.makeText(this, "Image resource not found!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    cameraImage.setImageBitmap(bitmap); // Hiển thị ảnh lên ImageView
                    recognizeText(bitmap); // Bắt đầu OCR
                } else {
                    Toast.makeText(this, "Image file not found!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "No image provided!", Toast.LENGTH_SHORT).show();
        }

        backBtn.setOnClickListener(v -> finish());
    }

    private void recognizeText(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                .process(image)
                .addOnSuccessListener(ocrText -> {
                    resultText.setText(ocrText.getText());
                    resultText.setMovementMethod(new ScrollingMovementMethod());
                    copyTextBtn.setVisibility(Button.VISIBLE);
                    copyTextBtn.setOnClickListener(v -> {
                        ClipboardManager clipboard = ContextCompat.getSystemService(this, ClipboardManager.class);
                        ClipData clip = ClipData.newPlainText("recognized text", ocrText.getText());
                        if (clipboard != null) {
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(this, "Text copied", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to recognize text", Toast.LENGTH_SHORT).show()
                );
    }
}
