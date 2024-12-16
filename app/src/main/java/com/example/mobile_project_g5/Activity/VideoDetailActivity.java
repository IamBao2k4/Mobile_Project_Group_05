package com.example.mobile_project_g5.Activity;

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

import com.example.mobile_project_g5.Component.ImageClass;
import com.example.mobile_project_g5.R;
import com.example.mobile_project_g5.Helper.SQLiteDataBase;

public class VideoDetailActivity extends AppCompatActivity {

    private VideoView videoView;
    private String videoPath;
    private ImageClass image;
    private SQLiteDataBase sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_solo_layout);

        // Khởi tạo các view
        videoView = findViewById(R.id.videoView);
        ImageButton settingsButton = findViewById(R.id.settings_button);
        ImageButton favoriteButton = findViewById(R.id.favorite_button);
        ImageButton deleteButton = findViewById(R.id.delete_button);
        ImageButton restoreButton = findViewById(R.id.restore_button);
        ImageButton shareButton = findViewById(R.id.share_button);
        ImageButton editButton = findViewById(R.id.edit_button);
        ImageButton backBtn = findViewById(R.id.btnSoloBack);

        // Lấy đường dẫn video từ Intent
        videoPath = getIntent().getStringExtra("video_path");
        sql = new SQLiteDataBase(this);
        image = sql.getImageByPath(videoPath);

        if(image.getIsFavorite() == 1){
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else{
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        if(image.getActivate().equals("0"))
        {
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            restoreButton.setVisibility(View.VISIBLE);
            favoriteButton.setVisibility(View.GONE);
            shareButton.setVisibility(View.GONE);
        }
        else
        {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            restoreButton.setVisibility(View.GONE);
            favoriteButton.setVisibility(View.VISIBLE);
            shareButton.setVisibility(View.VISIBLE);
        }

        settingsButton.setVisibility(View.GONE);
        restoreButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);

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
        backBtn.setOnClickListener(v -> {
            finish();
        });

        favoriteButton.setOnClickListener(v -> {
            toggleFavorite(image, favoriteButton, sql);
        });

        shareButton.setOnClickListener(v -> shareVideo());
    }

    private void toggleFavorite(ImageClass image, ImageButton favoriteButton, SQLiteDataBase sql) {
        if(image.getIsFavorite() == 1){
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            image.setIsFavorite(0);
        }
        else{
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
            image.setIsFavorite(1);
        }
        sql.updateImage(image);
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
