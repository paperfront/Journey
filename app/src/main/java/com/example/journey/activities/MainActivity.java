package com.example.journey.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.journey.R;
import com.example.journey.databinding.ActivityMainBinding;
import com.example.journey.fragments.AnalysisFragment;
import com.example.journey.fragments.MainPageFragment;
import com.example.journey.fragments.TimelineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private BottomNavigationView btmNavigation;
    private FrameLayout fragmentHolder;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    final Fragment analysisFragment = AnalysisFragment.newInstance();
    final Fragment homeFragment = MainPageFragment.newInstance();
    final Fragment timelineFragment = TimelineFragment.newInstance();

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
        fragmentHolder = binding.fragmentHolder;
    }

    private void setupElements() {
        setupNavigation();
    }

    private void setupNavigation() {

        btmNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = homeFragment;
                        break;
                    case R.id.action_analyze:
                        fragment = analysisFragment;
                        break;
                    case R.id.action_timeline:
                        fragment = timelineFragment;
                        break;
                    default:
                        Timber.e("Navigation item clicked does not have a case. Setting clicked item to home...");
                        fragment = homeFragment;
                }
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().replace(fragmentHolder.getId(), fragment).commit();
                return true;
            }
        });

        btmNavigation.setSelectedItemId(R.id.action_home);
    }


    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btLogout) {
            Timber.d("Logging out user");
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


