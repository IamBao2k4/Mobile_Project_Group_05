package com.example.mobile_project_g5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

public class PhotosFragment extends Fragment {
    SQLiteDataBase sql;

    ImageClass[] photos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.photos_fragment, container, false);
        sql = new SQLiteDataBase(this.getContext());
        photos = sql.getAllImages();

        GridView gridViewImages = currentView.findViewById(R.id.grid_layout);
        ImageAdapter imageAdapter = new ImageAdapter(this.getContext(), photos,"");
        gridViewImages.setAdapter(imageAdapter);

        gridViewImages.post(new Runnable() {
            @Override
            public void run() {
                gridViewImages.setSelection(imageAdapter.getCount() - 1);
            }
        });

        return currentView;
    }
}
