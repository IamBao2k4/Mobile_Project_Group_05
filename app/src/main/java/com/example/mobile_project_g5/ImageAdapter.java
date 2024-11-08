package com.example.mobile_project_g5;

import static com.example.mobile_project_g5.AlbumDetailActivity.REQUEST_CODE_DELETE_IMAGE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

//import com.bumptech.glide.Glide; // Thư viện tải hình ảnh

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ImageClass[] images;
    private String type;
    private List<ImageClass> images_chosen;
    private boolean isDelete;

    public ImageAdapter(Context context, ImageClass[] images, String type) {
        this.context = context;
        this.images = images;
        this.type =type;
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
        ImageView select = convertView.findViewById(R.id.select);
        if(type.equals("add")){
            select.setImageResource(R.drawable.ic_baseline_check_24);
            select.setVisibility(View.VISIBLE);
        }
        // Đặt đường dẫn của hình ảnh vào ImageView
        Uri imageUri = Uri.parse(images[position].getFilePath());
        // Sử dụng thư viện Glide để tải và hiển thị hình ảnh
        Glide.with(context)
                .load(imageUri)
                .into(imageView);// Sử dụng setImageResource với ID
        images_chosen = new ArrayList<>();
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
                Intent intent = ImageDetailActivity.newIntent(context, images[position].getFilePath(), images[position].getInformation());
                intent.putExtra("image_path", images[position].getFilePath());
                intent.putExtra("image_info", images[position].getInformation());
                context.startActivity(intent);
                ((Activity) context).startActivityForResult(intent, REQUEST_CODE_DELETE_IMAGE);
            }
        });

        return convertView;
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
}


