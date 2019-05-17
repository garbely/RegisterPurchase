package com.example.tv_shows.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.tv_shows.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
