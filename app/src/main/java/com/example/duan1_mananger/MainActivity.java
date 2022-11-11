package com.example.duan1_mananger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.duan1_mananger.databinding.ActivityMainBinding;
import com.example.duan1_mananger.home.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        getSupportFragmentManager().beginTransaction().add(R.id.fade_control, HomeFragment.newInstance()).commit();

    }
}