package com.example.mobile_project_g5;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.mobile_project_g5.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public HomeFragment selectedFragment = new HomeFragment();
    BottomNavigationView bottomNavigationView = findViewById(R.id.nav_button);
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        loadFragment(selectedFragment);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (R.id.nav_album == itemId) {
                selectedFragment = new HomeFragment();
            /*} else if (R.id.nav_camera == itemId) {
                selectedFragment = new cameraFragment();*/
            } else if (R.id.nav_settings == itemId) {
                    showPopupMenu(findViewById(R.id.nav_settings));
                    return false; // Không chuyển Fragment khi nhấn vào "More"
            }
            if (selectedFragment != null)
                loadFragment(selectedFragment);
            return true;
        });
    };
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.default_fragment, fragment)
                .commit();
    }
    private void showPopupMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.setting_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    /*case R.id.dark_light_mode:

                        break;*/
                    /*case R.id.action_option2:

                        break;
                    case R.id.action_option3:

                        break;*/
                }
                return true;
            }
        });
        popupMenu.show();
    }
}