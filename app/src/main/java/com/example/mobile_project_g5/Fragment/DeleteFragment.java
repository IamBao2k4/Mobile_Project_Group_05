package com.example.mobile_project_g5.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project_g5.Adapter.ImageAdapter;
import com.example.mobile_project_g5.Component.ImageClass;
import com.example.mobile_project_g5.Helper.SQLiteDataBase;
import com.example.mobile_project_g5.Fragment.HomeFragment;
import com.example.mobile_project_g5.R;

public class DeleteFragment extends Fragment {
    SQLiteDataBase sql;
    ImageAdapter adapter;
    ImageClass[] images;
    RecyclerView gridLayout;
    ViewGroup currentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        currentView = (ViewGroup) inflater.inflate(R.layout.deleteed_fragment, container, false);
        sql = new SQLiteDataBase(getContext());
        images = sql.getDeletedImage();
        adapter = new ImageAdapter(getContext(), images, "deleted");
        gridLayout = currentView.findViewById(R.id.deleted_grid);
        gridLayout.setLayoutManager(new GridLayoutManager(getContext(), 2));
        gridLayout.setAdapter(adapter);

        ImageButton backBtn = currentView.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.default_fragment, new HomeFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return currentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        images = sql.getDeletedImage();
        adapter = new ImageAdapter(getContext(), images, "deleted");
        gridLayout = currentView.findViewById(R.id.deleted_grid);
        gridLayout.setAdapter(adapter);
    }
}
