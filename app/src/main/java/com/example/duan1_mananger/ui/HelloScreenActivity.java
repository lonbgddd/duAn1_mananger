package com.example.duan1_mananger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duan1_mananger.MainActivity;
import com.example.duan1_mananger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HelloScreenActivity extends AppCompatActivity {
    ConstraintLayout layoutMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        layoutMain = findViewById(R.id.activityHelloScreen);
        ImageView img = findViewById(R.id.img_1);
        ImageView icLogo = findViewById(R.id.icLogo);
        TextView text1 = findViewById(R.id.tex1);
        TextView text2 = findViewById(R.id.text2);
        objAni(img,"translationY", -300f, 0f);
        objAni(icLogo,"translationY", -350f, 0f);
        objAni(text1,"translationY", 300f, 0f);
        objAni(text2,"translationY", 300f, 0f);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    Intent intent = new Intent(HelloScreenActivity.this, SignInActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                } else {
                    Intent intent = new Intent(HelloScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }


                finish();
            }
        },4000);
    }



    private void objAni(View view, String ani, float... values){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,ani,values);
        animator.setDuration(2500);
        animator.start();
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(layoutMain,"alpha", 0f, 1f);
        animator2.setDuration(2000);
        animator2.start();
    }
}