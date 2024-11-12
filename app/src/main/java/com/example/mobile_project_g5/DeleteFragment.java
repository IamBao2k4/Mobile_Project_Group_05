package com.example.mobile_project_g5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DeleteFragment extends Fragment {
    SQLiteDataBase sql;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.deleteed_fragment, container, false);
        sql = new SQLiteDataBase(getContext());
        ImageClass[] images = sql.getDeletedImage();
        ImageAdapter adapter = new ImageAdapter(getContext(), images, "deleted");
        GridView gridLayout = currentView.findViewById(R.id.deleted_grid);
        gridLayout.setAdapter(adapter);

        Button backBtn = currentView.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.default_fragment, new HomeFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return currentView;
    }
}
