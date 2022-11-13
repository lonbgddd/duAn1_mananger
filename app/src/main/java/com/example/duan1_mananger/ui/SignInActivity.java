package com.example.duan1_mananger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.duan1_mananger.MainActivity;
import com.example.duan1_mananger.R;

import com.example.duan1_mananger.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setBindingAnimation();
        binding.tvForgotPass.setOnClickListener(v -> {
            startActivity(new Intent(this,ForgotPasswordActivity.class));
        });

        binding.tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this,SignUpActivity.class));
        });

        binding.btnLogin.setOnClickListener(v -> {
            if(validate()){
                onClickSignIn();
            }
        });
    }

    private void onClickSignIn() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        binding.email.setText(mAuth.getCurrentUser().getEmail().toString());
        String strEmail = binding.email.getText().toString().trim();
        String strPass = binding.password.getText().toString().trim();
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.layoutLogin.setAlpha(0.2f);
        mAuth.signInWithEmailAndPassword(strEmail, strPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            Toast.makeText(SignInActivity.this, getString(R.string.notifi_login_success), Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.VISIBLE);
                            finishAffinity();
                        } else {
                            Toast.makeText(SignInActivity.this,  getString(R.string.notifi_login_fail), Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                            binding.layoutLogin.setAlpha(1f);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private boolean validate(){
        String strEmail = binding.email.getText().toString().trim();
        String strPass = binding.password.getText().toString().trim();
        if(TextUtils.isEmpty(strEmail)){
            binding.email.setError(getString(R.string.error_email_1),null);
            binding.email.requestFocus();
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            binding.email.setError(getString(R.string.error_email_2),null);
            binding.email.requestFocus();
            return false;
        }else if(TextUtils.isEmpty(strPass)){
            binding.password.setError(getString(R.string.error_pass_1),null);
            binding.password.requestFocus();
            return false;
        }else {
            return true;
        }
    }

    public  void setBindingAnimation(){
        viewAnimation(binding.imgLogin,"translationY", -400f, 0f);
        viewAnimation(binding.tvTitle,"translationY", -400f, 0f);
        viewAnimation(binding.email,"translationX", -300f, 0f);
        viewAnimation(binding.tilPassword,"translationX", 300f, 0f);
        viewAnimation(binding.tvForgotPass,"translationY", -400f, 0f);
        viewAnimation(binding.cavButton,"translationY", 400f, 0f);
        viewAnimation(binding.tvContent,"translationX", -200f, 0f);
        viewAnimation(binding.tvSignUp,"translationX", 200f, 0f);
    }



    public  void viewAnimation(View view, String ani, float... values){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,ani,values);
        animator.setDuration(1500);
        animator.start();
    }


}