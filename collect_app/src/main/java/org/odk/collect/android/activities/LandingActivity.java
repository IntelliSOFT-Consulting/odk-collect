package org.odk.collect.android.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.window.SplashScreen;

import org.odk.collect.android.R;
import org.odk.collect.android.databinding.ActivityLandingBinding;

public class LandingActivity extends CollectAbstractActivity {
    private ActivityLandingBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CountDownTimer timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //  binding.textView.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(LandingActivity.this, FirstLaunchActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timer.start();

    }


}