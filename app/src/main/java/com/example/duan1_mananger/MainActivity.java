package com.example.duan1_mananger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.duan1_mananger.databinding.ActivityMainBinding;
import com.example.duan1_mananger.home.HomeFragment;
import com.example.duan1_mananger.maket.MaketFragment;
import com.example.duan1_mananger.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getSupportFragmentManager().beginTransaction().add(R.id.fade_control, HomeFragment.newInstance()).commit();
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_fragment:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, HomeFragment.newInstance()).commit();
                        break;
                    case R.id.maket_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, MaketFragment.newInstance()).commit();
                        break;
                    case R.id.menu_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, HomeFragment.newInstance()).commit();
                        break;
                    case R.id.setting_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, SettingFragment.newInstance()).commit();
                        break;
                }

                return true;
            }
        });
        setContentView(binding.getRoot());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}