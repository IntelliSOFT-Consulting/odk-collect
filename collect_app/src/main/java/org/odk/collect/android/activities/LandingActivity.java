package org.odk.collect.android.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import org.odk.collect.android.R;
import org.odk.collect.android.databinding.ActivityLandingBinding;

public class LandingActivity extends CollectAbstractActivity {
    private ActivityLandingBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initToolbar();
        binding.btnProceed.setText(getString(R.string.proceed));
        binding.btnProceed.setOnClickListener(v -> {
            startActivity(new Intent(LandingActivity.this, MainMenuActivity.class));
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}