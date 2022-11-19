package com.example.duan1_mananger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.duan1_mananger.databinding.ActivityMainBinding;
import com.example.duan1_mananger.home.HomeFragment;
import com.example.duan1_mananger.maket.MaketFragment;
import com.example.duan1_mananger.model.TypePoduct;
import com.example.duan1_mananger.product.ProductFragment;
import com.example.duan1_mananger.product.TypeProductFragment;
import com.example.duan1_mananger.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;

    public String text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = getWindow();
        getSupportFragmentManager().beginTransaction().add(R.id.fade_control, HomeFragment.newInstance()).commit();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(getColor(R.color.white));
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, HomeFragment.newInstance()).commit();
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        window.setStatusBarColor(getColor(R.color.white));
                        break;
                    case R.id.maket_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, MaketFragment.newInstance()).commit();
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        window.setStatusBarColor(getColor(R.color.white));
                        break;
                    case R.id.menu_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, ProductFragment.newInstance()).commit();
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        window.setStatusBarColor(getColor(R.color.white));
                        break;
                    case R.id.setting_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, SettingFragment.newInstance()).commit();
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        window.setStatusBarColor(getColor(R.color.brown_120));
                        break;
                }
                return true;
            }
        });


    }

    public String getText() {
        return text;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    public void ReplaceProductFragment(TypePoduct typePoduct) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        ProductFragment productFragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("objType", typePoduct);
        productFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fade_control, productFragment);
        fragmentTransaction.addToBackStack(ProductFragment.TAG);
        fragmentTransaction.commit();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));



    }

    public void ReplaceTypeFragment() {
        Window window = getWindow();
        getSupportFragmentManager().beginTransaction().add(R.id.fade_control, TypeProductFragment.newInstance()).commit();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(getColor(R.color.white));

    }
}