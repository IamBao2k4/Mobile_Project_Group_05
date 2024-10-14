package com.example.mobile_project_g5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumItemAdapter extends BaseAdapter {
    private Context context;
    private String[] items;
    private int images;

    public AlbumItemAdapter(Context context, String[] items, int images) {
        this.context = context;
        this.items = items;
        this.images = images;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_album_layout, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.img_album);
        TextView textView = convertView.findViewById(R.id.text_album);

        imageView.setImageResource(images);
        textView.setText(items[position]);

        return convertView;
    }
}
