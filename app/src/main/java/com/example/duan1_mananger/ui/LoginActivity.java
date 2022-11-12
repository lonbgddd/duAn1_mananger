package com.example.duan1_mananger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.duan1_mananger.MainActivity;
import com.example.duan1_mananger.R;

import com.example.duan1_mananger.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


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

        binding.btnLogin.setOnClickListener(v -> {
            if(validate()){
                onClickSignIn();
            }
        });
    }

    private void onClickSignIn() {
        String strEmail = binding.email.getText().toString().trim();
        String strPass = binding.password.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(strEmail, strPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                        }
                    }
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

    private boolean validate(){
        String strEmail = binding.email.getText().toString().trim();
        String strPass = binding.password.getText().toString().trim();
        if (strEmail.isEmpty() && strPass.isEmpty()){
            Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}