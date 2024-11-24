package com.example.mobile_project_g5;

import static com.example.mobile_project_g5.AlbumDetailActivity.REQUEST_CODE_DELETE_IMAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;


import java.util.ArrayList;
import java.util.List;

//import com.bumptech.glide.Glide; // Thư viện tải hình ảnh

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ImageClass[] images;
    private String type;
    private List<ImageClass> images_chosen;
    public boolean isEdit = false;

    public ImageAdapter(Context context, ImageClass[] images, String type) {
        this.context = context;
        this.images = images;
        this.type = type;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public  List<ImageClass> getImages_chosen() { return images_chosen; }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_image_layout, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        ImageView videoIcon = convertView.findViewById(R.id.video_icon);
        TextView videoDuration = convertView.findViewById(R.id.video_duration);
        ImageView select = convertView.findViewById(R.id.select);
        if(type.equals("add")){
            select.setImageResource(R.drawable.ic_baseline_check_24);
            select.setVisibility(View.VISIBLE);
            this.isEdit = true;
        }
        // Kiểm tra loại tệp
        String fileType = images[position].getType(); // Thêm phương thức getType() vào model

        if (fileType.equals("image")) {
            videoIcon.setVisibility(View.GONE); // Ẩn biểu tượng video
            videoDuration.setVisibility(View.GONE); // Ẩn thời lượng video
            Uri imageUri = Uri.parse(images[position].getFilePath());
            Glide.with(context)
                    .load(imageUri)
                    .into(imageView);
        }
        else if (fileType.equals("video")) {
            videoIcon.setVisibility(View.VISIBLE); // Hiển thị biểu tượng video
            videoDuration.setVisibility(View.VISIBLE); // Hiển thị thời lượng video
            String resourcePath = images[position].getFilePath();
            String resourceName = resourcePath.substring(resourcePath.lastIndexOf("/") + 1);
            @SuppressLint("DiscouragedApi") int resId = context.getResources().getIdentifier(resourceName, "raw", context.getPackageName());
            // Lấy thumbnail video
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resId);
            retriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            Bitmap thumbnail = retriever.getFrameAtTime(0); // Lấy frame đầu tiên
            imageView.setImageBitmap(thumbnail);

            // Lấy thời lượng video
            retriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                afd.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (durationStr != null) {
                long duration = Long.parseLong(durationStr);
                String formattedDuration = formatDuration(duration);
                videoDuration.setText(formattedDuration);
            }

        }
        if(isEdit){
            select.setVisibility(View.VISIBLE);
        }
        else{
            select.setVisibility(View.GONE);
        }

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select.setColorFilter(Color.BLUE);
                images_chosen.add(images[position]);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (images[position].getType().equals("image")) {
                    Intent intent = ImageDetailActivity.newIntent(context, images[position].getFilePath(), images[position].getInformation(), type, images[position].getAlbumID());
                    intent.putExtra("image_path", images[position].getFilePath());
                    intent.putExtra("image_info", images[position].getInformation());
                    intent.putExtra("album_id", images[position].getAlbumID());
                    context.startActivity(intent);
                    //((Activity) context).startActivityForResult(intent, REQUEST_CODE_DELETE_IMAGE);
                }
                if (images[position].getType().equals("video")) {
                    Intent intent = VideoDetailActivity.newIntent(context, images[position].getFilePath(), images[position].getInformation());
                    intent.putExtra("image_path", images[position].getFilePath());
                    intent.putExtra("image_info", images[position].getInformation());
                    context.startActivity(intent);
                    //((Activity) context).startActivityForResult(intent, REQUEST_CODE_DELETE_IMAGE);
                }
            }
        });

        return convertView;
    }

    // Hàm định dạng thời lượng video
    private String formatDuration(long durationMillis) {
        long seconds = (durationMillis / 1000) % 60;
        long minutes = (durationMillis / (1000 * 60)) % 60;
        long hours = durationMillis / (1000 * 60 * 60);

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
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
        // VideoView, MediaController
        images = newImages;
        notifyDataSetChanged();
    }

    public void setEditMode(){
        this.isEdit = !this.isEdit;
        notifyDataSetChanged();
    }

    public void addImage(String restoredImagePath) {
        int length = images.length;
        ImageClass[] newImages = new ImageClass[length + 1];
        for (int i = 0; i < length; i++) {
            newImages[i] = images[i];
        }
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
}


