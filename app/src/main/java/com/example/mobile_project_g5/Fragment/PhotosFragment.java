package com.example.mobile_project_g5.Fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project_g5.Component.ImageClass;
import com.example.mobile_project_g5.Adapter.ImagesByDateAdapter;
import com.example.mobile_project_g5.Helper.SQLiteDataBase;
import com.example.mobile_project_g5.R;

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

        return currentView;
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
}