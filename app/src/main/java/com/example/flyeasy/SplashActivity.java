package com.example.flyeasy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    ImageView plane, logo, cloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        plane = findViewById(R.id.plane);
        logo = findViewById(R.id.logo);
        cloud = findViewById(R.id.cloud);

        Animation planeAnim = AnimationUtils.loadAnimation(this, R.anim.plane_move);
        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_pop);
        Animation cloudAnim = AnimationUtils.loadAnimation(this, R.anim.cloud_move);

        plane.startAnimation(planeAnim);
        cloud.startAnimation(cloudAnim);

        // Show logo after plane finishes
        new Handler().postDelayed(() -> {
            logo.setVisibility(View.VISIBLE);
            logo.startAnimation(logoAnim);
        }, 2000);

        // Move to next activity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 4000);
    }
}
