package com.example.mobile_project_g5;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project_g5.ImageClass;
import com.example.mobile_project_g5.ImagesByDateAdapter;
import com.example.mobile_project_g5.SQLiteDataBase;

import java.util.Arrays;

public class PhotosFragment extends Fragment {
    SQLiteDataBase sql;
    String[] dates;
    ImageClass[] images;
    RecyclerView listViewItems;
    ImagesByDateAdapter imageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.photos_fragment, container, false);
        sql = new SQLiteDataBase(this.getContext());
        listViewItems = currentView.findViewById(R.id.list_layout);
        listViewItems.setLayoutManager(new LinearLayoutManager(this.getContext()));

        if (savedInstanceState != null) {
            dates = savedInstanceState.getStringArray("dates");
            images = (ImageClass[]) savedInstanceState.getParcelableArray("images");
        } else {
            dates = sql.getMonthYear();
            images = sql.getAllImages(); // Assuming you have a method to get all images
        }

        imageAdapter = new ImagesByDateAdapter(this.getContext(), dates, images);
        listViewItems.setAdapter(imageAdapter);
        //setRecyclerViewHeight(listViewItems, 1);

        return currentView;
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



    @Override
    public void onResume() {
        super.onResume();
        String[] newDates = sql.getMonthYear();
        ImageClass[] newImages = sql.getAllImages();
        if (!Arrays.equals(dates, newDates) || !Arrays.equals(images, newImages)) {
            dates = newDates;
            images = newImages;
            imageAdapter = new ImagesByDateAdapter(this.getContext(), dates, images);
            listViewItems.setAdapter(imageAdapter);
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putStringArray("dates", dates);
//        outState.putParcelableArray("images", images);
//    }
//
//    @Override
//    public void onViewStateRestored(Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState != null) {
//            dates = savedInstanceState.getStringArray("dates");
//            images = (ImageClass[]) savedInstanceState.getParcelableArray("images");
//        }
//    }
}