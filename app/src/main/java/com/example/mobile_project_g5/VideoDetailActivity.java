package com.example.mobile_project_g5;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoDetailActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageButton shareButton;
    private String videoPath;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_solo_layout);

        // Khởi tạo các view
        videoView = findViewById(R.id.videoView);
        shareButton = findViewById(R.id.share_button);
        back = findViewById(R.id.btnSoloBack);
        // Lấy đường dẫn video từ Intent
        videoPath = getIntent().getStringExtra("video_path");

        // Thiết lập VideoView
        MediaController mediaController = new MediaController(this);
        if (videoPath != null) {
            Uri videoUri = Uri.parse(videoPath);
            videoView.setVideoURI(videoUri);

            // Thêm MediaController để phát và dừng video
            mediaController.setAnchorView(videoView); // Gắn MediaController vào VideoView
            mediaController.setVisibility(View.VISIBLE);
            videoView.setMediaController(mediaController);

            videoView.setOnPreparedListener(mp -> {
                // Tự động chạy video khi được tải
                videoView.start();
            });
        }
        // Xử lý khi nhấn vào VideoView
        LinearLayout actionButtonsLayout = findViewById(R.id.action_buttons_layout);
        videoView.setOnTouchListener((v, event) -> {
            if (mediaController.isShowing()) {
                actionButtonsLayout.setVisibility(View.GONE); // Ẩn action_buttons_layout
            } else {
                actionButtonsLayout.setVisibility(View.VISIBLE); // Hiển thị action_buttons_layout
            }
            return false; // Trả về false để sự kiện touch không bị chặn
        });
        back.setOnClickListener(v -> {
            finish();
        });

        shareButton.setOnClickListener(v -> shareVideo());
    }

    private void shareVideo() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        Uri videoUri = Uri.parse(videoPath);
        shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
        startActivity(Intent.createChooser(shareIntent, "Share video using"));
    }

    // Phương thức tạo Intent cho activity này
    public static Intent newIntent(Context context, String videoPath, String information) {
        Intent intent = new Intent(context, VideoDetailActivity.class);
        intent.putExtra("video_path", videoPath);
        intent.putExtra("information", information);
        return intent;
    }
}
