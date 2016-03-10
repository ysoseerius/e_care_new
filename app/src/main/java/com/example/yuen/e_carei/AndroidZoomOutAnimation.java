package com.example.yuen.e_carei;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.yuen.e_carei_login.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class AndroidZoomOutAnimation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_zoom_out_animation);
        //setupWindowAnimations();

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        //TextView textView = (TextView) findViewById(R.id.textView7);
        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
        imageView.startAnimation(zoomOutAnimation);
//       AlphaAnimation animation1 = new AlphaAnimation(0.1f, 1.0f);
//        animation1.setDuration(2000);
//        animation1.setFillAfter(true);
//        textView.startAnimation(animation1);

        int timeout = 1500; // make the activity visible for 4 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                Intent homepage = new Intent(AndroidZoomOutAnimation.this, LoginActivity.class);
                startActivity(homepage);
                finish();
                overridePendingTransition(0, R.anim.slide_out_left);
            }
        }, timeout);

    }

}
