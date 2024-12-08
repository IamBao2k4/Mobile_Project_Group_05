package com.example.mobile_project_g5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.mobile_project_g5.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public Fragment selectedFragment = new PhotosFragment();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context = this;
    SQLiteDataBase sql;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        loadFragment(selectedFragment);

        sharedPreferences = getSharedPreferences("isDarkModeOn", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(sharedPreferences.contains("isDarkModeOn")){
            boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
            setDarkLightMode(isDarkModeOn);
        }
        else {
            editor.putBoolean("isDarkModeOn", false);
            editor.apply();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onNavigationButtonClick(binding.getRoot());
    };

//    Navigation button handle
    public void onNavigationButtonClick(View view) {
        binding.navButton.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (R.id.nav_photos == itemId) {
                selectedFragment = new PhotosFragment();
            } else if (R.id.nav_albums == itemId) {
                 selectedFragment = new HomeFragment();
            } else if (R.id.nav_settings == itemId) {
                showPopupMenu(findViewById(R.id.nav_settings));
                return false; // Don't load fragment
            }
            if (selectedFragment != null)
                loadFragment(selectedFragment);
            return true;
        });
    }

    //    Load Fragment
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.default_fragment, fragment)
                .commit();
    }

    //    Show Popup Menu
    private void showPopupMenu(View anchor) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_menu_layout, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        NavigationView navigationView = bottomSheetView.findViewById(R.id.navigation_view);
        navigationView.inflateMenu(R.menu.setting_menu);

        navigationView.setNavigationItemSelectedListener(item -> {
                if(item.getItemId() == R.id.dark_light_mode) {
                    if (sharedPreferences.contains("isDarkModeOn")) {
                        boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
                        setAndChangeDarkLightMode(isDarkModeOn);
                    }
                }
                else if (item.getItemId() == R.id.favorite_album) {
//                        selectedFragment = new FavoriteFragment();
//                        loadFragment(selectedFragment);
                        sql = new SQLiteDataBase(context);
                        ImageClass[] images = sql.getFavoriteImages();
                        AlbumClass favoriteAlbum = new AlbumClass("Favorite Album", "-1", null, images);
                        Intent intent = AlbumDetailActivity.newIntent(context,favoriteAlbum, null);
                        context.startActivity(intent);
                }
                else if (item.getItemId() == R.id.deleted_album) {
                    sql = new SQLiteDataBase(context);
                    ImageClass[] images = sql.getDeletedImage();
                    AlbumClass deletedAlbum = new AlbumClass("Deleted Album", "-1", null, images);
                    Intent intent = AlbumDetailActivity.newIntent(context,deletedAlbum, null);
                    context.startActivity(intent);
//                    selectedFragment = new DeleteFragment();
//                    loadFragment(selectedFragment);
                }
                else if (item.getItemId() == R.id.action_option2) {
                    IdentifyDuplicateImage identifyDuplicateImage = new IdentifyDuplicateImage(context);
                    Map<Integer, List<ImageClass>> groups = identifyDuplicateImage.GroupDuplicateImages();
                    AlbumClass duplicate = identifyDuplicateImage.toAlbumClass(groups);
                    Intent intent = AlbumDetailActivity.newIntent(context, duplicate, groups);
                    context.startActivity(intent);
                }
            bottomSheetDialog.dismiss();
            return true;
        });

        bottomSheetDialog.show();
    }

    public void setDarkLightMode(boolean isDarkModeOn) {
        if(isDarkModeOn)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void setAndChangeDarkLightMode(boolean isDarkModeOn) {
        if(isDarkModeOn)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("isDarkModeOn",false);
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean("isDarkModeOn",true);
        }
        editor.apply();
    }
}