package com.example.mobile_project_g5;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

//import com.bumptech.glide.Glide; // Thư viện tải hình ảnh

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ImageClass[] images;

    public ImageAdapter(Context context, ImageClass[] images) {
        this.context = context;
        this.images = images;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_image_layout, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        // Đặt đường dẫn của hình ảnh vào ImageView
        Uri imageUri = Uri.parse(images[position].getFilePath());
        // Sử dụng thư viện Glide để tải và hiển thị hình ảnh
        Glide.with(context)
                .load(imageUri)
                .into(imageView);// Sử dụng setImageResource với ID

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ImageDetailActivity.newIntent(context, images[position].getFilePath(), images[position].getInformation());
                intent.putExtra("image_path", images[position].getFilePath());
                intent.putExtra("image_info", images[position].getInformation());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}


