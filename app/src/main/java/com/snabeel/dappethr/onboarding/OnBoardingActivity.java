package com.snabeel.dappethr.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.snabeel.dappethr.Constant;
import com.snabeel.dappethr.CreateImportWallet;
import com.snabeel.dappethr.MainActivity;
import com.snabeel.dappethr.PreferenceManager;
import com.snabeel.dappethr.R;
import com.snabeel.dappethr.databinding.ActivityOnBoardingBinding;

public class OnBoardingActivity extends AppCompatActivity {

    ActivityOnBoardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        updateStatusBarColorMainWhite();
//        getSupportActionBar().hide();
        OnBoardingAdapter adapter = new OnBoardingAdapter(this);
        binding.onboardingVP.setAdapter(adapter);
        binding.onboardingVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    binding.dot1.setImageResource(R.mipmap.onboarding_1);
                    binding.dot2.setImageResource(R.mipmap.onboarding_2);
                    binding.dot3.setImageResource(R.mipmap.onboarding_2);
                    binding.skipBtn.setText("Skip >>");
                } else if (position == 1) {
                    binding.dot1.setImageResource(R.mipmap.onboarding_2);
                    binding.dot2.setImageResource(R.mipmap.onboarding_1);
                    binding.dot3.setImageResource(R.mipmap.onboarding_2);
                    binding.skipBtn.setText("Skip >>");
                } else {
                    PreferenceManager.setBoolValue(Constant.IS_ON_BOARDING_VIEWED, true);
                    binding.dot1.setImageResource(R.mipmap.onboarding_2);
                    binding.dot2.setImageResource(R.mipmap.onboarding_2);
                    binding.dot3.setImageResource(R.mipmap.onboarding_1);
                    binding.skipBtn.setText("Continue >>");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.skipBtn.setOnClickListener(v->{
            startActivity(new Intent(OnBoardingActivity.this, CreateImportWallet.class));
            finish();
        });
    }


    public void updateStatusBarColorMainWhite() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#423D3D"));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int systemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                int flagLightStatusBar = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                systemUiVisibility |= flagLightStatusBar;
                getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
//                getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.background_color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}