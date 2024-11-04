package com.example.mobile_project_g5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class HomeFragment extends Fragment {
    SQLiteDataBase sql;
    AlbumClass[] albums;

    private boolean isEdit = false;
    private String newAlbumName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.albums_fragment, container, false);
        sql = new SQLiteDataBase(this.getContext());
        albums = sql.getAlbum();

        GridView gridLayout = currentView.findViewById(R.id.grid_layout);
        AlbumItemAdapter adapter = new AlbumItemAdapter(this.getContext(), albums);
        gridLayout.setAdapter(adapter);

        Button editBtn = currentView.findViewById(R.id.edit_btn);

        ImageButton addBtn = currentView.findViewById(R.id.add_btn);

        editBtn.setOnClickListener(v -> {
            isEdit = !isEdit;
            if (isEdit) {
                adapter.setEditMode(isEdit);
                editBtn.setText("Done");
                // Thực hiện các thao tác chỉnh sửa ở đây
                addBtn.setVisibility(View.VISIBLE);
            } else {
                adapter.setEditMode(isEdit);
                editBtn.setText("Edit");
                addBtn.setVisibility(View.GONE);
            }
        });

        addBtn.setOnClickListener(view -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(currentView.getContext());

            // LayoutInflater add_inflater = getLayoutInflater();
            View dialogView = getLayoutInflater().inflate(R.layout.new_ablums_layout, null);
            builder.setView(dialogView);

            EditText albumNameInput = dialogView.findViewById(R.id.albumNameInput);
            Button cancelButton = dialogView.findViewById(R.id.cancelButton);
            Button saveButton = dialogView.findViewById(R.id.saveButton);

            AlertDialog dialog = builder.create();
            dialog.show();

            albumNameInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // No action needed here
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    saveButton.setEnabled(charSequence.length() > 0);
                    saveButton.setTextColor(charSequence.length() > 0 ? Color.parseColor("#007AFF") : Color.GRAY);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // No action needed here
                }
            });

            cancelButton.setOnClickListener(v ->
                    dialog.dismiss()
            );

            saveButton.setOnClickListener(v -> {
                String newAlbumName = albumNameInput.getText().toString();
                boolean albumExists = false;
                for (AlbumClass album : albums) {
                    if (album.getAlbumName().equals(newAlbumName)) {
                        Toast.makeText(currentView.getContext(), "Tên album đã tồn tại", Toast.LENGTH_SHORT).show();
                        albumExists = true;
                        break;
                    }
                }
                if (!albumExists) {
                    AlbumClass album = new AlbumClass(newAlbumName, "", newAlbumName, new ImageClass[]{});
                    sql.addAlbum(album.getAlbumName(), album.getInformation());
                    albums = sql.getAlbum();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(currentView.getContext(), "Thêm album thành công", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            });

        });


        return currentView;
    }
}
