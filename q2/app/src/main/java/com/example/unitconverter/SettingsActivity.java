package com.example.unitconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup themeRadioGroup;
    private RadioButton lightThemeRadio;
    private RadioButton darkThemeRadio;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up Toolbar with back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }

        // Initialize views
        themeRadioGroup = findViewById(R.id.themeRadioGroup);
        lightThemeRadio = findViewById(R.id.lightThemeRadio);
        darkThemeRadio = findViewById(R.id.darkThemeRadio);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UnitConverterPrefs", MODE_PRIVATE);

        // Set the radio button based on the current theme
        int currentTheme = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            darkThemeRadio.setChecked(true);
        } else {
            lightThemeRadio.setChecked(true);
        }

        // Set listener for theme selection
        themeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.lightThemeRadio) {
                // Light theme selected
                sharedPreferences.edit().putInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(this, "Light theme applied", Toast.LENGTH_SHORT).show();
            } else if (checkedId == R.id.darkThemeRadio) {
                // Dark theme selected
                sharedPreferences.edit().putInt("theme_mode", AppCompatDelegate.MODE_NIGHT_YES).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(this, "Dark theme applied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Return to MainActivity when the back button in the action bar is pressed
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
