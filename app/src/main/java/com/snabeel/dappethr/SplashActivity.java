package com.snabeel.dappethr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.VideoView;

import com.snabeel.dappethr.accountsetup.activity.CreateImportWallet;
import com.snabeel.dappethr.onboarding.OnBoardingActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        getSupportActionBar().hide();
        videoView = findViewById(R.id.splashImage);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.splash4;
        videoView.setVideoPath(videoPath);
        videoView.start();
        PreferenceManager.init(this);
        Intent intent;
        if(PreferenceManager.getBoolValue(Constant.IS_ON_BOARDING_VIEWED)){
            if(!PreferenceManager.getBoolValue(Constant.IS_WALLET_SETUP)){
                intent = new Intent(SplashActivity.this, CreateImportWallet.class);
            }else{
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
        }else{
            intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
        }

        new Handler(Looper.getMainLooper()).postDelayed(()->{
            startActivity(intent);
            finish();
        }, 3000);
    }
}