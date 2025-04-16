import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity {
    package com.example.unit_convertor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

    public class SettingsActivity extends AppCompatActivity {

        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;
        private RadioGroup themeRadioGroup;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);

            sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            editor = sharedPreferences.edit();

            themeRadioGroup = findViewById(R.id.themeRadioGroup);

            // Load the saved theme
            loadTheme();

            // Listen for changes in the theme selection
            themeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                switch (checkedId) {
                    case R.id.lightThemeRadioButton:
                        setTheme(AppCompatDelegate.MODE_NIGHT_NO);
                        saveTheme(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                    case R.id.darkThemeRadioButton:
                        setTheme(AppCompatDelegate.MODE_NIGHT_YES);
                        saveTheme(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                }
            });
        }

        private void loadTheme() {
            int theme = sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(theme);

            if (theme == AppCompatDelegate.MODE_NIGHT_NO) {
                themeRadioGroup.check(R.id.lightThemeRadioButton);
            } else {
                themeRadioGroup.check(R.id.darkThemeRadioButton);
            }
        }

        private void saveTheme(int theme) {
            editor.putInt("theme", theme);
            editor.apply();
        }

        private void setTheme(int theme) {
            AppCompatDelegate.setDefaultNightMode(theme);
        }
    }
}
