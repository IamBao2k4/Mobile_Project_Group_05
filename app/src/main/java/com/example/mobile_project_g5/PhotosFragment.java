package com.example.mobile_project_g5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PhotosFragment extends Fragment {
    SQLiteDataBase sql;

    ImageClass[] photos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       //ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.photos_fragment, container, false);
        ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.activity_album_detail, container, false);
        Button editBtn = currentView.findViewById(R.id.edit_btn);
        ImageButton addBtn = currentView.findViewById(R.id.add_btn);
        TextView albumName = currentView.findViewById(R.id.album_name);
        Button backbtn = currentView.findViewById(R.id.back_btn);
        editBtn.setVisibility(View.GONE);
        addBtn.setVisibility(View.GONE);
        backbtn.setVisibility(View.GONE);
        albumName.setText("Photos");
        sql = new SQLiteDataBase(this.getContext());
        photos = sql.getAllImages();

        //GridView gridViewImages = currentView.findViewById(R.id.grid_layout);
        GridView gridViewImages = currentView.findViewById(R.id.grid_view_images);

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
