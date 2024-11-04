package com.example.mobile_project_g5;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumItemAdapter extends BaseAdapter {

    private final Context context;
    private final AlbumClass[] items;
    private boolean isEdit = false;


    public AlbumItemAdapter(Context context, AlbumClass[] items) {
        this.context = context;
        this.items = items;
    }
    public void setEditMode(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
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

        ImageButton imgBtn = convertView.findViewById(R.id.img_album);
        imgBtn.setContentDescription(items[position].getAlbumID());
        TextView textView = convertView.findViewById(R.id.text_album);

        textView.setText(items[position].getAlbumName());


     
        ImageView deleteBtn = convertView.findViewById(R.id.img_hide);
        if (isEdit) {
            deleteBtn.setVisibility(View.VISIBLE);
        }
        else{
            deleteBtn.setVisibility(View.GONE);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDataBase db = new SQLiteDataBase(context);
                String albumId = imgBtn.getContentDescription().toString();
                db.deleteAlbum(albumId);
                Toast.makeText(context, "Delete album success", Toast.LENGTH_SHORT).show();
            }
        });

        imgBtn.setOnClickListener(v -> {
            // Gọi Activity để hiển thị hình ảnh trong album
            Intent intent = AlbumDetailActivity.newIntent(context, items[position]);
            //Toast.makeText(context, imgBtn.getContentDescription(), Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
        });

        return convertView;
    }
}
