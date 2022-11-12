package com.example.duan1_mananger.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.duan1_mananger.R;

import com.example.duan1_mananger.databinding.ActivitySignInBinding;


public class LoginActivity extends AppCompatActivity {
    private ActivitySignInBinding binding = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBindingAnimation(binding);

        binding.tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this,SignUpActivity.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
    }

    private void setBindingAnimation(ActivitySignInBinding binding ){
        viewAnimation(binding.imgLogin,"translationY", -400f, 0f);
        viewAnimation(binding.tvTitle,"translationY", -400f, 0f);
        viewAnimation(binding.email,"translationX", -300f, 0f);
        viewAnimation(binding.password,"translationX", 300f, 0f);
        viewAnimation(binding.tvForgotPass,"translationY", -400f, 0f);
        viewAnimation(binding.cavButton,"translationY", 400f, 0f);
        viewAnimation(binding.tvContent,"translationX", -200f, 0f);
        viewAnimation(binding.tvSignUp,"translationX", 200f, 0f);
    }


    private void viewAnimation(View view, String ani, float... values){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,ani,values);
        animator.setDuration(1500);
        animator.start();

    }
}