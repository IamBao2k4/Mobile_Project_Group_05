package com.example.mobile_project_g5.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mobile_project_g5.Activity.ImageDetailActivity;
import com.example.mobile_project_g5.Activity.VideoDetailActivity;
import com.example.mobile_project_g5.Component.ImageClass;
import com.example.mobile_project_g5.R;
import com.example.mobile_project_g5.Helper.SQLiteDataBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    protected Context context;
    protected ImageClass[] images;
    protected String type;
    protected List<ImageClass> images_chosen = new ArrayList<>();
    public boolean isEdit = false;

    public ImageAdapter(Context context, ImageClass[] images, String type) {
        this.context = context;
        this.images = images;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageClass image = images[position];
        String fileType = image.getType();

        if (type.equals("add")) {
            holder.select.setImageResource(R.drawable.ic_baseline_check_24);
            holder.select.setVisibility(View.VISIBLE);
            this.isEdit = true;
        }

        if (fileType.equals("image")) {
            holder.videoIcon.setVisibility(View.GONE);
            holder.videoDuration.setVisibility(View.GONE);
            Glide.with(context)
                    .load(image.getFilePath())
                    .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(25)))
                    .into(holder.imageView);
        } else if (fileType.equals("video")) {
            holder.videoIcon.setVisibility(View.VISIBLE);
            holder.videoDuration.setVisibility(View.VISIBLE);
            String resourcePath = image.getFilePath();
            Bitmap thumbnail = getVideoThumbnail(resourcePath);
            Glide.with(context)
                    .load(thumbnail)
                    .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(25)))
                    .into(holder.imageView);
            String durationStr = getVideoDuration(resourcePath);
            if (durationStr != null) {
                long duration = Long.parseLong(durationStr);
                String formattedDuration = formatDuration(duration);
                holder.videoDuration.setText(formattedDuration);
            }
        }

        if (isEdit) {
            holder.select.setVisibility(View.VISIBLE);
        } else {
            holder.select.setVisibility(View.GONE);
        }

        holder.select.setOnClickListener(view -> {
            holder.select.setColorFilter(Color.BLUE);
            if (Objects.equals(type, "add")) {
                if (images_chosen.contains(image)) {
                    images_chosen.remove(image);
                    return;
                }
                images_chosen.add(image);
                return;
            }
            showDeleteDialog(position);
            holder.select.setColorFilter(Color.RED);
        });

        holder.imageView.setOnClickListener(v -> {
            if (image.getType().equals("image")) {
                Intent intent = ImageDetailActivity.newIntent(context, image.getFilePath(), image.getInformation(), type, image.getAlbumID());
                context.startActivity(intent);
            } else if (image.getType().equals("video")) {
                Intent intent = VideoDetailActivity.newIntent(context, image.getFilePath(), image.getInformation());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public List<ImageClass> getImages_chosen() {
        return images_chosen;
    }

    private Bitmap getVideoThumbnail(String resourcePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (resourcePath.contains("android.resource")) {
            String resourceName = resourcePath.substring(resourcePath.lastIndexOf("/") + 1);
            @SuppressLint("DiscouragedApi") int resId = context.getResources().getIdentifier(resourceName, "raw", context.getPackageName());
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resId);
            retriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            return retriever.getFrameAtTime(0);
        } else {
            retriever.setDataSource(resourcePath);
            return retriever.getFrameAtTime(0);
        }
    }

    private String getVideoDuration(String resourcePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (resourcePath.contains("android.resource")) {
            String resourceName = resourcePath.substring(resourcePath.lastIndexOf("/") + 1);
            @SuppressLint("DiscouragedApi") int resId = context.getResources().getIdentifier(resourceName, "raw", context.getPackageName());
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resId);
            retriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } else {
            retriever.setDataSource(resourcePath);
            return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        }
    }

    protected String formatDuration(long durationMillis) {
        long seconds = (durationMillis / 1000) % 60;
        long minutes = (durationMillis / (1000 * 60)) % 60;
        long hours = durationMillis / (1000 * 60 * 60);

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa ảnh");
        builder.setMessage("Bạn có chắc chắn muốn xóa ảnh này?");
        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            try (SQLiteDataBase sql = new SQLiteDataBase(context)) {
                if (position < images.length) {
                    sql.deleteImage(String.valueOf(images[position].getImageID()));
                    images = sql.getImagesByAlbumId(images[0].getAlbumID());
                    notifyDataSetChanged();
                    Toast.makeText(context, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Không thể xóa ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void removeImage(String imagePath) {
        int length = images.length;
        ImageClass[] newImages = new ImageClass[length - 1];
        int index = 0;

        for (int i = 0; i < length; i++) {
            if (!images[i].getFilePath().equals(imagePath)) {
                newImages[index++] = images[i];
            }
        }
        images = newImages;
        notifyDataSetChanged();
    }

    public void setEditMode() {
        this.isEdit = !this.isEdit;
        notifyDataSetChanged();
    }

    public void addImage(String restoredImagePath) {
        int length = images.length;
        ImageClass[] newImages = new ImageClass[length + 1];
        System.arraycopy(images, 0, newImages, 0, length);
        SQLiteDataBase dbHelper = new SQLiteDataBase(context);
        ImageClass image = dbHelper.getImageByPath(restoredImagePath);
        newImages[length] = new ImageClass(
                image.getImageID(),
                image.getAlbumID(),
                image.getFilePath(),
                image.getInformation(),
                image.getIsFavorite(),
                image.getExifDatetime(),
                image.getActivate(),
                image.getIsSelected(),
                image.getDeleteAt(),
                image.getType());
        images = newImages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageView videoIcon;
        public TextView videoDuration;
        public ImageView select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            videoIcon = itemView.findViewById(R.id.video_icon);
            videoDuration = itemView.findViewById(R.id.video_duration);
            select = itemView.findViewById(R.id.select);
        }
    }
}