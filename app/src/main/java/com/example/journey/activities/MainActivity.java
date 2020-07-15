package com.example.journey.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.journey.R;
import com.example.journey.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private BottomNavigationView btmNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bindElements();
        setupElements();
    }

    private void bindElements() {
        btmNavigation = binding.bottomNavigation;
    }

    private void setupElements() {
        setupNavigation();
    }

    private void setupNavigation() {
        btmNavigation.setSelectedItemId(R.id.action_home);
    }
}