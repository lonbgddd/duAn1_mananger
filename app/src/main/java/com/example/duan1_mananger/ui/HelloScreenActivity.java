package com.example.duan1_mananger.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duan1_mananger.R;

public class HelloScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ImageView img = findViewById(R.id.img_1);
        TextView text1 = findViewById(R.id.tex1);
        TextView text2 = findViewById(R.id.text2);
        TextView text3 = findViewById(R.id.textFooter);
        objAni(img,"translationY", -300f, 0f);
        objAni(text1,"translationY", 300f, 0f);
        objAni(text2,"translationY", 300f, 0f);
        objAni(text3,"translationY", 200f, 0f);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(HelloScreenActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }



    private void objAni(View view, String ani, float... values){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,ani,values);
        animator.setDuration(3000);
        animator.start();
    }
}