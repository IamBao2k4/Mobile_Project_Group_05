package com.example.mobile_project_g5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class AlbumItemAdapter extends RecyclerView.Adapter<AlbumItemAdapter.ViewHolder> {

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlbumClass album = items[position];
        holder.imgBtn.setContentDescription(album.getAlbumID());
        if (album.getImages().length > 0) {
            Glide.with(context)
                    .load(album.getImages()[0].getFilePath())
                    .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(25)))
                    .into(holder.imgBtn);
        } else {
            holder.imgBtn.setImageResource(R.drawable.ic_baseline_folder_24);
        }
        holder.textView.setText(album.getAlbumName());

        if (isEdit) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            holder.deleteBtn.setVisibility(View.GONE);
        }

        holder.deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xóa album");
            builder.setMessage("Bạn có chắc chắn muốn xóa album này?");

            builder.setPositiveButton("Xác nhận", (dialog, which) -> {
                try (SQLiteDataBase sql = new SQLiteDataBase(context)) {
                    if (position < items.length) {
                        sql.deleteAlbum(holder.imgBtn.getContentDescription().toString());
                        items = sql.getAlbum();
                        notifyDataSetChanged();
                        Toast.makeText(context, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Không thể xóa album", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        holder.imgBtn.setOnClickListener(v -> {
            Intent intent = AlbumDetailActivity.newIntent(context, album, null);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imgBtn;
        TextView textView;
        ImageView deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBtn = itemView.findViewById(R.id.img_album);
            textView = itemView.findViewById(R.id.text_album);
            deleteBtn = itemView.findViewById(R.id.img_hide);
        }
    }
}