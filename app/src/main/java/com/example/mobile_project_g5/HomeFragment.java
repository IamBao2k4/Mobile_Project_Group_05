package com.example.mobile_project_g5;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


public class HomeFragment extends Fragment {

    private String[] items = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6","Item 7", "Item 8", "Item 9", "Item 10"};
    private int images = R.drawable.ic_baseline_folder_24;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.albums_fragment, container, false);

        GridView gridLayout = currentView.findViewById(R.id.grid_layout);
        AlbumItemAdapter adapter = new AlbumItemAdapter(this.getContext(), items, images);
        gridLayout.setAdapter(adapter);

        return currentView;
    }
}
