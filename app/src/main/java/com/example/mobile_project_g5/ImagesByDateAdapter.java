package com.example.mobile_project_g5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImagesByDateAdapter extends RecyclerView.Adapter<ImagesByDateAdapter.ViewHolder> {
    private final Context context;
    private final String[] dates;
    private ImageClass[] images = new ImageClass[0];

    public ImagesByDateAdapter(Context context, String[] dates, ImageClass[] images) {
        this.context = context;
        this.dates = dates;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.images_by_date_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(dates[position]);

        SQLiteDataBase sql = new SQLiteDataBase(context);
        images = sql.getImagesByMonthYear(dates[position]);

        ImageAdapter imageAdapter = new ImageAdapter(context, images, "");
        holder.gridViewImages.setLayoutManager(new GridLayoutManager(context, 2));
        holder.gridViewImages.setAdapter(imageAdapter);

        //setRecyclerViewHeight(holder.gridViewImages, 2);
    }

    @Override
    public int getItemCount() {
        return dates.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public RecyclerView gridViewImages;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            gridViewImages = itemView.findViewById(R.id.grid_view_images);
        }
    }

    public static void setRecyclerViewHeight(RecyclerView recyclerView, int columns) {
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter != null) {
                    int totalHeight = 0;
                    for (int i = 0; i < adapter.getItemCount() / columns; i++) {
                        View item = recyclerView.getLayoutManager().findViewByPosition(i);
                        if (item != null) {
                            totalHeight += item.getMeasuredHeight();
                        }
                    }
                    ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                    params.height = totalHeight;
                    recyclerView.setLayoutParams(params);
                }
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}