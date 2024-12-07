package com.example.mobile_project_g5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class AlbumItemAdapter extends BaseAdapter {

    private final Context context;
    private AlbumClass[] items;
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
        Glide
                .with(context)
                .load(items[position].getImages()[0].getFilePath())
                .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(25)))
                .into(imgBtn);
        TextView textView = convertView.findViewById(R.id.text_album);

        textView.setText(items[position].getAlbumName());

     
        ImageView deleteBtn = convertView.findViewById(R.id.img_hide);
        if (isEdit) {
            deleteBtn.setVisibility(View.VISIBLE);
        }
        else{
            deleteBtn.setVisibility(View.GONE);
        }

        deleteBtn.setOnClickListener(v -> {
            // Tạo AlertDialog xác nhận xóa
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xóa album");
            builder.setMessage("Bạn có chắc chắn muốn xóa album này?");

            builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Xử lý khi người dùng nhấn Xác nhận
                    SQLiteDataBase sql = new SQLiteDataBase(context);
                    try {
                        if (position >= 0 && position < items.length) {
                            sql.deleteAlbum(imgBtn.getContentDescription().toString());
                            items = sql.getAlbum();
                            notifyDataSetChanged();
                            Toast.makeText(context, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Không thể xóa album", Toast.LENGTH_SHORT).show();
                        }
                    } finally {
                        sql.close();
                    }
                }
            });

            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Đóng hộp thoại khi người dùng nhấn Hủy
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        imgBtn.setOnClickListener(v -> {
            // Gọi Activity để hiển thị hình ảnh trong album
            Intent intent = AlbumDetailActivity.newIntent(context, items[position], null);
            context.startActivity(intent);
        });

        return convertView;
    }
}
