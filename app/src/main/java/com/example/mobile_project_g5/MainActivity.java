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

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public Fragment selectedFragment = new HomeFragment();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context = this;
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
            if (R.id.nav_album == itemId) {
                selectedFragment = new HomeFragment();
            } else if (R.id.nav_camera == itemId) {
                 selectedFragment = new PhotosFragment();
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
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.setting_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.dark_light_mode)
                {
                    if(sharedPreferences.contains("isDarkModeOn"))
                    {
                        boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
                        setAndChangeDarkLightMode(isDarkModeOn);
                    }
                }
                else if(item.getItemId() == R.id.favorite_album)
                {
                    selectedFragment = new FavoriteFragment();
                    loadFragment(selectedFragment);
                    IdentifyDuplicateImage identifyDuplicateImage = new IdentifyDuplicateImage(context);
                    AlbumDetailActivity albumDetailActivity = new AlbumDetailActivity();
                    Map<Integer, List<ImageClass>> groups = identifyDuplicateImage.GroupDuplicateImages();
                    AlbumClass duplicate = identifyDuplicateImage.toAlbumClass(groups);
                    Intent intent = albumDetailActivity.newIntent(context, duplicate,groups);
                    context.startActivity(intent);
                }
                else if(item.getItemId() == R.id.deleted_album)
                {
                    selectedFragment = new DeleteFragment();
                    loadFragment(selectedFragment);
                }
                return true;
            }
        });
        popupMenu.show();
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