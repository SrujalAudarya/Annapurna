package com.example.internshipproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    Animation fadein;
    TextView slogan, app_name;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.app_logo);
        slogan = findViewById(R.id.app_slogan);
        app_name = findViewById(R.id.app_name);

        fadein = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.fadein);

        logo.setAnimation(fadein);
        slogan.setAnimation(fadein);
        app_name.setAnimation(fadein);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }
}