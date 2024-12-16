package com.example.mobile_project_g5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class VideoFragment extends Fragment {
    SQLiteDataBase sql;
    ImageClass[] videos;
    RecyclerView listViewItems;
    ImageAdapter videoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.video_fragment, container, false);
        sql = new SQLiteDataBase(this.getContext());
        listViewItems = currentView.findViewById(R.id.list_layout);
        listViewItems.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        videos = sql.getAllVideos();
        videoAdapter = new ImageAdapter(this.getContext(), videos, "video");
        listViewItems.setAdapter(videoAdapter);

        return currentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageClass[] newVideos = sql.getAllVideos();
        if (!Arrays.equals(videos, newVideos)) {
            videos = newVideos;
            videoAdapter = new ImageAdapter(this.getContext(), videos, "video");
            listViewItems.setAdapter(videoAdapter);
        }
    }
}
