package com.example.mobile_project_g5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.core.util.Pair;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;

public class ImagesByDateAdapter extends BaseAdapter {
    protected Context context;
    private String[] dates;
    private ImageClass[] images = new ImageClass[0];

    public ImagesByDateAdapter(Context context, String[] dates) {
        this.context = context;
        this.dates = dates;
    }

    @Override
    public int getCount() {
        return dates.length;
    }

    @Override
    public Object getItem(int position) {
        return dates[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.images_by_date_layout, parent, false);
        }

        TextView date = convertView.findViewById(R.id.date);
        date.setText(dates[position]);

        SQLiteDataBase sql = new SQLiteDataBase(context);
        images = sql.getImagesByMonthYear(dates[position]);

        GridView gridViewImages = convertView.findViewById(R.id.grid_view_images);
        ImageAdapter imageAdapter = new ImageAdapter(context, images, "");
        gridViewImages.setAdapter(imageAdapter);

        setGridViewHeight(gridViewImages);

        return convertView;
    }

    public static void setGridViewHeight(GridView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = (int) Math.ceil((double) items / 2);

        for (int i = 0; i < rows; i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        int verticalSpacing = gridView.getVerticalSpacing();
        params.height = totalHeight + (verticalSpacing * (rows - 1));
        gridView.setLayoutParams(params);
    }
}
