package com.itnation.zioplayer.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itnation.zioplayer.MainFragment.FavoriteFragment;
import com.itnation.zioplayer.MainFragment.HomeFragment;
import com.itnation.zioplayer.MainFragment.MusicFragment;
import com.itnation.zioplayer.MainFragment.VideoFragment;
import com.itnation.zioplayer.R;

public class MainActivity extends AppCompatActivity {



    private FrameLayout frameView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameView = findViewById(R.id.frameView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Zio Player");
        }

        proceedWithAppFunctionality();


    }

    private void loadFragment(Fragment fragment, boolean isAppInitialize) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialize) {
            fragmentTransaction.add(R.id.frameView, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameView, fragment);
        }

        fragmentTransaction.commit();
    }






    private void proceedWithAppFunctionality() {
        // Load HomeFragment as the initial fragment
        loadFragment(new HomeFragment(), true);

        // Set up bottom navigation view listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.home) {
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle("Zio Player");
                    }

                    loadFragment(new HomeFragment(), false);
                    return true;
                } else if (itemId == R.id.video) {

                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle("Videos");
                    }

                    loadFragment(new VideoFragment(), false);
                    return true;
                } else if (itemId == R.id.music) {

                    ActionBar actionBar = getSupportActionBar();

                    if (actionBar != null) {
                        actionBar.setTitle("Audio");
                    }

                    loadFragment(new MusicFragment(), false);
                    return true;
                } else if (itemId == R.id.favorite) {

                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle("Favorites");
                    }

                    loadFragment(new FavoriteFragment(), false);
                    return true;
                }
                return false;
            }
        });
    }
}
