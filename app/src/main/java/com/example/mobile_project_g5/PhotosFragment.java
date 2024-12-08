package com.example.mobile_project_g5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

public class PhotosFragment extends Fragment {
    SQLiteDataBase sql;

    String[] dates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.photos_fragment, container, false);
        sql = new SQLiteDataBase(this.getContext());
        dates = sql.getMonthYear();

        ListView listViewItems = currentView.findViewById(R.id.list_layout);
        ImagesByDateAdapter imageAdapter = new ImagesByDateAdapter(this.getContext(), dates);
        listViewItems.setAdapter(imageAdapter);

        listViewItems.post(new Runnable() {
            @Override
            public void run() {
                listViewItems.setSelection(imageAdapter.getCount() - 1);
            }
        });

        return currentView;
    }
}
