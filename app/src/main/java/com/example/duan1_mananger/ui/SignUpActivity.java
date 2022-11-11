package com.example.duan1_mananger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.duan1_mananger.MainActivity;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.databinding.ActivitySignInBinding;
import com.example.duan1_mananger.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding = null;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBindingAnimation(binding);
        progressDialog = new ProgressDialog(this);
        binding.btnSignin.setOnClickListener(v -> {
            createUser();
        });
        // long

    }
    private void createUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(binding.numberPhone.getText().toString(), binding.pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                            finishAffinity();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Bạn đã nhập sai mật khẩu hoặc email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    private void setBindingAnimation(ActivitySignUpBinding binding ){
        viewAnimation(binding.immSignUp,"translationY", -400f, 0f);
        viewAnimation(binding.tvTitle,"translationY", -400f, 0f);
        viewAnimation(binding.numberPhone,"translationX", -400f, 0f);
        viewAnimation(binding.pass,"translationX", 400f, 0f);
        viewAnimation(binding.rePass,"translationX", -400f, 0f);
        viewAnimation(binding.cavButton,"translationY", 400f, 0f);

    }

    private void viewAnimation(View view, String ani, float... values){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,ani,values);
        animator.setDuration(1500);
        animator.start();
    }
}













