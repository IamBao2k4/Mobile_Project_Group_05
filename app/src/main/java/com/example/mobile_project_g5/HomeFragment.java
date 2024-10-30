package com.example.mobile_project_g5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class HomeFragment extends Fragment {
    SQLiteDataBase sql;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.albums_fragment, container, false);
        sql = new SQLiteDataBase(this.getContext());
        AlbumClass[] albums = sql.getAlbum();

        GridView gridLayout = currentView.findViewById(R.id.grid_layout);
        AlbumItemAdapter adapter = new AlbumItemAdapter(this.getContext(), albums);
        gridLayout.setAdapter(adapter);

        return currentView;
    }
}
