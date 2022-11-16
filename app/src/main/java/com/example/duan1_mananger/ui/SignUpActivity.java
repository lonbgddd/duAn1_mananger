package com.example.duan1_mananger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.duan1_mananger.R;
import com.example.duan1_mananger.databinding.ActivitySignUpBinding;
import com.example.duan1_mananger.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBindingAnimation(binding);

        binding.btnSignup.setOnClickListener(v -> {
            if (validateInput()) {
                createUser();
            }

        });


    }

    private void createUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.layoutSignUp.setAlpha(0.2f);
        mAuth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(" ",
                                    "",
                                    true,
                                    "",
                                    "",
                                    "",
                                    binding.pass.getText().toString(),
                                    binding.email.getText().toString());
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            reference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "onComplete: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                Toast.makeText(SignUpActivity.this, getString(R.string.notifi_sign_up_success), Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                                binding.progressBar.setVisibility(View.VISIBLE);
                                                finishAffinity();
                                            }
                                        }
                                    });

                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            Toast.makeText(SignUpActivity.this, getString(R.string.notifi_sign_up_fail), Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                            binding.layoutSignUp.setAlpha(1f);
                        }
                    }
                });
    }

    private boolean validateInput() {
        String strEmail = binding.email.getText().toString().trim();
        String strPass = binding.pass.getText().toString().trim();
        String strRePass = binding.rePass.getText().toString().trim();
        if (TextUtils.isEmpty(strEmail)) {
            binding.email.setError(getString(R.string.error_email_1), null);
            binding.email.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            binding.email.setError(getString(R.string.error_email_2), null);
            binding.email.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(strPass)) {
            binding.pass.setError(getString(R.string.error_pass_1), null);
            binding.pass.requestFocus();
            return false;
        } else if (!(strRePass.matches(strPass)) || TextUtils.isEmpty(strRePass)) {
            binding.rePass.setError(getString(R.string.error_pass_2), null);
            binding.rePass.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    private void setBindingAnimation(ActivitySignUpBinding binding) {
        viewAnimation(binding.immSignUp, "translationY", -400f, 0f);
        viewAnimation(binding.tvTitle, "translationY", -400f, 0f);
        viewAnimation(binding.email, "translationX", -400f, 0f);
        viewAnimation(binding.tilPass, "translationX", 400f, 0f);
        viewAnimation(binding.tilRePass, "translationX", -400f, 0f);
        viewAnimation(binding.cavButton, "translationY", 400f, 0f);

    }

    private void viewAnimation(View view, String ani, float... values) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, ani, values);
        animator.setDuration(1500);
        animator.start();
    }
}













