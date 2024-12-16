package com.example.mobile_project_g5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mobile_project_g5.Activity.AlbumDetailActivity;
import com.example.mobile_project_g5.Component.AlbumClass;
import com.example.mobile_project_g5.Component.ImageClass;
import com.example.mobile_project_g5.Fragment.HomeFragment;
import com.example.mobile_project_g5.Fragment.PhotosFragment;
import com.example.mobile_project_g5.Fragment.VideoFragment;
import com.example.mobile_project_g5.Helper.IdentifyDuplicateImage;
import com.example.mobile_project_g5.Helper.ReadMediaFromExternalStorage;
import com.example.mobile_project_g5.Helper.SQLiteDataBase;
import com.example.mobile_project_g5.databinding.ActivityMainBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_MANAGE_STORAGE = 1000;
    private ActivityMainBinding binding;
    public Fragment selectedFragment;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context = this;
    SQLiteDataBase sql;
    ReadMediaFromExternalStorage readMediaFromExternalStorage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+ (API 33)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                // Nếu chưa được cấp quyền, yêu cầu quyền
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO},
                        REQUEST_CODE_MANAGE_STORAGE);
            } else {
                // Nếu đã có quyền, thực hiện hành động
                proceedWithFunctionality();
                Log.d("Storage Permission", "Media permissions granted");
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10+ (API 29)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Nếu chưa được cấp quyền, yêu cầu quyền
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_MANAGE_STORAGE);
            } else {
                // Nếu đã có quyền
                proceedWithFunctionality();
                Log.d("Storage Permission", "READ_EXTERNAL_STORAGE granted");
            }
        } else { // Android 9 và thấp hơn
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Nếu chưa được cấp quyền, yêu cầu quyền
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_MANAGE_STORAGE);
            } else {
                proceedWithFunctionality();
                Log.d("Storage Permission", "READ_EXTERNAL_STORAGE granted");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_MANAGE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Storage Permission", "Permission granted and play");
                proceedWithFunctionality();
            } else {
                Log.d("Storage Permission", "Permission denied");
            }
        }
    }

    private void proceedWithFunctionality() {
        // Chức năng bạn muốn thực hiện sau khi có quyền
        setContentView(R.layout.activity_main);
        sql = new SQLiteDataBase(context);
        readMediaFromExternalStorage = new ReadMediaFromExternalStorage(context);
        readMediaFromExternalStorage.loadImagesOnce();
        selectedFragment = new PhotosFragment();

        loadFragment(selectedFragment);

        sharedPreferences = getSharedPreferences("isDarkModeOn", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.contains("isDarkModeOn")) {
            boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
            setDarkLightMode(isDarkModeOn);
        } else {
            editor.putBoolean("isDarkModeOn", false);
            editor.apply();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onNavigationButtonClick(binding.getRoot());
    }

    //    Navigation button handle
    public void onNavigationButtonClick(View view) {
        binding.navButton.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (R.id.nav_photos == itemId) {
                selectedFragment = new PhotosFragment();
            } else if (R.id.nav_albums == itemId) {
                selectedFragment = new HomeFragment();
            } else if (R.id.nav_video == itemId) {
                selectedFragment = new VideoFragment();
            }
            else if (R.id.nav_settings == itemId) {
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
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void setAndChangeDarkLightMode(boolean isDarkModeOn) {
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("isDarkModeOn", false);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean("isDarkModeOn", true);
        }
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        prefs.edit().putBoolean("isLoaded", false).apply();
        readMediaFromExternalStorage = new ReadMediaFromExternalStorage(context);
        readMediaFromExternalStorage.loadImagesOnce();
    }
}
