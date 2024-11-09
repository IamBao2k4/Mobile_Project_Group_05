package com.example.mobile_project_g5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FavoriteFragment extends Fragment {
    SQLiteDataBase sql;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.favorite_fragment, container, false);
        sql = new SQLiteDataBase(getContext());
        ImageClass[] images = sql.getFavoriteImages();
        ImageAdapter adapter = new ImageAdapter(getContext(), images, "favorite");
        GridView gridLayout = currentView.findViewById(R.id.grid_layout);
        gridLayout.setAdapter(adapter);
        
        return currentView;
    }
}
